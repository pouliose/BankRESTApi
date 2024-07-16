package com.company.fintech.controllers;

import com.company.fintech.CreateTestDataUtil;
import com.company.fintech.domain.entities.Account;
import com.company.fintech.domain.entities.Transaction;
import com.company.fintech.enums.ResponseStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@Log
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class TransactionControllerIntegrationTest {
    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    public TransactionControllerIntegrationTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void testThatCreateTransactionReturnsHttp201CreatedWhenAccountsExist() throws Exception {

        Account sourceAccount = CreateTestDataUtil.createTestAccountA();
        String sourceAccountJson = objectMapper.writeValueAsString(sourceAccount);

        MvcResult sourceResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sourceAccountJson)
        ).andReturn();

        String sourceAccountId = JsonPath.read(sourceResult.getResponse().getContentAsString(), "$.id");
        sourceAccount.setId(sourceAccountId);

        Account targetAccount = CreateTestDataUtil.createTestAccountB();
        String targetAccountJson = objectMapper.writeValueAsString(targetAccount);

        MvcResult targetResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(targetAccountJson)
        ).andReturn();

        String targetAccountId = JsonPath.read(targetResult.getResponse().getContentAsString(), "$.id");
        targetAccount.setId(targetAccountId);

        Transaction testTransactionA = CreateTestDataUtil.createTestTransactionA(sourceAccountId, targetAccountId);
        String testTransactionAJson = objectMapper.writeValueAsString(testTransactionA);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testTransactionAJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateTransactionReturnsHttpBadRequestWhenSourceAccountDoesNotExist() throws Exception {

        Account targetAccount = CreateTestDataUtil.createTestAccountB();
        String targetAccountJson = objectMapper.writeValueAsString(targetAccount);

        MvcResult targetResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(targetAccountJson)
        ).andReturn();

        String targetAccountId = JsonPath.read(targetResult.getResponse().getContentAsString(), "$.id");
        targetAccount.setId(targetAccountId);

        String sourceId = "sourceAccountIdNotExist";

        Transaction testTransactionA = CreateTestDataUtil.createTestTransactionA(sourceId, targetAccountId);
        String testTransactionAJson = objectMapper.writeValueAsString(testTransactionA);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/transfers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testTransactionAJson)
                ).andExpect(
                        MockMvcResultMatchers.status().isNotFound())
                .andExpect(content().string("Account with " + sourceId + " not found")
                );
    }

    @Test
    public void testThatCreateTransactionReturnsHttpBadRequestWhenTargetAccountDoesNotExist() throws Exception {

        Account sourceAccount = CreateTestDataUtil.createTestAccountA();

        String sourceAccountJson = objectMapper.writeValueAsString(sourceAccount);

        MvcResult sourceResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sourceAccountJson)
        ).andReturn();

        String sourceAccountId = JsonPath.read(sourceResult.getResponse().getContentAsString(), "$.id");
        sourceAccount.setId(sourceAccountId);

        String targetId = "targetAccountIdDoNotExist";
        Transaction testTransactionA = CreateTestDataUtil.createTestTransactionA(sourceAccountId, targetId);
        String testTransactionAJson = objectMapper.writeValueAsString(testTransactionA);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/transfers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testTransactionAJson)
                ).andExpect(
                        MockMvcResultMatchers.status().isNotFound())
                .andExpect(content().string("Account with " + targetId + " not found")
                );
    }

    @Test
    public void testThatCreateTransactionReturnsHttpBadRequestWhenSourceAccountHasInsufficientBalance() throws Exception {

        Account sourceAccount = CreateTestDataUtil.createTestAccountA();

        String sourceAccountJson = objectMapper.writeValueAsString(sourceAccount);

        MvcResult sourceResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sourceAccountJson)
        ).andReturn();

        String sourceAccountId = JsonPath.read(sourceResult.getResponse().getContentAsString(), "$.id");
        sourceAccount.setId(sourceAccountId);

        Account targetAccount = CreateTestDataUtil.createTestAccountB();
        String targetAccountJson = objectMapper.writeValueAsString(targetAccount);

        MvcResult targetResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(targetAccountJson)
        ).andReturn();

        String targetAccountId = JsonPath.read(targetResult.getResponse().getContentAsString(), "$.id");
        targetAccount.setId(targetAccountId);

        Transaction testTransactionA = CreateTestDataUtil.createTestTransactionA(sourceAccountId, targetAccountId);
        testTransactionA.setAmount(sourceAccount.getBalance().add(BigDecimal.valueOf(1)));

        String testTransactionAJson = objectMapper.writeValueAsString(testTransactionA);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/transfers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testTransactionAJson)
                ).andExpect(
                        MockMvcResultMatchers.status().isBadRequest())
                .andExpect(
                        content().string((ResponseStatus.INSUFFICIENT_BALANCE_FOR_MONEY_TRANSFER.getDescription()))
                );
    }

    @Test
    public void testThatCreateTransactionReturnsHttpBadRequestWhenTransactionAmountIsZero() throws Exception {

        Account sourceAccount = CreateTestDataUtil.createTestAccountA();

        String sourceAccountJson = objectMapper.writeValueAsString(sourceAccount);

        MvcResult sourceResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sourceAccountJson)
        ).andReturn();

        String sourceAccountId = JsonPath.read(sourceResult.getResponse().getContentAsString(), "$.id");
        sourceAccount.setId(sourceAccountId);

        Account targetAccount = CreateTestDataUtil.createTestAccountB();
        String targetAccountJson = objectMapper.writeValueAsString(targetAccount);

        MvcResult targetResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(targetAccountJson)
        ).andReturn();

        String targetAccountId = JsonPath.read(targetResult.getResponse().getContentAsString(), "$.id");
        targetAccount.setId(targetAccountId);

        Transaction testTransactionA = CreateTestDataUtil.createTestTransactionA(sourceAccountId, targetAccountId);
        testTransactionA.setAmount(BigDecimal.valueOf(0));

        String testTransactionAJson = objectMapper.writeValueAsString(testTransactionA);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/transfers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testTransactionAJson)
                ).andExpect(
                        MockMvcResultMatchers.status().isBadRequest())
                .andExpect(
                        content().string((ResponseStatus.TRANSACTION_AMOUNT_IS_NOT_VALID.getDescription()))
                );
    }

    @Test
    public void testThatCreateTransactionReturnsHttpBadRequestWhenSourceAndTargetAccountsAreTheSame() throws Exception {

        Account sourceAccount = CreateTestDataUtil.createTestAccountA();

        String sourceAccountJson = objectMapper.writeValueAsString(sourceAccount);

        MvcResult sourceResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sourceAccountJson)
        ).andReturn();

        String sourceAccountId = JsonPath.read(sourceResult.getResponse().getContentAsString(), "$.id");
        sourceAccount.setId(sourceAccountId);

        Transaction testTransactionA = CreateTestDataUtil.createTestTransactionA(sourceAccountId, sourceAccountId);

        String testTransactionAJson = objectMapper.writeValueAsString(testTransactionA);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/transfers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testTransactionAJson)
                ).andExpect(
                        MockMvcResultMatchers.status().isBadRequest())
                .andExpect(
                        content().string((ResponseStatus.SOURCE_AND_TARGET_ACCOUNT_ARE_THE_SAME.getDescription()))
                );
    }

    @Test
    public void testThatCreateTransactionReturnsHttpBadRequestWhenSourceAndTargetAccountsHaveDifferentCurrency() throws Exception {

        Account sourceAccount = CreateTestDataUtil.createTestAccountA();

        String sourceAccountJson = objectMapper.writeValueAsString(sourceAccount);

        MvcResult sourceResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sourceAccountJson)
        ).andReturn();

        String sourceAccountId = JsonPath.read(sourceResult.getResponse().getContentAsString(), "$.id");
        sourceAccount.setId(sourceAccountId);

        Account targetAccount = CreateTestDataUtil.createTestAccountB();
        targetAccount.setCurrency("dummy");
        String targetAccountJson = objectMapper.writeValueAsString(targetAccount);

        MvcResult targetResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(targetAccountJson)
        ).andReturn();

        String targetAccountId = JsonPath.read(targetResult.getResponse().getContentAsString(), "$.id");
        targetAccount.setId(targetAccountId);

        Transaction testTransactionA = CreateTestDataUtil.createTestTransactionA(sourceAccountId, targetAccountId);

        String testTransactionAJson = objectMapper.writeValueAsString(testTransactionA);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/transfers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testTransactionAJson)
                ).andExpect(
                        MockMvcResultMatchers.status().isBadRequest())
                .andExpect(
                        content().string((ResponseStatus.SOURCE_AND_TARGET_ACCOUNTS_CURRENCY_ARE_DIFFERENT.getDescription()))
                );
    }

    @Test
    public void testThatCreateTransactionReturnsHttpBadRequestWhenTransactionCurrencyDiffersFromThatOfSourceAndTargetAccounts() throws Exception {

        Account sourceAccount = CreateTestDataUtil.createTestAccountA();

        String sourceAccountJson = objectMapper.writeValueAsString(sourceAccount);

        MvcResult sourceResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sourceAccountJson)
        ).andReturn();

        String sourceAccountId = JsonPath.read(sourceResult.getResponse().getContentAsString(), "$.id");
        sourceAccount.setId(sourceAccountId);

        Account targetAccount = CreateTestDataUtil.createTestAccountB();
        String targetAccountJson = objectMapper.writeValueAsString(targetAccount);

        MvcResult targetResult = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(targetAccountJson)
        ).andReturn();

        String targetAccountId = JsonPath.read(targetResult.getResponse().getContentAsString(), "$.id");
        targetAccount.setId(targetAccountId);

        Transaction testTransactionA = CreateTestDataUtil.createTestTransactionA(sourceAccountId, targetAccountId);
        testTransactionA.setCurrency("dummy");

        String testTransactionAJson = objectMapper.writeValueAsString(testTransactionA);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/transfers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testTransactionAJson)
                ).andExpect(
                        MockMvcResultMatchers.status().isBadRequest())
                .andExpect(
                        content().string((ResponseStatus.CURRENCY_OF_TRANSACTION_DIFFERS_TO_THAT_OF_ACCOUNTS.getDescription()))
                );
    }

    @Test
    public void testThatGetTransactionReturnsHttpStatus404WhenAnyTransactionExists() throws Exception {
        String transactionId = "zzzzzzzzzzzzzzzzzz";
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/transfers/" + transactionId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        MockMvcResultMatchers.status().isNotFound()
                ).andExpect(content().string("Transaction with " + transactionId + " not found")
                );
    }
}