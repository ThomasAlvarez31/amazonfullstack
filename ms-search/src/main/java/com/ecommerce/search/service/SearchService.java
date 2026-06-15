package com.ecommerce.search.service;

import com.ecommerce.search.model.SearchResult;
import com.ecommerce.search.repository.SearchResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de búsqueda de productos por nombre y categoría.
 */
@Service
public class SearchService {

    private final SearchResultRepository repository;

    public SearchService(SearchResultRepository repository) {
        this.repository = repository;
    }

    /**
     * Busca productos cuyo nombre contenga la palabra clave (sin distinguir mayúsculas).
     *
     * @param keyword término de búsqueda
     * @return lista de resultados que coinciden
     */
    public List<SearchResult> search(String keyword) {
        return repository.findByNameContainingIgnoreCase(keyword);
    }

    /**
     * Filtra productos por categoría.
     *
     * @param category nombre de la categoría
     * @return lista de productos de esa categoría
     */
    public List<SearchResult> findByCategory(String category) {
        return repository.findByCategory(category);
    }

    /**
     * Retorna todos los productos indexados.
     *
     * @return lista completa de resultados de búsqueda
     */
    public List<SearchResult> findAll() {
        return repository.findAll();
    }

    /**
     * Indexa un nuevo producto para búsqueda.
     *
     * @param result datos del producto a indexar
     * @return resultado guardado con ID generado
     */
    public SearchResult save(SearchResult result) {
        return repository.save(result);
    }

    /**
     * Elimina un resultado de búsqueda por su ID.
     *
     * @param id identificador del resultado
     * @return true si fue eliminado, false si no existía
     */
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
