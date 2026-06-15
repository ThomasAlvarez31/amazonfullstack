package com.ecommerce.search.controller;

import com.ecommerce.search.model.SearchResult;
import com.ecommerce.search.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para búsqueda de productos.
 */
@io.swagger.v3.oas.annotations.tags.Tag(name = "Search", description = "Búsqueda de productos")
@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService service;

    public SearchController(SearchService service) {
        this.service = service;
    }

    /**
     * Busca productos por palabra clave. Sin keyword retorna todos los resultados.
     *
     * @param keyword término de búsqueda (opcional)
     * @return lista de productos coincidentes
     */
    @GetMapping
    public List<SearchResult> search(@RequestParam(required = false) String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            return service.search(keyword);
        }
        return service.findAll();
    }

    /**
     * Filtra productos por categoría.
     *
     * @param category nombre de la categoría
     * @return lista de productos de la categoría
     */
    @GetMapping("/category/{category}")
    public List<SearchResult> findByCategory(@PathVariable String category) {
        return service.findByCategory(category);
    }

    /**
     * Indexa un nuevo producto para búsqueda.
     *
     * @param result datos del producto a indexar
     * @return resultado creado con ID generado
     */
    @PostMapping
    public SearchResult save(@RequestBody SearchResult result) {
        return service.save(result);
    }

    /**
     * Elimina un producto del índice de búsqueda.
     *
     * @param id identificador del resultado
     * @return 204 si fue eliminado, o 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
