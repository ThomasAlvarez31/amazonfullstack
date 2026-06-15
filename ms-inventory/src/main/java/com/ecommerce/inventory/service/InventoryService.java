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

    /**
     * Retorna todos los registros de inventario.
     *
     * @return lista de entradas de inventario
     */
    public List<Inventory> findAll() {
        return repository.findAll();
    }

    /**
     * Busca un registro de inventario por su ID.
     *
     * @param id identificador del registro
     * @return Optional con el registro si existe
     */
    public Optional<Inventory> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Persiste un nuevo registro de inventario.
     *
     * @param inventory datos del inventario (productId, quantity, location)
     * @return registro guardado con ID generado
     */
    public Inventory save(Inventory inventory) {
        return repository.save(inventory);
    }

    /**
     * Actualiza un registro de inventario existente.
     *
     * @param id      identificador del registro
     * @param updated datos actualizados
     * @return Optional con el registro actualizado, o vacío si no existe
     */
    public Optional<Inventory> update(Long id, Inventory updated) {
        return repository.findById(id).map(existing -> {
            existing.setProductId(updated.getProductId());
            existing.setQuantity(updated.getQuantity());
            existing.setLocation(updated.getLocation());
            return repository.save(existing);
        });
    }

    /**
     * Elimina un registro de inventario por su ID.
     *
     * @param id identificador del registro
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
