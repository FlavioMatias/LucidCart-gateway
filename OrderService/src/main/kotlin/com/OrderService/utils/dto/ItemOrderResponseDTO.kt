package com.OrderService.utils.dto

import java.util.Date

data class ItemOrderResponseDTO (
    val id : Long,
    val productId: Long,
    val orderId: Long,
    val quantity: Long,
    val unitPrice: Double,
    val subtotal: Double,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)