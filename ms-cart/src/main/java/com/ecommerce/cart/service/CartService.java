package com.ecommerce.cart.service;

import com.ecommerce.cart.model.CartItem;
import com.ecommerce.cart.repository.CartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la gestión del carrito de compras por usuario.
 */
@Service
public class CartService {

    private final CartItemRepository repository;

    public CartService(CartItemRepository repository) {
        this.repository = repository;
    }

    /**
     * Retorna todos los ítems del carrito de un usuario.
     *
     * @param userId identificador del usuario
     * @return lista de ítems en el carrito
     */
    public List<CartItem> getCartByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    /**
     * Agrega un ítem al carrito.
     *
     * @param item ítem a agregar con userId, productId y cantidad
     * @return ítem guardado con ID generado
     */
    public CartItem addItem(CartItem item) {
        return repository.save(item);
    }

    /**
     * Actualiza la cantidad de un ítem existente en el carrito.
     *
     * @param itemId   identificador del ítem
     * @param quantity nueva cantidad
     * @return Optional con el ítem actualizado, o vacío si no existe
     */
    public Optional<CartItem> updateQuantity(Long itemId, Integer quantity) {
        return repository.findById(itemId).map(existing -> {
            existing.setQuantity(quantity);
            return repository.save(existing);
        });
    }

    /**
     * Elimina un ítem del carrito por su ID.
     *
     * @param itemId identificador del ítem
     * @return true si fue eliminado, false si no existía
     */
    public boolean removeItem(Long itemId) {
        if (repository.existsById(itemId)) {
            repository.deleteById(itemId);
            return true;
        }
        return false;
    }

    /**
     * Vacía completamente el carrito de un usuario.
     *
     * @param userId identificador del usuario
     */
    @Transactional
    public void clearCart(Long userId) {
        repository.deleteByUserId(userId);
    }
}
