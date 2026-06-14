package com.ecommerce.wishlist.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WishlistItemTest {

    @Test
    void settersYGetters_deberianFuncionarCorrectamente() {
        WishlistItem item = new WishlistItem();
        item.setId(1L);
        item.setUserId(2L);
        item.setProductId(3L);

        assertEquals(1L, item.getId());
        assertEquals(2L, item.getUserId());
        assertEquals(3L, item.getProductId());
    }
}
