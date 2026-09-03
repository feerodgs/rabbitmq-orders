package br.com.felipe.rabbitmq_orders.messaging;

import br.com.felipe.rabbitmq_orders.config.RabbitMqNames;
import br.com.felipe.rabbitmq_orders.dto.OrderCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer {
    private final RabbitTemplate rabbitTemplate;

    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(OrderCreatedEvent event) {
        rabbitTemplate.convertAndSend(RabbitMqNames.ORDERS_EXCHANGE, RabbitMqNames.ORDER_CREATED, event);
    }
}
