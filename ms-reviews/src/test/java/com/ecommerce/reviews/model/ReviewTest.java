package com.ecommerce.reviews.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {

    @Test
    void settersYGetters_deberianFuncionarCorrectamente() {
        Review review = new Review();
        review.setId(1L);
        review.setProductId(2L);
        review.setUserId(3L);
        review.setRating(5);
        review.setComment("Muy bueno");

        assertEquals(1L, review.getId());
        assertEquals(2L, review.getProductId());
        assertEquals(3L, review.getUserId());
        assertEquals(5, review.getRating());
        assertEquals("Muy bueno", review.getComment());
    }
}
