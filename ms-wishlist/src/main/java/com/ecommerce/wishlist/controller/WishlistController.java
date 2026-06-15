package com.ecommerce.wishlist.controller;

import com.ecommerce.wishlist.model.WishlistItem;
import com.ecommerce.wishlist.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de listas de deseos.
 */
@io.swagger.v3.oas.annotations.tags.Tag(name = "Wishlist", description = "Lista de deseos por usuario")
@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService service;

    public WishlistController(WishlistService service) {
        this.service = service;
    }

    /**
     * Retorna la lista de deseos de un usuario.
     *
     * @param userId identificador del usuario
     * @return lista de ítems deseados
     */
    @GetMapping("/{userId}")
    public List<WishlistItem> findByUserId(@PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    /**
     * Agrega un producto a la lista de deseos.
     *
     * @param item ítem con userId y productId
     * @return ítem creado con ID generado
     */
    @PostMapping
    public WishlistItem save(@RequestBody WishlistItem item) {
        return service.save(item);
    }

    /**
     * Elimina un ítem de la lista de deseos por su ID.
     *
     * @param id identificador del ítem
     * @return 204 si fue eliminado, o 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }

    /**
     * Elimina un producto específico de la lista de deseos de un usuario.
     *
     * @param userId    identificador del usuario
     * @param productId identificador del producto
     * @return 204 sin contenido
     */
    @DeleteMapping("/user/{userId}/product/{productId}")
    public ResponseEntity<Void> deleteByUserAndProduct(@PathVariable Long userId,
                                                       @PathVariable Long productId) {
        service.deleteByUserAndProduct(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
