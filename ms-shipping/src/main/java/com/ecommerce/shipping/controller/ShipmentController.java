package com.ecommerce.shipping.controller;

import com.ecommerce.shipping.model.Shipment;
import com.ecommerce.shipping.service.ShipmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipping")
public class ShipmentController {

    private final ShipmentService service;

    public ShipmentController(ShipmentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Shipment> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shipment> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/order/{orderId}")
    public List<Shipment> findByOrderId(@PathVariable Long orderId) {
        return service.findByOrderId(orderId);
    }

    @PostMapping
    public Shipment save(@RequestBody Shipment shipment) {
        return service.save(shipment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Shipment> update(@PathVariable Long id, @RequestBody Shipment shipment) {
        return service.update(id, shipment)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
