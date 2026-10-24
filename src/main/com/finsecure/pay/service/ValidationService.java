package com.finsecure.pay.service;

import com.finsecure.pay.model.TransactionRequest;
import com.finsecure.pay.model.TransactionResponse;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class ValidationService {

    private static final BigDecimal MAX_TRANSACTION_LIMIT = new BigDecimal("50000.00");

    public TransactionResponse validateTransaction(TransactionRequest request) {
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return new TransactionResponse(request.getTransactionId(), "REJECTED", "Invalid transaction amount.");
        }
        if (request.getAmount().compareTo(MAX_TRANSACTION_LIMIT) > 0) {
            return new TransactionResponse(request.getTransactionId(), "REJECTED", "Transaction exceeds maximum limit of 50,000.");
        }
        if (request.getCurrency() == null || !request.getCurrency().matches("SDG|USD|EUR|GBP")) {
            return new TransactionResponse(request.getTransactionId(), "REJECTED", "Unsupported currency.");
        }
        if (request.getSourceAccount() != null && request.getSourceAccount().equals("BLOCKED-ACC-001")) {
            return new TransactionResponse(request.getTransactionId(), "REJECTED", "Source account is blocked.");
        }

        return new TransactionResponse(request.getTransactionId(), "APPROVED", "Transaction validated successfully.");
    }
}