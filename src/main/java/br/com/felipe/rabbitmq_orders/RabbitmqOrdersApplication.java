package br.com.felipe.rabbitmq_orders;

import br.com.felipe.rabbitmq_orders.config.RabbitMqConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitmqOrdersApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqOrdersApplication.class, args);
    }


}
