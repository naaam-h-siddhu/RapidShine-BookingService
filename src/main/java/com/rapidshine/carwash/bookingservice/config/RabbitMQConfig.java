package com.rapidshine.carwash.bookingservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String WASHER_STATUS_QUEUE = "washer.status.update.queue";
    public static final String WASHER_STATUS_EXCHANGE = "washer.status.update.exchange";
    public static final String WASHER_STATUS_ROUTING_KEY = "washer.status.update";

    @Bean
    public Queue washerStatusQueue() {
        return new Queue(WASHER_STATUS_QUEUE, true); // durable = true
    }

    @Bean
    public TopicExchange washerStatusExchange() {
        return new TopicExchange(WASHER_STATUS_EXCHANGE);
    }

    @Bean
    public Binding washerStatusBinding() {
        return BindingBuilder
                .bind(washerStatusQueue())
                .to(washerStatusExchange())
                .with(WASHER_STATUS_ROUTING_KEY);
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
