package com.ecommerce.cart.service;

import com.ecommerce.cart.model.CartItem;
import com.ecommerce.cart.repository.CartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartItemRepository repository;

    public CartService(CartItemRepository repository) {
        this.repository = repository;
    }

    public List<CartItem> getCartByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    public CartItem addItem(CartItem item) {
        return repository.save(item);
    }

    public Optional<CartItem> updateQuantity(Long itemId, Integer quantity) {
        return repository.findById(itemId).map(existing -> {
            existing.setQuantity(quantity);
            return repository.save(existing);
        });
    }

    public boolean removeItem(Long itemId) {
        if (repository.existsById(itemId)) {
            repository.deleteById(itemId);
            return true;
        }
        return false;
    }

    @Transactional
    public void clearCart(Long userId) {
        repository.deleteByUserId(userId);
    }
}
