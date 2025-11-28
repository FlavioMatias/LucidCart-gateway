package com.OrderService.utils.dto

import com.OrderService.model.enuns.OrderStatus
import java.util.Date

data class OrderResponseDTO(
    var id: Long,
    var status: OrderStatus,
    var traceCode: String,
    var address: AddressResponseDTO,
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
    var items : List<ItemOrderResponseDTO>
)
