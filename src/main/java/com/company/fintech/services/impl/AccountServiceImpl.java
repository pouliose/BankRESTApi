package com.company.fintech.services.impl;

import com.company.fintech.domain.entities.Account;
import com.company.fintech.exception.AccountNotFoundException;
import com.company.fintech.repositories.AccountRepository;
import com.company.fintech.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) {
        account.setCreatedAt(LocalDateTime.now());

        return accountRepository.save(account);
    }

    @Override
    public Account getAccount(String id) {
        return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account with " + id + " not found"));
    }
}
