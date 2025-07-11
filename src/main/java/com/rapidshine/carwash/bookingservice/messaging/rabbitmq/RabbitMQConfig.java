package com.rapidshine.carwash.bookingservice.messaging.rabbitmq;

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
    // Booking notification sending (Booking -> Notification)
    public static final String BOOKING_QUEUE_NAME = "booking.notification";
    public static final String BOOKING_EXCHANGE_NAME = "booking.exchange";
    public static final String BOOKING_ROUTING_KEY = "booking.created";

    // Washer status updates (Booking → Washer)
    public static final String WASHER_STATUS_QUEUE = "washer.status.update.queue";
    public static final String WASHER_STATUS_EXCHANGE = "washer.status.update.exchange";
    public static final String WASHER_STATUS_ROUTING_KEY = "washer.status.update";

    // Job completion updates (Washer → Booking)
    public static final String JOB_COMPLETION_QUEUE = "job.completion.update.queue";
    public static final String JOB_COMPLETION_EXCHANGE = "job.completion.exchange";
    public static final String JOB_COMPLETION_ROUTING_KEY = "job.completion.update";
    // Washer service count update(Booking-> Washer)
    public static final String WASHER_COUNT_QUEUE = "washer.count.update.queue";
    public static final String WASHER_COUNT_EXCHANGE = "washer.count.update.exchange";
    public static final String WASHER_COUNT_ROUTING_KEY = "washer.count.update";

    // Notification Db update request
    public static final String NOTIFICATION_UPDATE_QUEUE = "notification.request.queue";
    public static  final  String NOTIFICATION_UPDATE_EXCHANGE = "notification.request.exchange";
    public static final String NOTIFICATION_UPDATE_ROUTING_KEY = "notification.request.routing.key";
    //washer count beans
    @Bean
    public Queue washerCountQueue() {
        return new Queue(WASHER_COUNT_QUEUE, true);
    }

    @Bean
    public TopicExchange washerCountExchange() {
        return new TopicExchange(WASHER_COUNT_EXCHANGE);
    }

    @Bean
    public Binding washerCountBinding() {
        return BindingBuilder
                .bind(washerCountQueue())
                .to(washerCountExchange())
                .with(WASHER_COUNT_ROUTING_KEY);
    }
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

    // booking message beans
    @Bean
    public Queue bookingCreatedQueue(){
        return new Queue(BOOKING_QUEUE_NAME,true
        );
    }

    @Bean
    public TopicExchange bookingTopicExchange(){
        return new TopicExchange(BOOKING_EXCHANGE_NAME);
    }
    @Bean
    public Binding BookingBinding(){
        return BindingBuilder
                .bind(bookingCreatedQueue())
                .to(bookingTopicExchange())
                .with(BOOKING_ROUTING_KEY);
    }

    // Notification update request
    @Bean
    public Queue notificaitonUpdateQueue(){
        return new Queue(NOTIFICATION_UPDATE_QUEUE,true
        );
    }

    @Bean
    public TopicExchange notificationTopicExchange(){
        return new TopicExchange(NOTIFICATION_UPDATE_EXCHANGE);
    }
    @Bean
    public Binding notificaionBinding(){
        return BindingBuilder
                .bind(notificaitonUpdateQueue())
                .to(notificationTopicExchange())
                .with(NOTIFICATION_UPDATE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
