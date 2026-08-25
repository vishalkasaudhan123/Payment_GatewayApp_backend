package com.interface_example.paymentGateway.service;

import org.springframework.stereotype.Service;

import com.interface_example.paymentGateway.util.TransactionIdGenerator;
import com.interface_example.paymentGateway.dto.PaymentRequest;
import com.interface_example.paymentGateway.entity.FailedPaymentTransaction;
import com.interface_example.paymentGateway.entity.SuccessPaymentTransaction;
import com.interface_example.paymentGateway.repository.FailedPaymentTransactionRepository;
import com.interface_example.paymentGateway.repository.SuccessPaymentTransactionRepository;
import com.interface_example.paymentGateway.service.PaymentTransactionService.PaymentResult;

/**
 * Handles UPI payments.
 */
@Service("upi")
public class UPIPaymentService implements PaymentService {

    private final SuccessPaymentTransactionRepository successRepository;
    private final FailedPaymentTransactionRepository failedRepository;
    private final TransactionIdGenerator transactionIdGenerator;


    public UPIPaymentService(
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

        if (paymentRequest.getUpi() == null
                || paymentRequest.getUpi().isBlank()) {

            return saveFailure(
                    paymentRequest,
                    transactionId,
                    "UPI ID is required.");
        }

        String message =
                "UPI payment of ₹"
                        + paymentRequest.getAmount()
                        + " to "
                        + paymentRequest.getUpi()
                        + " successful";

        SuccessPaymentTransaction transaction =
                new SuccessPaymentTransaction();

        transaction.setTransactionId(transactionId);
        transaction.setAmount(paymentRequest.getAmount());
        transaction.setMethod(paymentRequest.getMethod());
        transaction.setUpi(paymentRequest.getUpi());
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
        transaction.setUpi(paymentRequest.getUpi());
        transaction.setFailureReason(reason);

        failedRepository.save(transaction);

        return new PaymentResult(
                false,
                transactionId,
                reason);
    }
}