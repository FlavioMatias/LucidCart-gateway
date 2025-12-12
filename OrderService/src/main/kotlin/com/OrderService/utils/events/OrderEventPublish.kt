package com.OrderService.utils.events

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.stereotype.Service

@Service
class OrderEventPublisher(
    private val rabbit: AmqpTemplate,
    private val objectMapper: ObjectMapper
){
    fun publishOrderCreated(event: OrderCreatedEvent){
        val json = objectMapper.writeValueAsString(event)
        rabbit.convertAndSend(
            "orders.topic",
            "order.created",
            json
        )
    }
}
