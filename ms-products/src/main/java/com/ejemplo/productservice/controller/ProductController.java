package com.ejemplo.productservice.controller;

import com.ejemplo.productservice.model.Product;
import com.ejemplo.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de productos.
 * Respuestas cacheadas en Redis (TTL 10 min) para GET por ID y listado.
 */
@Tag(name = "Products", description = "Gestión del catálogo de productos")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Obtiene todos los productos (respuesta cacheada en Redis).
     *
     * @return lista de productos
     */
    @Operation(summary = "Listar todos los productos")
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    /**
     * Obtiene un producto por su ID (respuesta cacheada en Redis).
     *
     * @param id identificador del producto
     * @return 200 con el producto, o 404 si no existe
     */
    @Operation(summary = "Buscar producto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo producto e invalida el caché de lista.
     *
     * @param product datos del producto (validados con Bean Validation)
     * @return 201 con el producto creado
     */
    @Operation(summary = "Crear nuevo producto")
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(product));
    }

    /**
     * Actualiza un producto existente y actualiza el caché.
     *
     * @param id      identificador del producto
     * @param product datos actualizados
     * @return 200 con el producto actualizado
     */
    @Operation(summary = "Actualizar producto")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        return ResponseEntity.ok(productService.update(id, product));
    }

    /**
     * Elimina un producto por ID y limpia su caché.
     *
     * @param id identificador del producto
     * @return 200 si fue eliminado, 404 si no existe
     */
    @Operation(summary = "Eliminar producto")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        if (!productService.delete(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok("Producto " + id + " eliminado");
    }
}
