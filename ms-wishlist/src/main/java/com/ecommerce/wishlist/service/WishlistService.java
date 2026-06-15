package com.ecommerce.wishlist.service;

import com.ecommerce.wishlist.model.WishlistItem;
import com.ecommerce.wishlist.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de negocio para la gestión de listas de deseos por usuario.
 */
@Service
public class WishlistService {

    private final WishlistRepository repository;

    public WishlistService(WishlistRepository repository) {
        this.repository = repository;
    }

    /**
     * Retorna todos los ítems de la lista de deseos de un usuario.
     *
     * @param userId identificador del usuario
     * @return lista de ítems deseados
     */
    public List<WishlistItem> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    /**
     * Agrega un producto a la lista de deseos de un usuario.
     *
     * @param item ítem con userId y productId
     * @return ítem guardado con ID generado
     */
    public WishlistItem save(WishlistItem item) {
        return repository.save(item);
    }

    /**
     * Elimina un ítem de la lista de deseos por su ID.
     *
     * @param id identificador del ítem
     * @return true si fue eliminado, false si no existía
     */
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Elimina un producto específico de la lista de deseos de un usuario.
     *
     * @param userId    identificador del usuario
     * @param productId identificador del producto
     */
    @Transactional
    public void deleteByUserAndProduct(Long userId, Long productId) {
        repository.deleteByUserIdAndProductId(userId, productId);
    }
}
