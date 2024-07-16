package com.company.fintech.repositories;

import com.company.fintech.CreateTestDataUtil;
import com.company.fintech.domain.entities.Account;
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
public class AccountRepositoryIntegrationTests {

    private AccountRepository accountRepository;
    @Autowired
    public AccountRepositoryIntegrationTests(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Test
    public void testAccountCanBeCreatedAndRecalledWhenExists() {

        Account accountA = CreateTestDataUtil.createTestAccountA();
        accountRepository.save(accountA);
        Account result = accountRepository.findById(accountA.getId()).get();

        assertThat(result.getId()).isEqualTo(accountA.getId());
        assertThat(result.getBalance()).isEqualByComparingTo(accountA.getBalance());
        assertThat(result.getCurrency()).isEqualTo(accountA.getCurrency());
    }
}
