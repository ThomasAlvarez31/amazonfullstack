package com.ecommerce.payments.repository;

import com.ecommerce.payments.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
