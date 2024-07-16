package com.company.fintech.controllers;

import com.company.fintech.CreateTestDataUtil;
import com.company.fintech.domain.entities.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AccountControllerIntegrationTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    public AccountControllerIntegrationTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void testThatCreateAccountSuccessfullyReturnsHttp201Created() throws Exception {
        Account accountA = CreateTestDataUtil.createTestAccountA();
        String testAccountAJson = objectMapper.writeValueAsString(accountA);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testAccountAJson)
        ).andExpect(
                status().isCreated()
        );
    }

    @Test
    public void testThatCreateAccountSuccessfullyReturnsSavedAccount() throws Exception {
        Account testAccountA = CreateTestDataUtil.createTestAccountA();
        String testAccountAJson = objectMapper.writeValueAsString(testAccountA);

        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testAccountAJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.balance").value(testAccountA.getBalance())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.currency").value(testAccountA.getCurrency())
        );
    }

    @Test
    public void testThatGetAccountReturnsHttpStatus404WhenAnyAccountExists() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/accounts/zzzzzzzzzzzzzzzzzz")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        status().isNotFound());
    }
}
