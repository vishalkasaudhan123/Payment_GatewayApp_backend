package com.interface_example.paymentGateway.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

/**
 * Generates unique transaction IDs.
 */
@Component
public class TransactionIdGenerator {

    /**
     * Generates a transaction ID.
     *
     * @return unique transaction ID
     */
    public String generateTransactionId() {

        String uuidPart = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();

        return "TXN-" + uuidPart;
    }
}