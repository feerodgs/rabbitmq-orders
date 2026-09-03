package br.com.felipe.rabbitmq_orders.config;

public final class RabbitMqNames {

    private RabbitMqNames() {
    }

    public static final String ORDERS_EXCHANGE = "orders.direct";
    public static final String ORDERS_QUEUE = "orders.processing";
    public static final String ORDER_CREATED = "order.created";
}
