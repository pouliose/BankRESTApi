package com.company.fintech;

import com.company.fintech.domain.entities.Account;
import com.company.fintech.domain.entities.Transaction;

import java.math.BigDecimal;

public class CreateTestDataUtil {

    public static Account createTestAccountA() {
        return Account.builder()
                .balance(new BigDecimal(10))
                .currency("USD")
                .build();
    }

    public static Account createTestAccountB() {
        return Account.builder()
                .balance(new BigDecimal(99))
                .currency("USD")
                .build();
    }

    public static Transaction createTestTransactionA(String sourceId, String targetId) {
        return Transaction.builder()
                .sourceAccountId(sourceId)
                .targetAccountId(targetId)
                .amount(new BigDecimal(5))
                .currency("USD")
                .build();
    }

    public static Transaction createTestTransactionB(String sourceId, String targetId) {
        return Transaction.builder()
                .sourceAccountId(sourceId)
                .targetAccountId(targetId)
                .amount(new BigDecimal(9))
                .currency("GBR")
                .build();
    }
}
