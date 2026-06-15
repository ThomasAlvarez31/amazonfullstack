package com.ecommerce.shipping.service;

import com.ecommerce.shipping.model.Shipment;
import com.ecommerce.shipping.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la gestión de envíos asociados a órdenes.
 */
@Service
public class ShipmentService {

    private final ShipmentRepository repository;

    public ShipmentService(ShipmentRepository repository) {
        this.repository = repository;
    }

    /**
     * Retorna todos los envíos registrados.
     *
     * @return lista de envíos
     */
    public List<Shipment> findAll() {
        return repository.findAll();
    }

    /**
     * Busca un envío por su ID.
     *
     * @param id identificador del envío
     * @return Optional con el envío si existe
     */
    public Optional<Shipment> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Retorna todos los envíos asociados a una orden.
     *
     * @param orderId identificador de la orden
     * @return lista de envíos de la orden
     */
    public List<Shipment> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }

    /**
     * Registra un nuevo envío.
     *
     * @param shipment datos del envío (orderId, address, status, carrier)
     * @return envío guardado con ID generado
     */
    public Shipment save(Shipment shipment) {
        return repository.save(shipment);
    }

    /**
     * Actualiza dirección, estado y transportista de un envío.
     *
     * @param id      identificador del envío
     * @param updated datos actualizados
     * @return Optional con el envío actualizado, o vacío si no existe
     */
    public Optional<Shipment> update(Long id, Shipment updated) {
        return repository.findById(id).map(existing -> {
            existing.setAddress(updated.getAddress());
            existing.setStatus(updated.getStatus());
            existing.setCarrier(updated.getCarrier());
            return repository.save(existing);
        });
    }

    /**
     * Elimina un envío por su ID.
     *
     * @param id identificador del envío
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
