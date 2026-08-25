package com.interface_example.paymentGateway.service;

import com.interface_example.paymentGateway.service.PaymentTransactionService.PaymentResult;
import com.interface_example.paymentGateway.dto.PaymentRequest;

/**
 * Strategy interface for processing a specific payment method.
 */
public interface PaymentService {

    /**
     * Processes and saves a payment transaction.
     *
     * @param paymentRequest payment details
     * @return payment result
     */
    PaymentResult makePayment(PaymentRequest paymentRequest);
}