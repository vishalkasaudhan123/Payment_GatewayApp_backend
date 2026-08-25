package com.interface_example.paymentGateway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.interface_example.paymentGateway.dto.PaymentRequest;
import com.interface_example.paymentGateway.repository.FailedPaymentTransactionRepository;
import com.interface_example.paymentGateway.repository.SuccessPaymentTransactionRepository;

import com.interface_example.paymentGateway.service.PaymentTransactionService.PaymentResult;

@ExtendWith(MockitoExtension.class)
class PaymentTransactionServiceTest {

    @Mock
    private PaymentService upiPaymentService;

    @Mock
    private PaymentService creditPaymentService;

    @Mock
    private PaymentService debitPaymentService;

    @Mock
    private PaymentService netBankingPaymentService;

    @Mock
    private SuccessPaymentTransactionRepository successRepository;

    @Mock
    private FailedPaymentTransactionRepository failedRepository;

    private PaymentTransactionService paymentTransactionService;

    private Map<String, PaymentService> paymentServices;

    @BeforeEach
    void setUp() {

        paymentServices = new HashMap<>();

        paymentServices.put("upi", upiPaymentService);
        paymentServices.put("credit", creditPaymentService);
        paymentServices.put("debit", debitPaymentService);
        paymentServices.put("netbanking", netBankingPaymentService);

        paymentTransactionService =
                new PaymentTransactionService(
                        paymentServices,
                        successRepository,
                        failedRepository);
    }

    @Test
    void shouldSelectUpiPaymentService() {

        PaymentRequest request = new PaymentRequest();

        request.setMethod("upi");
        request.setAmount(1000);
        request.setUpi("vishal@upi");

        PaymentResult expectedResult =
                new PaymentResult(
                        true,
                        "TXN-123",
                        "UPI payment successful");

        when(upiPaymentService.makePayment(request))
                .thenReturn(expectedResult);

        PaymentResult actualResult =
                paymentTransactionService.processPayment(request);

        assertThat(actualResult)
                .isEqualTo(expectedResult);

        verify(upiPaymentService)
                .makePayment(request);
    }

    @Test
    void shouldSelectCreditCardPaymentService() {

        PaymentRequest request = new PaymentRequest();

        request.setMethod("credit");
        request.setAmount(1000);

        PaymentResult expectedResult =
                new PaymentResult(
                        true,
                        "TXN-123",
                        "Credit Card payment successful");

        when(creditPaymentService.makePayment(request))
                .thenReturn(expectedResult);

        PaymentResult actualResult =
                paymentTransactionService.processPayment(request);

        assertThat(actualResult)
                .isEqualTo(expectedResult);

        verify(creditPaymentService)
                .makePayment(request);
    }

    @Test
    void shouldSelectDebitCardPaymentService() {

        PaymentRequest request = new PaymentRequest();

        request.setMethod("debit");
        request.setAmount(1000);

        PaymentResult expectedResult =
                new PaymentResult(
                        true,
                        "TXN-123",
                        "Debit Card payment successful");

        when(debitPaymentService.makePayment(request))
                .thenReturn(expectedResult);

        PaymentResult actualResult =
                paymentTransactionService.processPayment(request);

        assertThat(actualResult)
                .isEqualTo(expectedResult);

        verify(debitPaymentService)
                .makePayment(request);
    }

    @Test
    void shouldSelectNetBankingPaymentService() {

        PaymentRequest request = new PaymentRequest();

        request.setMethod("netbanking");
        request.setAmount(1000);
        request.setBank("HDFC Bank");

        PaymentResult expectedResult =
                new PaymentResult(
                        true,
                        "TXN-123",
                        "Net Banking payment successful");

        when(netBankingPaymentService.makePayment(request))
                .thenReturn(expectedResult);

        PaymentResult actualResult =
                paymentTransactionService.processPayment(request);

        assertThat(actualResult)
                .isEqualTo(expectedResult);

        verify(netBankingPaymentService)
                .makePayment(request);
    }

    @Test
    void shouldReturnFailureWhenPaymentMethodIsInvalid() {

        PaymentRequest request = new PaymentRequest();

        request.setMethod("bitcoin");
        request.setAmount(1000);

        PaymentResult result =
                paymentTransactionService.processPayment(request);

        assertThat(result.success()).isFalse();

        assertThat(result.message())
                .isEqualTo("Payment method not found.");
    }

    @Test
    void shouldReturnFailureWhenPaymentMethodIsMissing() {

        PaymentRequest request = new PaymentRequest();

        request.setAmount(1000);

        PaymentResult result =
                paymentTransactionService.processPayment(request);

        assertThat(result.success()).isFalse();

        assertThat(result.message())
                .isEqualTo("Payment method is required.");
    }
}