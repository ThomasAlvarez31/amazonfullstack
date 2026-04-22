package com.ecommerce.shipping.service;

import com.ecommerce.shipping.model.Shipment;
import com.ecommerce.shipping.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShipmentService {

    private final ShipmentRepository repository;

    public ShipmentService(ShipmentRepository repository) {
        this.repository = repository;
    }

    public List<Shipment> findAll() {
        return repository.findAll();
    }

    public Optional<Shipment> findById(Long id) {
        return repository.findById(id);
    }

    public List<Shipment> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }

    public Shipment save(Shipment shipment) {
        return repository.save(shipment);
    }

    public Optional<Shipment> update(Long id, Shipment updated) {
        return repository.findById(id).map(existing -> {
            existing.setAddress(updated.getAddress());
            existing.setStatus(updated.getStatus());
            existing.setCarrier(updated.getCarrier());
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
