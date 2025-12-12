package com.OrderService.config

import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfig {

    @Bean
    fun ordersExchange() = TopicExchange("orders.topic")

    @Bean
    fun orderCreatedQueue() = Queue("order.created.q", true)

    @Bean
    fun orderCreatedBinding(
        queue: Queue,
        exchange: TopicExchange
    ) = BindingBuilder
        .bind(queue)
        .to(exchange)
        .with("order.created")
}
