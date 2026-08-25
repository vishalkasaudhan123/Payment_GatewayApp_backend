package com.interface_example.paymentGateway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

import com.interface_example.paymentGateway.dto.PaymentRequest;
import com.interface_example.paymentGateway.repository.FailedPaymentTransactionRepository;
import com.interface_example.paymentGateway.repository.SuccessPaymentTransactionRepository;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SuccessPaymentTransactionRepository successRepository;

    @Autowired
    private FailedPaymentTransactionRepository failedRepository;

    @BeforeEach
    void setUp() {
        successRepository.deleteAll();
        failedRepository.deleteAll();
    }

    @Test
    void shouldProcessUpiPaymentAndPersistTransaction() throws Exception {

        PaymentRequest request = new PaymentRequest();
        request.setAmount(300);
        request.setMethod("upi");
        request.setUpi("test@upi");

        mockMvc.perform(
                post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.transactionId").isString());

        assertThat(successRepository.findByMethod("upi"))
                .hasSize(1);

        assertThat(failedRepository.findByMethod("upi"))
                .isEmpty();
    }

    @Test
    void shouldRejectInvalidAmountAndPersistFailedTransaction() throws Exception {

        PaymentRequest request = new PaymentRequest();
        request.setAmount(-100);
        request.setMethod("upi");
        request.setUpi("test@upi");

        mockMvc.perform(
                post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid amount."));

        assertThat(failedRepository.findByMethod("upi"))
                .hasSize(1);

        assertThat(successRepository.findByMethod("upi"))
                .isEmpty();
    }

    @Test
    void shouldRejectCreditCardPaymentWhenCardNumberIsMissing() throws Exception {

        PaymentRequest request = new PaymentRequest();
        request.setAmount(1000);
        request.setMethod("credit");

        mockMvc.perform(
                post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(
                        jsonPath("$.message")
                                .value("Valid card number is required."));

        assertThat(failedRepository.findByMethod("credit"))
                .hasSize(1);

        assertThat(successRepository.findByMethod("credit"))
                .isEmpty();
    }

    @Test
    void shouldProcessDebitCardPaymentAndPersistTransaction() throws Exception {

        PaymentRequest request = new PaymentRequest();
        request.setAmount(100);
        request.setMethod("debit");
        request.setCard("1234567890121234");
        request.setCvv("123");

        mockMvc.perform(
                post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.transactionId").isString());

        assertThat(successRepository.findByMethod("debit"))
                .hasSize(1);

        assertThat(failedRepository.findByMethod("debit"))
                .isEmpty();
    }

    @Test
    void shouldProcessNetBankingPaymentAndPersistTransaction() throws Exception {

        PaymentRequest request = new PaymentRequest();
        request.setAmount(500);
        request.setMethod("netbanking");
        request.setBank("HDFC Bank");

        mockMvc.perform(
                post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.transactionId").isString());

        assertThat(successRepository.findByMethod("netbanking"))
                .hasSize(1);

        assertThat(failedRepository.findByMethod("netbanking"))
                .isEmpty();
    }
}