package com.OrderService.utils.events

data class OrderCreatedEvent(
    val orderId: Long,
    val userId: Long,
    val items: List<ItemPayload>
)

data class ItemPayload(
    val productId: Long,
    val quantity: Long
)
