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

    public List<Review> findAll() {
        return repository.findAll();
    }

    public List<Review> findByProductId(Long productId) {
        return repository.findByProductId(productId);
    }

    public Optional<Review> findById(Long id) {
        return repository.findById(id);
    }

    public Review save(Review review) {
        return repository.save(review);
    }

    public Optional<Review> update(Long id, Review updated) {
        return repository.findById(id).map(existing -> {
            existing.setRating(updated.getRating());
            existing.setComment(updated.getComment());
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
