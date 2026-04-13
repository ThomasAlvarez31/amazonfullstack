package com.ecommerce.cart.service;

import com.ecommerce.cart.model.CartItem;
import com.ecommerce.cart.repository.CartItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartItemRepository repository;

    @InjectMocks
    private CartService service;

    @Test
    void getCartByUser_deberiaRetornarItemsDelUsuario() {
        CartItem item = new CartItem();
        item.setUserId(1L);
        item.setProductId(10L);
        item.setQuantity(2);

        when(repository.findByUserId(1L)).thenReturn(List.of(item));

        List<CartItem> resultado = service.getCartByUser(1L);

        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getProductId());
        verify(repository, times(1)).findByUserId(1L);
    }

    @Test
    void addItem_deberiaGuardarYRetornarItem() {
        CartItem item = new CartItem();
        item.setUserId(1L);
        item.setProductId(10L);
        item.setQuantity(3);
        item.setUnitPrice(99.99);

        when(repository.save(any(CartItem.class))).thenReturn(item);

        CartItem resultado = service.addItem(item);

        assertEquals(3, resultado.getQuantity());
        assertEquals(99.99, resultado.getUnitPrice());
        verify(repository, times(1)).save(item);
    }

    @Test
    void updateQuantity_deberiaActualizarCantidadCuandoItemExiste() {
        CartItem item = new CartItem();
        item.setUserId(1L);
        item.setQuantity(1);

        when(repository.findById(1L)).thenReturn(Optional.of(item));
        when(repository.save(any(CartItem.class))).thenReturn(item);

        Optional<CartItem> resultado = service.updateQuantity(1L, 5);

        assertTrue(resultado.isPresent());
        assertEquals(5, resultado.get().getQuantity());
    }

    @Test
    void removeItem_deberiaEliminarYRetornarTrueCuandoExiste() {
        when(repository.existsById(1L)).thenReturn(true);

        boolean resultado = service.removeItem(1L);

        assertTrue(resultado);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void removeItem_deberiaRetornarFalseCuandoNoExiste() {
        when(repository.existsById(99L)).thenReturn(false);

        boolean resultado = service.removeItem(99L);

        assertFalse(resultado);
        verify(repository, never()).deleteById(any());
    }

    @Test
    void clearCart_deberiaEliminarTodosLosItemsDelUsuario() {
        service.clearCart(1L);

        verify(repository, times(1)).deleteByUserId(1L);
    }
}
