package br.com.felipe.rabbitmq_orders.messaging;

import br.com.felipe.rabbitmq_orders.config.RabbitMqNames;
import br.com.felipe.rabbitmq_orders.dto.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessingConsumer {

    @RabbitListener(queues = RabbitMqNames.ORDERS_QUEUE)
    public void listenerOfQueue (OrderCreatedEvent event){
        System.out.println("Pedido enviado para processamento: " + event);
    }
}