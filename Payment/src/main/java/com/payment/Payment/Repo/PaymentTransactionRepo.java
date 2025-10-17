package com.payment.Payment.Repo;

import com.payment.Payment.Entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PaymentTransactionRepo extends JpaRepository<PaymentTransaction, String>, JpaSpecificationExecutor<PaymentTransaction> {
}
