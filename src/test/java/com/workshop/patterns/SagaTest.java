package com.workshop.patterns;

import com.workshop.patterns.saga.AccountService;
import com.workshop.patterns.saga.CardService;
import com.workshop.patterns.saga.CreditApprovalSaga;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SagaTest {

    private AccountService accountService;
    private CardService cardService;
    private CreditApprovalSaga saga;

    @BeforeEach
    public void setup() {
        accountService = new AccountService();
        cardService = new CardService();
        saga = new CreditApprovalSaga(accountService, cardService);
    }

    @Test
    public void testSagaSuccess() {
        String result = saga.approveCredit("user123", false);

        assertEquals("ACTIVA", accountService.getAccountStatus("user123"));
        assertEquals("EMITIDA", cardService.getCardStatus("user123"));
        assertTrue(result.contains("Aprobado"));
    }

    @Test
    public void testSagaCompensation() {
        String result = saga.approveCredit("user123", true);

        assertEquals("ANULADA", accountService.getAccountStatus("user123"));
        assertTrue(result.contains("Denegado"));
    }
}