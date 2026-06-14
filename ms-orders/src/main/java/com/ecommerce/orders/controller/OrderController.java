package com.ecommerce.orders.controller;

import com.ecommerce.orders.model.Order;
import com.ecommerce.orders.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de órdenes de compra.
 * Al crear una orden se publica un evento asíncrono a RabbitMQ.
 */
@Tag(name = "Orders", description = "Gestión de órdenes de compra")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    /**
     * Obtiene todas las órdenes.
     *
     * @return lista de órdenes registradas
     */
    @Operation(summary = "Listar todas las órdenes")
    @GetMapping
    public List<Order> getAll() {
        return service.findAll();
    }

    /**
     * Obtiene una orden por su ID.
     *
     * @param id identificador de la orden
     * @return 200 con la orden, o 404 si no existe
     */
    @Operation(summary = "Buscar orden por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea una nueva orden. Publica evento {@code order.created} en RabbitMQ.
     *
     * @param order datos de la orden
     * @return orden creada con ID generado
     */
    @Operation(summary = "Crear nueva orden")
    @PostMapping
    public Order create(@RequestBody Order order) {
        return service.save(order);
    }

    /**
     * Actualiza una orden existente.
     *
     * @param id    identificador de la orden
     * @param order datos actualizados
     * @return 200 con la orden actualizada, o 404 si no existe
     */
    @Operation(summary = "Actualizar orden")
    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable Long id, @RequestBody Order order) {
        return service.update(id, order)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina una orden por su ID.
     *
     * @param id identificador de la orden
     * @return 204 si fue eliminada, o 404 si no existe
     */
    @Operation(summary = "Eliminar orden")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
