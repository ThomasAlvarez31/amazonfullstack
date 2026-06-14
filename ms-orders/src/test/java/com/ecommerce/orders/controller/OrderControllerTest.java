package com.ecommerce.orders.controller;

import com.ecommerce.orders.model.Order;
import com.ecommerce.orders.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService service;

    private Order buildOrder() {
        Order o = new Order();
        o.setUserId(1L);
        o.setProductId(2L);
        o.setQuantity(3);
        o.setTotalPrice(99.0);
        o.setStatus("PENDIENTE");
        return o;
    }

    @Test
    void getAll_deberiaRetornarLista() throws Exception {
        when(service.findAll()).thenReturn(List.of(buildOrder()));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDIENTE"));
    }

    @Test
    void getById_deberiaRetornar200CuandoExiste() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.of(buildOrder()));

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDIENTE"));
    }

    @Test
    void getById_deberiaRetornar404CuandoNoExiste() throws Exception {
        when(service.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/orders/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_deberiaRetornarOrdenCreada() throws Exception {
        Order order = buildOrder();
        when(service.save(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDIENTE"));
    }

    @Test
    void update_deberiaRetornar200CuandoExiste() throws Exception {
        Order updated = buildOrder();
        updated.setStatus("ENVIADO");
        when(service.update(eq(1L), any(Order.class))).thenReturn(Optional.of(updated));

        mockMvc.perform(put("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ENVIADO"));
    }

    @Test
    void update_deberiaRetornar404CuandoNoExiste() throws Exception {
        when(service.update(eq(99L), any(Order.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/orders/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildOrder())))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_deberiaRetornar204CuandoExiste() throws Exception {
        when(service.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_deberiaRetornar404CuandoNoExiste() throws Exception {
        when(service.delete(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/orders/99"))
                .andExpect(status().isNotFound());
    }
}
