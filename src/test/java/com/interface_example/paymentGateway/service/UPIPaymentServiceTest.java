package com.interface_example.paymentGateway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.interface_example.paymentGateway.dto.PaymentRequest;
import com.interface_example.paymentGateway.entity.FailedPaymentTransaction;
import com.interface_example.paymentGateway.entity.SuccessPaymentTransaction;
import com.interface_example.paymentGateway.repository.FailedPaymentTransactionRepository;
import com.interface_example.paymentGateway.repository.SuccessPaymentTransactionRepository;

import com.interface_example.paymentGateway.service.PaymentTransactionService.PaymentResult;
import com.interface_example.paymentGateway.util.TransactionIdGenerator;

@ExtendWith(MockitoExtension.class)
class UPIPaymentServiceTest {

    @Mock
    private SuccessPaymentTransactionRepository successRepository;

    @Mock
    private FailedPaymentTransactionRepository failedRepository;

    @Mock
    private TransactionIdGenerator transactionIdGenerator;

    @InjectMocks
    private UPIPaymentService upiPaymentService;

    private PaymentRequest paymentRequest;

    @BeforeEach
    void setUp() {

        paymentRequest = new PaymentRequest();

        paymentRequest.setAmount(1000);
        paymentRequest.setMethod("upi");
        paymentRequest.setUpi("vishal@upi");
    }

    @Test
    void shouldProcessUpiPaymentSuccessfully() {

        // Arrange
        String transactionId = "TXN-123456789ABC";

        when(transactionIdGenerator.generateTransactionId())
                .thenReturn(transactionId);

        // Act
        PaymentResult result =
                upiPaymentService.makePayment(paymentRequest);

        // Assert
        assertThat(result.success()).isTrue();

        assertThat(result.transactionId())
                .isEqualTo(transactionId);

        assertThat(result.message())
                .contains("UPI payment");

        verify(successRepository)
                .save(any(SuccessPaymentTransaction.class));

        verify(failedRepository, never())
                .save(any(FailedPaymentTransaction.class));
    }

    @Test
    void shouldFailWhenAmountIsInvalid() {

        // Arrange
        paymentRequest.setAmount(0);

        when(transactionIdGenerator.generateTransactionId())
                .thenReturn("TXN-123456789ABC");

        // Act
        PaymentResult result =
                upiPaymentService.makePayment(paymentRequest);

        // Assert
        assertThat(result.success()).isFalse();

        assertThat(result.message())
                .isEqualTo("Invalid amount.");

        verify(failedRepository)
                .save(any(FailedPaymentTransaction.class));

        verify(successRepository, never())
                .save(any(SuccessPaymentTransaction.class));
    }

    @Test
    void shouldFailWhenUpiIdIsMissing() {

        // Arrange
        paymentRequest.setUpi(null);

        when(transactionIdGenerator.generateTransactionId())
                .thenReturn("TXN-123456789ABC");

        // Act
        PaymentResult result =
                upiPaymentService.makePayment(paymentRequest);

        // Assert
        assertThat(result.success()).isFalse();

        assertThat(result.message())
                .isEqualTo("UPI ID is required.");

        verify(failedRepository)
                .save(any(FailedPaymentTransaction.class));

        verify(successRepository, never())
                .save(any(SuccessPaymentTransaction.class));
    }

    @Test
    void shouldFailWhenUpiIdIsBlank() {

        // Arrange
        paymentRequest.setUpi("   ");

        when(transactionIdGenerator.generateTransactionId())
                .thenReturn("TXN-123456789ABC");

        // Act
        PaymentResult result =
                upiPaymentService.makePayment(paymentRequest);

        // Assert
        assertThat(result.success()).isFalse();

        assertThat(result.message())
                .isEqualTo("UPI ID is required.");

        verify(failedRepository)
                .save(any(FailedPaymentTransaction.class));

        verify(successRepository, never())
                .save(any(SuccessPaymentTransaction.class));
    }

    @Test
    void shouldReturnFailureWhenPaymentRequestIsNull() {

        // Arrange
        when(transactionIdGenerator.generateTransactionId())
                .thenReturn("TXN-123456789ABC");

        // Act
        PaymentResult result =
                upiPaymentService.makePayment(null);

        // Assert
        assertThat(result.success()).isFalse();

        assertThat(result.transactionId())
                .isEqualTo("TXN-123456789ABC");

        assertThat(result.message())
                .isEqualTo("Payment request is required.");

        verify(successRepository, never())
                .save(any(SuccessPaymentTransaction.class));

        verify(failedRepository, never())
                .save(any(FailedPaymentTransaction.class));
    }
}