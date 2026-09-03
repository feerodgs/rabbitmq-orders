package br.com.felipe.rabbitmq_orders.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(RabbitMqNames.ORDERS_EXCHANGE);
    }

    @Bean
    public Queue orderQueue() {
        return new Queue(RabbitMqNames.ORDERS_QUEUE);
    }

    @Bean
    public Binding orderBinding(Queue orderQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(RabbitMqNames.ORDER_CREATED);
    }

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}