package com.ecommerce.shipping.controller;

import com.ecommerce.shipping.model.Shipment;
import com.ecommerce.shipping.service.ShipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de envíos.
 */
@io.swagger.v3.oas.annotations.tags.Tag(name = "Shipping", description = "Gestión de envíos")
@RestController
@RequestMapping("/api/shipping")
public class ShipmentController {

    private final ShipmentService service;

    public ShipmentController(ShipmentService service) {
        this.service = service;
    }

    /**
     * Retorna todos los envíos registrados.
     *
     * @return lista de envíos
     */
    @GetMapping
    public List<Shipment> findAll() {
        return service.findAll();
    }

    /**
     * Busca un envío por su ID.
     *
     * @param id identificador del envío
     * @return 200 con el envío, o 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Shipment> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retorna todos los envíos de una orden.
     *
     * @param orderId identificador de la orden
     * @return lista de envíos asociados a la orden
     */
    @GetMapping("/order/{orderId}")
    public List<Shipment> findByOrderId(@PathVariable Long orderId) {
        return service.findByOrderId(orderId);
    }

    /**
     * Registra un nuevo envío.
     *
     * @param shipment datos del envío
     * @return envío creado con ID generado
     */
    @PostMapping
    public Shipment save(@RequestBody Shipment shipment) {
        return service.save(shipment);
    }

    /**
     * Actualiza un envío existente.
     *
     * @param id       identificador del envío
     * @param shipment datos actualizados
     * @return 200 con envío actualizado, o 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Shipment> update(@PathVariable Long id, @RequestBody Shipment shipment) {
        return service.update(id, shipment)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un envío por su ID.
     *
     * @param id identificador del envío
     * @return 204 si fue eliminado, o 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
