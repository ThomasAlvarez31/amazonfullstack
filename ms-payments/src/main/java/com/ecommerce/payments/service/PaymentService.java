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

    /**
     * Retorna todos los pagos registrados.
     *
     * @return lista de pagos
     */
    public List<Payment> findAll() {
        return repository.findAll();
    }

    /**
     * Busca un pago por su ID.
     *
     * @param id identificador del pago
     * @return Optional con el pago si existe
     */
    public Optional<Payment> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Persiste un nuevo pago asociado a una orden.
     *
     * @param payment datos del pago (orderId, amount, status, paymentMethod)
     * @return pago guardado con ID generado
     */
    public Payment save(Payment payment) {
        return repository.save(payment);
    }

    /**
     * Actualiza un pago existente.
     *
     * @param id      identificador del pago
     * @param updated datos actualizados
     * @return Optional con el pago actualizado, o vacío si no existe
     */
    public Optional<Payment> update(Long id, Payment updated) {
        return repository.findById(id).map(existing -> {
            existing.setOrderId(updated.getOrderId());
            existing.setAmount(updated.getAmount());
            existing.setStatus(updated.getStatus());
            existing.setPaymentMethod(updated.getPaymentMethod());
            return repository.save(existing);
        });
    }

    /**
     * Elimina un pago por su ID.
     *
     * @param id identificador del pago
     * @return true si fue eliminado, false si no existía
     */
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
