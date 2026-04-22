package com.ecommerce.wishlist.controller;

import com.ecommerce.wishlist.model.WishlistItem;
import com.ecommerce.wishlist.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService service;

    public WishlistController(WishlistService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public List<WishlistItem> findByUserId(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    @PostMapping
    public WishlistItem save(@RequestBody WishlistItem item) {
        return service.save(item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/user/{userId}/product/{productId}")
    public ResponseEntity<Void> deleteByUserAndProduct(@PathVariable Long userId,
                                                       @PathVariable Long productId) {
        service.deleteByUserAndProduct(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
