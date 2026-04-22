package com.ecommerce.search.service;

import com.ecommerce.search.model.SearchResult;
import com.ecommerce.search.repository.SearchResultRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private SearchResultRepository repository;

    @InjectMocks
    private SearchService service;

    @Test
    void search_deberiaRetornarResultadosPorKeyword() {
        SearchResult result = new SearchResult();
        result.setProductId(1L);
        result.setName("Laptop Gamer Pro");
        result.setCategory("Electronica");
        result.setPrice(1200.0);

        when(repository.findByNameContainingIgnoreCase("Laptop")).thenReturn(List.of(result));

        List<SearchResult> resultados = service.search("Laptop");

        assertEquals(1, resultados.size());
        assertEquals("Laptop Gamer Pro", resultados.get(0).getName());
        verify(repository, times(1)).findByNameContainingIgnoreCase("Laptop");
    }

    @Test
    void findByCategory_deberiaRetornarProductosDeLaCategoria() {
        SearchResult result = new SearchResult();
        result.setCategory("Electronica");
        result.setName("Mouse Gamer");

        when(repository.findByCategory("Electronica")).thenReturn(List.of(result));

        List<SearchResult> resultados = service.findByCategory("Electronica");

        assertEquals(1, resultados.size());
        assertEquals("Electronica", resultados.get(0).getCategory());
    }

    @Test
    void save_deberiaGuardarResultado() {
        SearchResult result = new SearchResult();
        result.setProductId(2L);
        result.setName("Teclado Mecánico");
        result.setPrice(85.0);

        when(repository.save(any(SearchResult.class))).thenReturn(result);

        SearchResult guardado = service.save(result);

        assertEquals("Teclado Mecánico", guardado.getName());
        verify(repository, times(1)).save(result);
    }

    @Test
    void delete_deberiaEliminarYRetornarTrueCuandoExiste() {
        when(repository.existsById(1L)).thenReturn(true);

        boolean resultado = service.delete(1L);

        assertTrue(resultado);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void delete_deberiaRetornarFalseCuandoNoExiste() {
        when(repository.existsById(99L)).thenReturn(false);

        boolean resultado = service.delete(99L);

        assertFalse(resultado);
        verify(repository, never()).deleteById(any());
    }
}
