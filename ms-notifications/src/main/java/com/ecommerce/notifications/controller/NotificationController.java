package com.ecommerce.notifications.controller;

import com.ecommerce.notifications.kafka.NotificationProducer;
import com.ecommerce.notifications.model.Notification;
import com.ecommerce.notifications.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;
    private final NotificationProducer producer;

    public NotificationController(NotificationService service, NotificationProducer producer) {
        this.service = service;
        this.producer = producer;
    }

    @GetMapping
    public List<Notification> findAll() {
        return service.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<Notification> findByUserId(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Notification save(@RequestBody Notification notification) {
        return service.save(notification);
    }

    @PostMapping("/send")
    public ResponseEntity<String> send(@RequestParam String message) {
        producer.send(message);
        return ResponseEntity.ok("Mensaje enviado al topic: " + message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
