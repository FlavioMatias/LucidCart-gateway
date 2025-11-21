package com.OrderService.utils.dto

data class ItemOrderRequestDTO(
    val productId: Long,
    val quantity: Long,
    val unitPrice: Double
)