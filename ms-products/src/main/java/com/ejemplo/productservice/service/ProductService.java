package com.ejemplo.productservice.service;

import com.ejemplo.productservice.model.Product;
import com.ejemplo.productservice.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la gestión de productos.
 * Implementa caché con Redis: TTL 10 min (cache "products" y "productsList").
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Retorna todos los productos. Resultado cacheado en "productsList" (TTL 10 min).
     *
     * @return lista de todos los productos
     */
    @Cacheable("productsList")
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * Busca un producto por ID. Resultado cacheado en "products" con key={@code id}.
     *
     * @param id identificador del producto
     * @return Optional con el producto si existe
     */
    @Cacheable(value = "products", key = "#id")
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Crea un nuevo producto e invalida el caché de lista.
     *
     * @param product datos del producto a crear
     * @return producto guardado con ID generado
     */
    @CacheEvict(value = "productsList", allEntries = true)
    public Product save(Product product) {
        return productRepository.save(product);
    }

    /**
     * Actualiza un producto existente. Actualiza el caché individual e invalida la lista.
     *
     * @param id      identificador del producto
     * @param product datos actualizados
     * @return producto actualizado
     */
    @Caching(
        put = @CachePut(value = "products", key = "#id"),
        evict = @CacheEvict(value = "productsList", allEntries = true)
    )
    public Product update(Long id, Product product) {
        product.setId(id);
        return productRepository.save(product);
    }

    /**
     * Elimina un producto por ID y limpia sus entradas de caché.
     *
     * @param id identificador del producto
     * @return true si fue eliminado, false si no existía
     */
    @Caching(evict = {
        @CacheEvict(value = "products", key = "#id"),
        @CacheEvict(value = "productsList", allEntries = true)
    })
    public boolean delete(Long id) {
        if (!productRepository.existsById(id)) return false;
        productRepository.deleteById(id);
        return true;
    }
}
