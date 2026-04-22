package com.ecommerce.wishlist.service;

import com.ecommerce.wishlist.model.WishlistItem;
import com.ecommerce.wishlist.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepository repository;

    public WishlistService(WishlistRepository repository) {
        this.repository = repository;
    }

    public List<WishlistItem> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    public WishlistItem save(WishlistItem item) {
        return repository.save(item);
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public void deleteByUserAndProduct(Long userId, Long productId) {
        repository.deleteByUserIdAndProductId(userId, productId);
    }
}
