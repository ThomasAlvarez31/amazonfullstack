package com.ecommerce.wishlist.service;

import com.ecommerce.wishlist.model.WishlistItem;
import com.ecommerce.wishlist.repository.WishlistRepository;
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
class WishlistServiceTest {

    @Mock
    private WishlistRepository repository;

    @InjectMocks
    private WishlistService service;

    @Test
    void findByUserId_deberiaRetornarItemsDeseadosDelUsuario() {
        WishlistItem item = new WishlistItem();
        item.setUserId(1L);
        item.setProductId(10L);

        when(repository.findByUserId(1L)).thenReturn(List.of(item));

        List<WishlistItem> resultado = service.findByUserId(1L);

        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getProductId());
        verify(repository, times(1)).findByUserId(1L);
    }

    @Test
    void save_deberiaGuardarYRetornarItem() {
        WishlistItem item = new WishlistItem();
        item.setUserId(1L);
        item.setProductId(5L);

        when(repository.save(any(WishlistItem.class))).thenReturn(item);

        WishlistItem resultado = service.save(item);

        assertEquals(5L, resultado.getProductId());
        verify(repository, times(1)).save(item);
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

    @Test
    void deleteByUserAndProduct_deberiaLlamarAlRepositorio() {
        service.deleteByUserAndProduct(1L, 10L);

        verify(repository, times(1)).deleteByUserIdAndProductId(1L, 10L);
    }
}
