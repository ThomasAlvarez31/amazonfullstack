package com.ecommerce.search.controller;

import com.ecommerce.search.model.SearchResult;
import com.ecommerce.search.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService service;

    public SearchController(SearchService service) {
        this.service = service;
    }

    @GetMapping
    public List<SearchResult> search(@RequestParam(required = false) String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            return service.search(keyword);
        }
        return service.findAll();
    }

    @GetMapping("/category/{category}")
    public List<SearchResult> findByCategory(@PathVariable String category) {
        return service.findByCategory(category);
    }

    @PostMapping
    public SearchResult save(@RequestBody SearchResult result) {
        return service.save(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
