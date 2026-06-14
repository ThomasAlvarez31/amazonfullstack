package com.ecommerce.notifications.service;

import com.ecommerce.notifications.model.Notification;
import com.ecommerce.notifications.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la gestión de notificaciones.
 * Crea notificaciones automáticamente al consumir eventos de RabbitMQ.
 */
@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    /**
     * Retorna todas las notificaciones del sistema.
     *
     * @return lista de notificaciones
     */
    public List<Notification> findAll() {
        return repository.findAll();
    }

    /**
     * Retorna todas las notificaciones de un usuario.
     *
     * @param userId identificador del usuario
     * @return lista de notificaciones del usuario
     */
    public List<Notification> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    /**
     * Busca una notificación por su ID.
     *
     * @param id identificador de la notificación
     * @return Optional con la notificación si existe
     */
    public Optional<Notification> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Persiste una notificación. Invocado también desde {@link com.ecommerce.notifications.messaging.OrderEventConsumer}
     * al recibir eventos de órdenes via RabbitMQ.
     *
     * @param notification datos de la notificación
     * @return notificación guardada con ID generado
     */
    public Notification save(Notification notification) {
        return repository.save(notification);
    }

    /**
     * Elimina una notificación por su ID.
     *
     * @param id identificador de la notificación
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
