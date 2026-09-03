package br.com.felipe.rabbitmq_orders.controller;

import br.com.felipe.rabbitmq_orders.dto.CreateOrderRequest;
import br.com.felipe.rabbitmq_orders.dto.OrderCreatedEvent;
import br.com.felipe.rabbitmq_orders.messaging.OrderProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderProducer orderProducer;

    public OrderController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody CreateOrderRequest request) {
        var orderId = UUID.randomUUID();
        var eventId = UUID.randomUUID();

        var customerId = request.getCustomerId();
        var productId = request.getProductId();
        var quantity = request.getQuantity();

        OrderCreatedEvent event = new OrderCreatedEvent(eventId, orderId, customerId, productId, quantity);
        orderProducer.send(event);

        return ResponseEntity.accepted().build();
    }

}
