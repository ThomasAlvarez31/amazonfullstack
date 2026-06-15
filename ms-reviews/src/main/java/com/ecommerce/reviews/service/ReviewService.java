package com.ecommerce.reviews.service;

import com.ecommerce.reviews.model.Review;
import com.ecommerce.reviews.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la gestión de reseñas de productos.
 */
@Service
public class ReviewService {

    private final ReviewRepository repository;

    public ReviewService(ReviewRepository repository) {
        this.repository = repository;
    }

    /**
     * Retorna todas las reseñas registradas.
     *
     * @return lista de reseñas
     */
    public List<Review> findAll() {
        return repository.findAll();
    }

    /**
     * Retorna todas las reseñas de un producto.
     *
     * @param productId identificador del producto
     * @return lista de reseñas del producto
     */
    public List<Review> findByProductId(Long productId) {
        return repository.findByProductId(productId);
    }

    /**
     * Busca una reseña por su ID.
     *
     * @param id identificador de la reseña
     * @return Optional con la reseña si existe
     */
    public Optional<Review> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * Persiste una nueva reseña.
     *
     * @param review datos de la reseña (productId, userId, rating, comment)
     * @return reseña guardada con ID generado
     */
    public Review save(Review review) {
        return repository.save(review);
    }

    /**
     * Actualiza rating y comentario de una reseña existente.
     *
     * @param id      identificador de la reseña
     * @param updated datos actualizados
     * @return Optional con la reseña actualizada, o vacío si no existe
     */
    public Optional<Review> update(Long id, Review updated) {
        return repository.findById(id).map(existing -> {
            existing.setRating(updated.getRating());
            existing.setComment(updated.getComment());
            return repository.save(existing);
        });
    }

    /**
     * Elimina una reseña por su ID.
     *
     * @param id identificador de la reseña
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
