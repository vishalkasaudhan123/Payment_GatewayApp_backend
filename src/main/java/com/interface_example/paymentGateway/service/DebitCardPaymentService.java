package com.interface_example.paymentGateway.service;

import org.springframework.stereotype.Service;


import com.interface_example.paymentGateway.service.PaymentTransactionService.PaymentResult;
import com.interface_example.paymentGateway.util.TransactionIdGenerator;
import com.interface_example.paymentGateway.dto.PaymentRequest;
import com.interface_example.paymentGateway.entity.FailedPaymentTransaction;
import com.interface_example.paymentGateway.entity.SuccessPaymentTransaction;
import com.interface_example.paymentGateway.repository.FailedPaymentTransactionRepository;
import com.interface_example.paymentGateway.repository.SuccessPaymentTransactionRepository;

/**
 * Handles Debit Card payments.
 */
@Service("debit")
public class DebitCardPaymentService implements PaymentService {

    private final SuccessPaymentTransactionRepository successRepository;
    private final FailedPaymentTransactionRepository failedRepository;
    private final TransactionIdGenerator transactionIdGenerator;

    public DebitCardPaymentService(
            SuccessPaymentTransactionRepository successRepository,
            FailedPaymentTransactionRepository failedRepository,
            TransactionIdGenerator transactionIdGenerator) {

        this.successRepository = successRepository;
        this.failedRepository = failedRepository;
        this.transactionIdGenerator = transactionIdGenerator;
    }

    @Override
    public PaymentResult makePayment(PaymentRequest paymentRequest) {

        String transactionId = transactionIdGenerator.generateTransactionId();

        if (paymentRequest == null) {
            return new PaymentResult(
                    false,
                    transactionId,
                    "Payment request is required.");
        }

        if (paymentRequest.getAmount() <= 0) {
            return saveFailure(
                    paymentRequest,
                    transactionId,
                    "Invalid amount.");
        }

        if (paymentRequest.getCard() == null
                || paymentRequest.getCard().length() < 4) {

            return saveFailure(
                    paymentRequest,
                    transactionId,
                    "Valid card number is required.");
        }

        if (paymentRequest.getCvv() == null
                || paymentRequest.getCvv().isBlank()) {

            return saveFailure(
                    paymentRequest,
                    transactionId,
                    "CVV is required.");
        }

        String maskedCard = maskCard(paymentRequest.getCard());

        String message =
                "Debit card payment of ₹"
                        + paymentRequest.getAmount()
                        + " using card "
                        + maskedCard
                        + " successful";

        SuccessPaymentTransaction transaction =
                new SuccessPaymentTransaction();

        transaction.setTransactionId(transactionId);
        transaction.setAmount(paymentRequest.getAmount());
        transaction.setMethod(paymentRequest.getMethod());
//        transaction.setMethod("debit");
        transaction.setMaskedCard(maskedCard);
        transaction.setSuccessMessage(message);

        successRepository.save(transaction);

        return new PaymentResult(
                true,
                transactionId,
                message);
    }

    private PaymentResult saveFailure(
            PaymentRequest paymentRequest,
            String transactionId,
            String reason) {

        FailedPaymentTransaction transaction =
                new FailedPaymentTransaction();

        transaction.setTransactionId(transactionId);
        transaction.setAmount(paymentRequest.getAmount());
        transaction.setMethod(paymentRequest.getMethod());
//        transaction.setMethod("debit");

        if (paymentRequest.getCard() != null
                && paymentRequest.getCard().length() >= 4) {

            transaction.setMaskedCard(
                    maskCard(paymentRequest.getCard()));
        }

        transaction.setFailureReason(reason);

        failedRepository.save(transaction);

        return new PaymentResult(
                false,
                transactionId,
                reason);
    }

    private String maskCard(String cardNumber) {

        return "**** **** **** "
                + cardNumber.substring(cardNumber.length() - 4);
    }
}