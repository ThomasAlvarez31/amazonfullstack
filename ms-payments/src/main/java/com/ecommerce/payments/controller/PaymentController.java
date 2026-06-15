package com.ecommerce.payments.controller;

import com.ecommerce.payments.model.Payment;
import com.ecommerce.payments.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de pagos.
 */
@io.swagger.v3.oas.annotations.tags.Tag(name = "Payments", description = "Gestión de pagos de órdenes")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    /**
     * Retorna todos los pagos registrados.
     *
     * @return lista de pagos
     */
    @GetMapping
    public List<Payment> getAll() {
        return service.findAll();
    }

    /**
     * Busca un pago por su ID.
     *
     * @param id identificador del pago
     * @return 200 con el pago, o 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Registra un nuevo pago.
     *
     * @param payment datos del pago
     * @return pago creado con ID generado
     */
    @PostMapping
    public Payment create(@RequestBody Payment payment) {
        return service.save(payment);
    }

    /**
     * Actualiza un pago existente.
     *
     * @param id      identificador del pago
     * @param payment datos actualizados
     * @return 200 con pago actualizado, o 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Payment> update(@PathVariable Long id, @RequestBody Payment payment) {
        return service.update(id, payment)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un pago por su ID.
     *
     * @param id identificador del pago
     * @return 204 si fue eliminado, o 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
