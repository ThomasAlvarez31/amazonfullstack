package com.ecommerce.orders.service;

import com.ecommerce.orders.event.OrderCreatedEvent;
import com.ecommerce.orders.messaging.OrderEventPublisher;
import com.ecommerce.orders.model.Order;
import com.ecommerce.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la gestión de órdenes.
 * Publica eventos a RabbitMQ al crear una orden nueva.
 */
@Service
public class OrderService {

    private final OrderRepository repository;
    private final OrderEventPublisher eventPublisher;

    public OrderService(OrderRepository repository, OrderEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Retorna todas las órdenes registradas.
     *
     * @return lista de órdenes
     */
    public List<Order> findAll() {
        return repository.findAll();
    }

    /**
     * Busca una orden por su ID.
     *
     * @param id identificador de la orden
     * @return Optional con la orden si existe
     */
    public Optional<Order> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Persiste una nueva orden y publica un evento {@link OrderCreatedEvent}
     * en RabbitMQ para notificación asíncrona a ms-notifications.
     *
     * @param order datos de la orden a crear
     * @return orden guardada con ID generado
     */
    public Order save(Order order) {
        Order saved = repository.save(order);
        eventPublisher.publishOrderCreated(new OrderCreatedEvent(
            saved.getId(),
            saved.getUserId(),
            saved.getProductId(),
            saved.getQuantity(),
            saved.getTotalPrice(),
            saved.getStatus()
        ));
        return saved;
    }

    /**
     * Actualiza una orden existente.
     *
     * @param id      identificador de la orden
     * @param updated datos actualizados
     * @return Optional con la orden actualizada, o vacío si no existe
     */
    public Optional<Order> update(Long id, Order updated) {
        return repository.findById(id).map(existing -> {
            existing.setUserId(updated.getUserId());
            existing.setProductId(updated.getProductId());
            existing.setQuantity(updated.getQuantity());
            existing.setStatus(updated.getStatus());
            existing.setTotalPrice(updated.getTotalPrice());
            return repository.save(existing);
        });
    }

    /**
     * Elimina una orden por su ID.
     *
     * @param id identificador de la orden
     * @return true si fue eliminada, false si no existía
     */
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
