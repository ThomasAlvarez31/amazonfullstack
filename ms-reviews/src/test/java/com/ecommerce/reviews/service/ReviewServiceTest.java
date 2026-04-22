package com.ecommerce.reviews.service;

import com.ecommerce.reviews.model.Review;
import com.ecommerce.reviews.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository repository;

    @InjectMocks
    private ReviewService service;

    @Test
    void findByProductId_deberiaRetornarOpinionesDelProducto() {
        Review review = new Review();
        review.setProductId(1L);
        review.setUserId(2L);
        review.setRating(5);
        review.setComment("Excelente producto");

        when(repository.findByProductId(1L)).thenReturn(List.of(review));

        List<Review> resultado = service.findByProductId(1L);

        assertEquals(1, resultado.size());
        assertEquals(5, resultado.get(0).getRating());
        verify(repository, times(1)).findByProductId(1L);
    }

    @Test
    void save_deberiaGuardarYRetornarOpinion() {
        Review review = new Review();
        review.setProductId(1L);
        review.setUserId(3L);
        review.setRating(4);
        review.setComment("Muy bueno");

        when(repository.save(any(Review.class))).thenReturn(review);

        Review resultado = service.save(review);

        assertEquals(4, resultado.getRating());
        assertEquals("Muy bueno", resultado.getComment());
        verify(repository, times(1)).save(review);
    }

    @Test
    void update_deberiaActualizarCuandoExiste() {
        Review existing = new Review();
        existing.setRating(3);
        existing.setComment("Regular");

        Review updated = new Review();
        updated.setRating(5);
        updated.setComment("Cambié de opinión, es genial");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Review.class))).thenReturn(existing);

        Optional<Review> resultado = service.update(1L, updated);

        assertTrue(resultado.isPresent());
        assertEquals(5, resultado.get().getRating());
    }

    @Test
    void delete_deberiaEliminarYRetornarTrueCuandoExiste() {
        when(repository.existsById(1L)).thenReturn(true);

        boolean resultado = service.delete(1L);

        assertTrue(resultado);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void delete_deberiaRetornarFalseCuandoNoExiste() {
        when(repository.existsById(99L)).thenReturn(false);

        boolean resultado = service.delete(99L);

        assertFalse(resultado);
        verify(repository, never()).deleteById(any());
    }
}
