package com.company.fintech.validations;

import com.company.fintech.domain.entities.Account;
import com.company.fintech.domain.entities.Transaction;
import com.company.fintech.enums.ResponseStatus;
import com.company.fintech.exception.InvalidAccountException;
import com.company.fintech.exception.InvalidCurrencyException;
import com.company.fintech.exception.InvalidTransactionAmountException;
import com.company.fintech.services.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Slf4j
@AllArgsConstructor
public class TransactionValidations {

    @Autowired
    private final AccountService accountService;

    public void validateTransaction(Transaction transaction) {

        validateAccountsExistence(transaction);
        validateAccountsAreDifferent(transaction);
        validateCurrencyOfAccountsAndTransactionAreTheSame(transaction);
        validateTransactionAmount(transaction);
    }

    public void validateAccountsExistence(Transaction transaction) {
        Account sourceAccount = accountService.getAccount(transaction.getSourceAccountId());
        Account targetAccount = accountService.getAccount(transaction.getTargetAccountId());
    }

    public void validateAccountsAreDifferent(Transaction transaction) {
        if(transaction.getSourceAccountId().equals(transaction.getTargetAccountId()))
            throw new InvalidAccountException(ResponseStatus.SOURCE_AND_TARGET_ACCOUNT_ARE_THE_SAME.getDescription());
    }

    private void validateCurrencyOfAccountsAndTransactionAreTheSame(Transaction transaction) {
        Account sourceAccount = accountService.getAccount(transaction.getSourceAccountId());
        Account targetAccount = accountService.getAccount(transaction.getTargetAccountId());

        if(!sourceAccount.getCurrency().equals(targetAccount.getCurrency()))
            throw new InvalidCurrencyException(ResponseStatus.SOURCE_AND_TARGET_ACCOUNTS_CURRENCY_ARE_DIFFERENT.getDescription());
        else if(!sourceAccount.getCurrency().equals(transaction.getCurrency()))
            throw new InvalidCurrencyException(ResponseStatus.CURRENCY_OF_TRANSACTION_DIFFERS_TO_THAT_OF_ACCOUNTS.getDescription());
    }

    public void validateTransactionAmount(Transaction transaction) {
        Account sourceAccount = accountService.getAccount(transaction.getSourceAccountId());
        checkTransactionAmount(transaction, sourceAccount);
    }

    private void checkTransactionAmount(Transaction transaction, Account sourceAccountOptional) {
        BigDecimal sourceAccountBalance = sourceAccountOptional.getBalance();

        if (transaction.getAmount().compareTo(BigDecimal.valueOf(0)) <= 0) {
            throw new InvalidTransactionAmountException(ResponseStatus.TRANSACTION_AMOUNT_IS_NOT_VALID.getDescription());
        } else if (sourceAccountBalance.compareTo(transaction.getAmount()) <= 0) {
           throw new InvalidTransactionAmountException(ResponseStatus.INSUFFICIENT_BALANCE_FOR_MONEY_TRANSFER.getDescription());
        }
    }
}
