package com.ecommerce.search.service;

import com.ecommerce.search.model.SearchResult;
import com.ecommerce.search.repository.SearchResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private final SearchResultRepository repository;

    public SearchService(SearchResultRepository repository) {
        this.repository = repository;
    }

    public List<SearchResult> search(String keyword) {
        return repository.findByNameContainingIgnoreCase(keyword);
    }

    public List<SearchResult> findByCategory(String category) {
        return repository.findByCategory(category);
    }

    public List<SearchResult> findAll() {
        return repository.findAll();
    }

    public SearchResult save(SearchResult result) {
        return repository.save(result);
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
