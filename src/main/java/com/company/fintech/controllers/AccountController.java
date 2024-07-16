package com.company.fintech.controllers;

import com.company.fintech.domain.entities.Account;
import com.company.fintech.services.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/accounts")
@AllArgsConstructor
public class AccountController {
    private AccountService accountService;

    @PostMapping(path="")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account) {
        accountService.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping(path="/{accountId}")
    public ResponseEntity<Account> getAccount(@PathVariable("accountId") String accountId) {
        Account account = accountService.getAccount(accountId);

        return ResponseEntity.ok(account);
    }

}
