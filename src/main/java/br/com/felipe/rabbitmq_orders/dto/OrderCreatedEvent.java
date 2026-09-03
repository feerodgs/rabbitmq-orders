package br.com.felipe.rabbitmq_orders.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private UUID eventId;
    private UUID orderId;
    private String customerId;
    private String productId;
    private Integer quantity;
}