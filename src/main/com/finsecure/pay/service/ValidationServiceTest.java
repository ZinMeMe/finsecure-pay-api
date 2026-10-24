// src/test/java/com/finsecure/pay/service/ValidationServiceTest.java
package com.finsecure.pay.service;

import com.finsecure.pay.model.TransactionRequest;
import com.finsecure.pay.model.TransactionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ValidationServiceTest {

    private ValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new ValidationService();
    }

    @Test
    void testValidTransactionShouldBeApproved() {
        TransactionRequest req = new TransactionRequest();
        req.setTransactionId("TXN-1001");
        req.setAmount(new BigDecimal("300.00"));
        req.setCurrency("SGD");
        req.setSourceAccount("ACC-123");
        req.setDestinationAccount("ACC-456");

        TransactionResponse res = validationService.validateTransaction(req);
        assertEquals("APPROVED", res.getStatus());
    }

    @Test
    void testExceedsLimitShouldBeRejected() {
        TransactionRequest req = new TransactionRequest();
        req.setTransactionId("TXN-1002");
        req.setAmount(new BigDecimal("75000.00"));
        req.setCurrency("SGD");
        req.setSourceAccount("ACC-123");
        req.setDestinationAccount("ACC-456");

        TransactionResponse res = validationService.validateTransaction(req);
        assertEquals("REJECTED", res.getStatus());
        assertTrue(res.getMessage().contains("exceeds maximum limit"));
    }

    @Test
    void testValidTransactionShouldBeApproved() {
        TransactionRequest req = new TransactionRequest();
        req.setTransactionId("TXN-1001");
        req.setAmount(new BigDecimal("250.00"));
        req.setCurrency("USD");
        req.setSourceAccount("ACC-123");
        req.setDestinationAccount("ACC-456");

        TransactionResponse res = validationService.validateTransaction(req);
        assertEquals("APPROVED", res.getStatus());
    }

    @Test
    void testExceedsLimitShouldBeRejected() {
        TransactionRequest req = new TransactionRequest();
        req.setTransactionId("TXN-1002");
        req.setAmount(new BigDecimal("75000.00"));
        req.setCurrency("USD");
        req.setSourceAccount("ACC-123");
        req.setDestinationAccount("ACC-456");

        TransactionResponse res = validationService.validateTransaction(req);
        assertEquals("REJECTED", res.getStatus());
        assertTrue(res.getMessage().contains("exceeds maximum limit"));
    }

    @Test
    void testBlockedAccountShouldBeRejected() {
        TransactionRequest req = new TransactionRequest();
        req.setTransactionId("TXN-1003");
        req.setAmount(new BigDecimal("100.00"));
        req.setCurrency("EUR");
        req.setSourceAccount("BLOCKED-ACC-001");
        req.setDestinationAccount("ACC-456");

        TransactionResponse res = validationService.validateTransaction(req);
        assertEquals("REJECTED", res.getStatus());
        assertTrue(res.getMessage().contains("source account is blocked"));
    }
}ßß