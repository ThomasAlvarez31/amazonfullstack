package com.ecommerce.payments.service;

import com.ecommerce.payments.model.Payment;
import com.ecommerce.payments.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la gestión de pagos de órdenes.
 */
@Service
public class PaymentService {

    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    public List<Payment> findAll() {
        return repository.findAll();
    }

    public Optional<Payment> findById(Long id) {
        return repository.findById(id);
    }

    public Payment save(Payment payment) {
        return repository.save(payment);
    }

    public Optional<Payment> update(Long id, Payment updated) {
        return repository.findById(id).map(existing -> {
            existing.setOrderId(updated.getOrderId());
            existing.setAmount(updated.getAmount());
            existing.setStatus(updated.getStatus());
            existing.setPaymentMethod(updated.getPaymentMethod());
            return repository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
