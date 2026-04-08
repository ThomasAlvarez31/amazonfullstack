package com.ecommerce.cart.controller;

import com.ecommerce.cart.model.CartItem;
import com.ecommerce.cart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public List<CartItem> getCart(@PathVariable Long userId) {
        return service.getCartByUser(userId);
    }

    @PostMapping
    public CartItem addItem(@RequestBody CartItem item) {
        return service.addItem(item);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<CartItem> updateQuantity(@PathVariable Long itemId,
                                                   @RequestParam Integer quantity) {
        return service.updateQuantity(itemId, quantity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId) {
        if (service.removeItem(itemId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        service.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
