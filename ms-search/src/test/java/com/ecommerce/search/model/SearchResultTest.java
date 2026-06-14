package com.ecommerce.search.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SearchResultTest {

    @Test
    void settersYGetters_deberianFuncionarCorrectamente() {
        SearchResult result = new SearchResult();
        result.setId(1L);
        result.setProductId(2L);
        result.setName("Laptop");
        result.setCategory("Electrónica");
        result.setPrice(999.0);

        assertEquals(1L, result.getId());
        assertEquals(2L, result.getProductId());
        assertEquals("Laptop", result.getName());
        assertEquals("Electrónica", result.getCategory());
        assertEquals(999.0, result.getPrice());
    }
}
