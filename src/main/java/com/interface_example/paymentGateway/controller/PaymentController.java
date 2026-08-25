
package com.interface_example.paymentGateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interface_example.paymentGateway.dto.PaymentRequest;
import com.interface_example.paymentGateway.service.PaymentTransactionService;
import com.interface_example.paymentGateway.service.PaymentTransactionService.PaymentResult;

/**
 * REST controller for payment operations.
 *
 * <p>
 * The controller is responsible only for handling HTTP requests
 * and returning HTTP responses. Payment business logic is handled
 * by the service layer.
 * </p>
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentTransactionService paymentTransactionService;

    public PaymentController(
            PaymentTransactionService paymentTransactionService) {
        this.paymentTransactionService = paymentTransactionService;
    }

    /**
     * Processes a payment request.
     *
     * @param paymentRequest payment details received from the client
     * @return payment processing result
     */
    @PostMapping
    public ResponseEntity<PaymentResult> processPayment(
            @RequestBody PaymentRequest paymentRequest) {

        PaymentResult result =
                paymentTransactionService.processPayment(paymentRequest);

        if (result.success()) {
            return ResponseEntity.ok(result);
        }

        return ResponseEntity.badRequest().body(result);
    }
}