package com.ecommerce.reviews.controller;

import com.ecommerce.reviews.model.Review;
import com.ecommerce.reviews.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de reseñas de productos.
 */
@io.swagger.v3.oas.annotations.tags.Tag(name = "Reviews", description = "Reseñas de productos")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    /**
     * Retorna todas las reseñas.
     *
     * @return lista de reseñas
     */
    @GetMapping
    public List<Review> findAll() {
        return service.findAll();
    }

    /**
     * Retorna reseñas de un producto específico.
     *
     * @param productId identificador del producto
     * @return lista de reseñas del producto
     */
    @GetMapping("/product/{productId}")
    public List<Review> findByProductId(@PathVariable Long productId) {
        return service.findByProductId(productId);
    }

    /**
     * Busca una reseña por su ID.
     *
     * @param id identificador de la reseña
     * @return 200 con la reseña, o 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Review> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea una nueva reseña.
     *
     * @param review datos de la reseña
     * @return reseña creada con ID generado
     */
    @PostMapping
    public Review save(@RequestBody Review review) {
        return service.save(review);
    }

    /**
     * Actualiza una reseña existente.
     *
     * @param id     identificador de la reseña
     * @param review datos actualizados
     * @return 200 con reseña actualizada, o 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Review> update(@PathVariable Long id, @RequestBody Review review) {
        return service.update(id, review)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina una reseña por su ID.
     *
     * @param id identificador de la reseña
     * @return 204 si fue eliminada, o 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
