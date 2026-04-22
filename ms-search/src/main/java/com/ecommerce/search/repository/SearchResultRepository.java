package com.ecommerce.search.repository;

import com.ecommerce.search.model.SearchResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchResultRepository extends JpaRepository<SearchResult, Long> {
    List<SearchResult> findByNameContainingIgnoreCase(String keyword);
    List<SearchResult> findByCategory(String category);
}
