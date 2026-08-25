package com.interface_example.paymentGateway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
class CreditCardPaymentServiceTest {

    @Mock
    private SuccessPaymentTransactionRepository successRepository;

    @Mock
    private FailedPaymentTransactionRepository failedRepository;

    @Mock
    private TransactionIdGenerator transactionIdGenerator;

    @InjectMocks
    private CreditCardPaymentService creditCardPaymentService;

    private PaymentRequest paymentRequest;

    @BeforeEach
    void setUp() {

        paymentRequest = new PaymentRequest();

        paymentRequest.setAmount(1500);
        paymentRequest.setMethod("credit");
        paymentRequest.setCard("1234567812345678");
        paymentRequest.setCvv("123");
    }

    @Test
    void shouldProcessCreditCardPaymentSuccessfully() {

        when(transactionIdGenerator.generateTransactionId())
                .thenReturn("TXN-CREDIT123");

        PaymentResult result =
                creditCardPaymentService.makePayment(paymentRequest);

        assertThat(result.success()).isTrue();

        assertThat(result.transactionId())
                .isEqualTo("TXN-CREDIT123");

        assertThat(result.message())
                .contains("Credit card payment");

        verify(successRepository)
                .save(any(SuccessPaymentTransaction.class));

        verify(failedRepository, never())
                .save(any(FailedPaymentTransaction.class));
    }

    @Test
    void shouldFailWhenAmountIsInvalid() {

        paymentRequest.setAmount(0);

        when(transactionIdGenerator.generateTransactionId())
                .thenReturn("TXN-CREDIT123");

        PaymentResult result =
                creditCardPaymentService.makePayment(paymentRequest);

        assertThat(result.success()).isFalse();

        assertThat(result.message())
                .isEqualTo("Invalid amount.");

        verify(failedRepository)
                .save(any(FailedPaymentTransaction.class));

        verify(successRepository, never())
                .save(any(SuccessPaymentTransaction.class));
    }

    @Test
    void shouldFailWhenCardNumberIsNull() {

        paymentRequest.setCard(null);

        when(transactionIdGenerator.generateTransactionId())
                .thenReturn("TXN-CREDIT123");

        PaymentResult result =
                creditCardPaymentService.makePayment(paymentRequest);

        assertThat(result.success()).isFalse();

        assertThat(result.message())
                .isEqualTo("Valid card number is required.");

        verify(failedRepository)
                .save(any(FailedPaymentTransaction.class));
    }

    @Test
    void shouldFailWhenCardNumberIsLessThanFourCharacters() {

        paymentRequest.setCard("123");

        when(transactionIdGenerator.generateTransactionId())
                .thenReturn("TXN-CREDIT123");

        PaymentResult result =
                creditCardPaymentService.makePayment(paymentRequest);

        assertThat(result.success()).isFalse();

        assertThat(result.message())
                .isEqualTo("Valid card number is required.");

        verify(failedRepository)
                .save(any(FailedPaymentTransaction.class));
    }

    @Test
    void shouldFailWhenCvvIsMissing() {

        paymentRequest.setCvv(null);

        when(transactionIdGenerator.generateTransactionId())
                .thenReturn("TXN-CREDIT123");

        PaymentResult result =
                creditCardPaymentService.makePayment(paymentRequest);

        assertThat(result.success()).isFalse();

        assertThat(result.message())
                .isEqualTo("CVV is required.");

        verify(failedRepository)
                .save(any(FailedPaymentTransaction.class));
    }

    @Test
    void shouldFailWhenCvvIsBlank() {

        paymentRequest.setCvv("   ");

        when(transactionIdGenerator.generateTransactionId())
                .thenReturn("TXN-CREDIT123");

        PaymentResult result =
                creditCardPaymentService.makePayment(paymentRequest);

        assertThat(result.success()).isFalse();

        assertThat(result.message())
                .isEqualTo("CVV is required.");

        verify(failedRepository)
                .save(any(FailedPaymentTransaction.class));
    }

    @Test
    void shouldSaveMaskedCardForSuccessfulPayment() {

        when(transactionIdGenerator.generateTransactionId())
                .thenReturn("TXN-CREDIT123");

        creditCardPaymentService.makePayment(paymentRequest);

        ArgumentCaptor<SuccessPaymentTransaction> captor =
                ArgumentCaptor.forClass(
                        SuccessPaymentTransaction.class);

        verify(successRepository).save(captor.capture());

        SuccessPaymentTransaction transaction =
                captor.getValue();

        assertThat(transaction.getMaskedCard())
                .isEqualTo("**** **** **** 5678");

        assertThat(transaction.getMethod())
                .isEqualTo("credit");
    }
}