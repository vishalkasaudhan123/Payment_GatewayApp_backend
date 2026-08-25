package com.interface_example.paymentGateway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.interface_example.paymentGateway.entity.SuccessPaymentTransaction;

/**
 * Repository for successful payment transactions.
 */
@Repository
public interface SuccessPaymentTransactionRepository
        extends JpaRepository<SuccessPaymentTransaction, Long> {

    List<SuccessPaymentTransaction> findByMethod(String method);
}