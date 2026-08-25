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
class NetBankingPaymentServiceTest {

    @Mock
    private SuccessPaymentTransactionRepository successRepository;

    @Mock
    private FailedPaymentTransactionRepository failedRepository;

    @Mock
    private TransactionIdGenerator transactionIdGenerator;

    @InjectMocks
    private NetBankingPaymentService netBankingPaymentService;

    private PaymentRequest paymentRequest;

    @BeforeEach
    void setUp() {

        paymentRequest = new PaymentRequest();

        paymentRequest.setAmount(3000);
        paymentRequest.setMethod("netbanking");
        paymentRequest.setBank("HDFC Bank");
    }

    @Test
    void shouldProcessNetBankingPaymentSuccessfully() {

        when(transactionIdGenerator.generateTransactionId())
                .thenReturn("TXN-NET123");

        PaymentResult result =
                netBankingPaymentService
                        .makePayment(paymentRequest);

        assertThat(result.success()).isTrue();

        assertThat(result.transactionId())
                .isEqualTo("TXN-NET123");

        assertThat(result.message())
                .contains("Net banking payment");

        verify(successRepository)
                .save(any(SuccessPaymentTransaction.class));

        verify(failedRepository, never())
                .save(any(FailedPaymentTransaction.class));
    }

    @Test
    void shouldFailWhenAmountIsInvalid() {

        paymentRequest.setAmount(0);

        when(transactionIdGenerator.generateTransactionId())
                .thenReturn("TXN-NET123");

        PaymentResult result =
                netBankingPaymentService
                        .makePayment(paymentRequest);

        assertThat(result.success()).isFalse();

        assertThat(result.message())
                .isEqualTo("Invalid amount.");

        verify(failedRepository)
                .save(any(FailedPaymentTransaction.class));
    }

    @Test
    void shouldFailWhenBankIsNull() {

        paymentRequest.setBank(null);

        when(transactionIdGenerator.generateTransactionId())
                .thenReturn("TXN-NET123");

        PaymentResult result =
                netBankingPaymentService
                        .makePayment(paymentRequest);

        assertThat(result.success()).isFalse();

        assertThat(result.message())
                .isEqualTo("Bank selection is required.");

        verify(failedRepository)
                .save(any(FailedPaymentTransaction.class));

        verify(successRepository, never())
                .save(any(SuccessPaymentTransaction.class));
    }

    @Test
    void shouldFailWhenBankIsBlank() {

        paymentRequest.setBank("   ");

        when(transactionIdGenerator.generateTransactionId())
                .thenReturn("TXN-NET123");

        PaymentResult result =
                netBankingPaymentService
                        .makePayment(paymentRequest);

        assertThat(result.success()).isFalse();

        assertThat(result.message())
                .isEqualTo("Bank selection is required.");

        verify(failedRepository)
                .save(any(FailedPaymentTransaction.class));
    }
}