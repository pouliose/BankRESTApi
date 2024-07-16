package com.company.fintech.services;

import com.company.fintech.domain.entities.Transaction;

public interface TransactionService {
    Transaction createTransaction(Transaction Transaction);

    Transaction getTransaction(String id);
}
