package com.OrderService.utils.factory

import com.OrderService.utils.dto.ItemOrderRequestDTO
import com.OrderService.model.ItemOrder
import com.OrderService.model.Order
import org.springframework.stereotype.Component


@Component
class ItemOrderFactory {

    fun create(
        dto: ItemOrderRequestDTO,
        order: Order,
        userId: Long
    ): ItemOrder {

        return ItemOrder(
            userId = userId,
            productId = dto.productId,
            order = order,
            quantity = dto.quantity,
            unitPrice = dto.unitPrice,
            createdAt = null,
            updatedAt = null
        )
    }
}
