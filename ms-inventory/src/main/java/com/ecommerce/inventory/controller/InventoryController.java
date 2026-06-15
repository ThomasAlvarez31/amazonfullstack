package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.model.Inventory;
import com.ecommerce.inventory.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión del inventario de productos.
 */
@io.swagger.v3.oas.annotations.tags.Tag(name = "Inventory", description = "Gestión del inventario")
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    /**
     * Retorna todos los registros de inventario.
     *
     * @return lista de entradas de inventario
     */
    @GetMapping
    public List<Inventory> getAll() {
        return service.findAll();
    }

    /**
     * Busca un registro de inventario por su ID.
     *
     * @param id identificador del registro
     * @return 200 con el registro, o 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo registro de inventario.
     *
     * @param inventory datos del inventario
     * @return registro creado con ID generado
     */
    @PostMapping
    public Inventory create(@RequestBody Inventory inventory) {
        return service.save(inventory);
    }

    /**
     * Actualiza un registro de inventario existente.
     *
     * @param id        identificador del registro
     * @param inventory datos actualizados
     * @return 200 con registro actualizado, o 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Inventory> update(@PathVariable Long id, @RequestBody Inventory inventory) {
        return service.update(id, inventory)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un registro de inventario.
     *
     * @param id identificador del registro
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
