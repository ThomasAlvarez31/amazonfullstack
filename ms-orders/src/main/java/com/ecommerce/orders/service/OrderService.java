package com.ecommerce.orders.service;

import com.ecommerce.orders.model.Order;
import com.ecommerce.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public List<Order> findAll() {
        return repository.findAll();
    }

    public Optional<Order> findById(Long id) {
        return repository.findById(id);
    }

    public Order save(Order order) {
        return repository.save(order);
    }

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

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
