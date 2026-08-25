package com.interface_example.paymentGateway.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.interface_example.paymentGateway.dto.PaymentRequest;
import com.interface_example.paymentGateway.entity.FailedPaymentTransaction;
import com.interface_example.paymentGateway.entity.SuccessPaymentTransaction;
import com.interface_example.paymentGateway.repository.FailedPaymentTransactionRepository;
import com.interface_example.paymentGateway.repository.SuccessPaymentTransactionRepository;

/**
 * Coordinates payment processing by selecting the appropriate
 * payment strategy.
 *
 * <p>
 * Payment-specific business logic and transaction saving are
 * handled by the individual PaymentService implementations.
 * </p>
 */
@Service
public class PaymentTransactionService {

    private final Map<String, PaymentService> paymentServices;
    private final SuccessPaymentTransactionRepository successRepository;
    private final FailedPaymentTransactionRepository failedRepository;

    public PaymentTransactionService(
            Map<String, PaymentService> paymentServices,
            SuccessPaymentTransactionRepository successRepository,
            FailedPaymentTransactionRepository failedRepository) {

        this.paymentServices = paymentServices;
        this.successRepository = successRepository;
        this.failedRepository = failedRepository;
    }

    /**
     * Selects the payment strategy and delegates payment processing.
     *
     * @param paymentRequest payment details
     * @return payment result
     */
    public PaymentResult processPayment(PaymentRequest paymentRequest) {

        if (paymentRequest == null) {

            return new PaymentResult(
                    false,
                    null,
                    "Payment request is required.");
        }

        if (paymentRequest.getMethod() == null
                || paymentRequest.getMethod().isBlank()) {

            return new PaymentResult(
                    false,
                    null,
                    "Payment method is required.");
        }
        
        String method =
                paymentRequest.getMethod().toLowerCase();

        PaymentService paymentService =
                paymentServices.get(method);
                 

        if (paymentService == null) {

            return new PaymentResult(
                    false,
                    null,
                    "Payment method not found.");
        }

        return paymentService.makePayment(paymentRequest);
    }

    /**
     * Returns all successful transactions.
     *
     * @return successful transactions
     */
    public List<SuccessPaymentTransaction> getAllTransactions() {
        return successRepository.findAll();
    }

    /**
     * Returns all failed transactions.
     *
     * @return failed transactions
     */
    public List<FailedPaymentTransaction> getAllFailedTransactions() {
        return failedRepository.findAll();
    }

    /**
     * Represents the result of a payment operation.
     *
     * @param success whether the payment succeeded
     * @param transactionId generated transaction ID
     * @param message payment result message
     */
    public record PaymentResult(
            boolean success,
            String transactionId,
            String message) {
    }
}