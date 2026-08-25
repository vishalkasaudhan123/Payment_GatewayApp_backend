
package com.interface_example.paymentGateway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.interface_example.paymentGateway.entity.FailedPaymentTransaction;

/**
 * Repository for failed payment transactions.
 */
@Repository
public interface FailedPaymentTransactionRepository
        extends JpaRepository<FailedPaymentTransaction, Long> {

    List<FailedPaymentTransaction> findByMethod(String method);
}