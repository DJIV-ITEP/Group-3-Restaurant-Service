package com.itep.restaurant_service.Component;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.ConnectionFactory;

@Configuration
public class RabbitMQConfig {

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplat.setConnectionFactory(connectionFactory);
        // return RabbitTemplate.setConnectionFactory(connectionFactory);
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        // Configure RabbitMQ connection
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("localhosts");

        return connectionFactory;
    }
}