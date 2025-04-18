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

    // Washer status updates (Booking → Washer)
    public static final String WASHER_STATUS_QUEUE = "washer.status.update.queue";
    public static final String WASHER_STATUS_EXCHANGE = "washer.status.update.exchange";
    public static final String WASHER_STATUS_ROUTING_KEY = "washer.status.update";

    // Job completion updates (Washer → Booking)
    public static final String JOB_COMPLETION_QUEUE = "job.completion.update.queue";
    public static final String JOB_COMPLETION_EXCHANGE = "job.completion.exchange";
    public static final String JOB_COMPLETION_ROUTING_KEY = "job.completion.update";

    // Washer status beans
    @Bean
    public Queue washerStatusQueue() {
        return new Queue(WASHER_STATUS_QUEUE, true);
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

    // Job completion beans
    @Bean
    public Queue jobCompletionQueue() {
        return new Queue(JOB_COMPLETION_QUEUE, true);
    }

    @Bean
    public TopicExchange jobCompletionExchange() {
        return new TopicExchange(JOB_COMPLETION_EXCHANGE);
    }

    @Bean
    public Binding jobCompletionBinding() {
        return BindingBuilder
                .bind(jobCompletionQueue())
                .to(jobCompletionExchange())
                .with(JOB_COMPLETION_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
