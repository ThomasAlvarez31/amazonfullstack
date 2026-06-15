package com.ecommerce.cart.controller;

import com.ecommerce.cart.model.CartItem;
import com.ecommerce.cart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión del carrito de compras.
 */
@io.swagger.v3.oas.annotations.tags.Tag(name = "Cart", description = "Carrito de compras por usuario")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    /**
     * Retorna todos los ítems del carrito de un usuario.
     *
     * @param userId identificador del usuario
     * @return lista de ítems en el carrito
     */
    @GetMapping("/{userId}")
    public List<CartItem> getCart(@PathVariable Long userId) {
        return service.getCartByUser(userId);
    }

    /**
     * Agrega un ítem al carrito.
     *
     * @param item datos del ítem (userId, productId, cantidad)
     * @return ítem creado con ID generado
     */
    @PostMapping
    public CartItem addItem(@RequestBody CartItem item) {
        return service.addItem(item);
    }

    /**
     * Actualiza la cantidad de un ítem en el carrito.
     *
     * @param itemId   identificador del ítem
     * @param quantity nueva cantidad
     * @return 200 con ítem actualizado, o 404 si no existe
     */
    @PutMapping("/{itemId}")
    public ResponseEntity<CartItem> updateQuantity(@PathVariable Long itemId,
                                                   @RequestParam Integer quantity) {
        return service.updateQuantity(itemId, quantity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un ítem del carrito.
     *
     * @param itemId identificador del ítem
     * @return 204 si fue eliminado, o 404 si no existe
     */
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId) {
        if (service.removeItem(itemId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Vacía el carrito completo de un usuario.
     *
     * @param userId identificador del usuario
     * @return 204 sin contenido
     */
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        service.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
