package com.ecommerce.notifications.controller;

import com.ecommerce.notifications.model.Notification;
import com.ecommerce.notifications.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la consulta de notificaciones.
 * Las notificaciones se generan automáticamente desde eventos RabbitMQ (ms-orders).
 */
@io.swagger.v3.oas.annotations.tags.Tag(name = "Notifications", description = "Notificaciones de usuarios vía RabbitMQ")
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    /**
     * Retorna todas las notificaciones del sistema.
     *
     * @return lista de notificaciones
     */
    @GetMapping
    public List<Notification> findAll() {
        return service.findAll();
    }

    /**
     * Retorna todas las notificaciones de un usuario específico.
     *
     * @param userId identificador del usuario
     * @return lista de notificaciones del usuario
     */
    @GetMapping("/user/{userId}")
    public List<Notification> findByUserId(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    /**
     * Busca una notificación por su ID.
     *
     * @param id identificador de la notificación
     * @return 200 con la notificación, o 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Notification> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea una notificación manualmente.
     *
     * @param notification datos de la notificación
     * @return notificación creada con ID generado
     */
    @PostMapping
    public Notification save(@RequestBody Notification notification) {
        return service.save(notification);
    }

    /**
     * Elimina una notificación por su ID.
     *
     * @param id identificador de la notificación
     * @return 204 si fue eliminada, o 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
