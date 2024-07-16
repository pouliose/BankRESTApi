package com.company.fintech.repositories;

import com.company.fintech.CreateTestDataUtil;
import com.company.fintech.domain.entities.Transaction;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@Log
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TransactionRepositoryIntegrationTests {

    private TransactionRepository transactionRepository;
    @Autowired
    public TransactionRepositoryIntegrationTests(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Test
    public void testTransactionCanBeCreatedAndRecalledWhenExists() {

        Transaction transaction = CreateTestDataUtil.createTestTransactionB("sourceId","targetId");
        transactionRepository.save(transaction);
        Transaction result = transactionRepository.findById(transaction.getId()).get();

        assertThat(result.getId()).isEqualTo(transaction.getId());
        assertThat(result.getSourceAccountId()).isEqualTo(transaction.getSourceAccountId());
        assertThat(result.getTargetAccountId()).isEqualTo(transaction.getTargetAccountId());
        assertThat(result.getAmount()).isEqualByComparingTo(transaction.getAmount());
        assertThat(result.getCurrency()).isEqualTo(transaction.getCurrency());
    }
}
