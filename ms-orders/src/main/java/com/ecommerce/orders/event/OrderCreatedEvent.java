package com.ecommerce.orders.event;

import java.io.Serializable;

/** Evento publicado a RabbitMQ cuando se crea una orden. */
public class OrderCreatedEvent implements Serializable {

    private Long orderId;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private Double totalPrice;
    private String status;

    public OrderCreatedEvent() {}

    public OrderCreatedEvent(Long orderId, Long userId, Long productId,
                             Integer quantity, Double totalPrice, String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Long getOrderId()      { return orderId; }
    public Long getUserId()       { return userId; }
    public Long getProductId()    { return productId; }
    public Integer getQuantity()  { return quantity; }
    public Double getTotalPrice() { return totalPrice; }
    public String getStatus()     { return status; }

    public void setOrderId(Long orderId)           { this.orderId = orderId; }
    public void setUserId(Long userId)             { this.userId = userId; }
    public void setProductId(Long productId)       { this.productId = productId; }
    public void setQuantity(Integer quantity)      { this.quantity = quantity; }
    public void setTotalPrice(Double totalPrice)   { this.totalPrice = totalPrice; }
    public void setStatus(String status)           { this.status = status; }
}
