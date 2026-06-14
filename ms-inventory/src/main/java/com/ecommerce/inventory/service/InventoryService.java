package com.ecommerce.inventory.service;

import com.ecommerce.inventory.model.Inventory;
import com.ecommerce.inventory.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la gestión del inventario de productos.
 */
@Service
public class InventoryService {

    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    public List<Inventory> findAll() {
        return repository.findAll();
    }

    public Optional<Inventory> findById(Long id) {
        return repository.findById(id);
    }

    public Inventory save(Inventory inventory) {
        return repository.save(inventory);
    }

    public Optional<Inventory> update(Long id, Inventory updated) {
        return repository.findById(id).map(existing -> {
            existing.setProductId(updated.getProductId());
            existing.setQuantity(updated.getQuantity());
            existing.setLocation(updated.getLocation());
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
