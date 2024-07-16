package com.company.fintech.services.impl;

import com.company.fintech.domain.entities.Account;
import com.company.fintech.domain.entities.Transaction;
import com.company.fintech.exception.TransactionNotFoundException;
import com.company.fintech.services.AccountService;
import com.company.fintech.services.TransactionService;
import com.company.fintech.repositories.AccountRepository;
import com.company.fintech.repositories.TransactionRepository;
import com.company.fintech.validations.TransactionValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private final AccountRepository accountRepository;
    @Autowired
    private final TransactionRepository transactionRepository;
    @Autowired
    private final AccountService accountService;

    public TransactionServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository, AccountService accountService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {

        TransactionValidations transactionValidations = new TransactionValidations(accountService);

        transactionValidations.validateTransaction(transaction);

        updateSourceAccountBalance(transaction, accountService, accountRepository);

        updateTargetAmount(transaction, accountService, accountRepository);

        transaction.setExecutedAt(LocalDateTime.now());

        Optional<Account> sourceAccount = accountRepository.findById(transaction.getSourceAccountId());
        sourceAccount.map(a -> a.getTransactions().add(transaction));

        Optional<Account> targetAccount = accountRepository.findById(transaction.getTargetAccountId());
        targetAccount.map(a -> a.getTransactions().add(transaction));

        return transactionRepository.save(transaction);
    }

    private static void updateSourceAccountBalance(Transaction transaction, AccountService accountService, AccountRepository accountRepository) {
        Account sourceAccount = accountService.getAccount(transaction.getSourceAccountId());
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(transaction.getAmount()));
        accountRepository.save(sourceAccount);
    }

    private void updateTargetAmount(Transaction transaction, AccountService accountService, AccountRepository accountRepository) {
        Account targetAccount = accountService.getAccount(transaction.getTargetAccountId());
        targetAccount.setBalance(targetAccount.getBalance().add(transaction.getAmount()));
        accountRepository.save(targetAccount);
    }

    @Override
    public Transaction getTransaction(String id) {
        return transactionRepository.findById(id).orElseThrow(()-> new TransactionNotFoundException("Transaction with " + id + " not found"));
    }
}
