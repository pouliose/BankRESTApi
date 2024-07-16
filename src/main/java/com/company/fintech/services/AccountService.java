package com.company.fintech.services;

import com.company.fintech.domain.entities.Account;

public interface AccountService {
    Account createAccount(Account account);

    Account getAccount(String id);
}