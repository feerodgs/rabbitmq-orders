package br.com.felipe.rabbitmq_orders.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private String customerId;
    private String productId;
    private Integer quantity;
}
