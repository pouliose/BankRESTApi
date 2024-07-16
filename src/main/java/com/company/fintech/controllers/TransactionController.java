package com.company.fintech.controllers;

import com.company.fintech.domain.entities.Transaction;
import com.company.fintech.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import com.company.fintech.services.AccountService;
import com.company.fintech.validations.TransactionValidations;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/transfers")
@AllArgsConstructor
public class TransactionController {
    private TransactionService transactionService;
    private AccountService accountService;

    @PostMapping(path="")
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody Transaction transaction) {

        return checkNewTransactionRequestAndExecute(transaction, accountService);
    }

    private ResponseEntity<Transaction> checkNewTransactionRequestAndExecute(Transaction transaction, AccountService accountService) {

        validateNewTransactionRequest(transaction, accountService);

        transactionService.createTransaction(transaction);

        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    private static void validateNewTransactionRequest(Transaction transaction, AccountService accountService) {

        TransactionValidations validations = new TransactionValidations(accountService);

        validations.validateTransaction(transaction);
    }

    @GetMapping(path="/{transactionId}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable("transactionId") String transactionId) {
        Transaction transaction = transactionService.getTransaction(transactionId);

        return ResponseEntity.ok(transaction);
    }
}
