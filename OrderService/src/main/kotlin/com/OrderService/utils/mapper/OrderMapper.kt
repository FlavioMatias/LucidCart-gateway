package com.OrderService.mapper


import com.OrderService.model.ItemOrder
import com.OrderService.model.Order
import com.OrderService.utils.dto.ItemOrderResponseDTO
import com.OrderService.utils.dto.OrderResponseDTO
import com.OrderService.utils.mapper.AddressMapper
import org.springframework.stereotype.Component

@Component
class OrderMapper(
    private val addressMapper: AddressMapper
){

    fun toItemOrderDTO(entity: ItemOrder): ItemOrderResponseDTO {
        return ItemOrderResponseDTO(
            id = entity.id!!,
            productId = entity.productId,
            orderId = entity.order.id!!,
            quantity = entity.quantity,
            unitPrice = entity.unitPrice,
            subtotal = entity.subtotal(),
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    fun toOrderDTO(entity: Order): OrderResponseDTO {
        return OrderResponseDTO(
            id = entity.id!!,
            status = entity.status,
            traceCode = entity.traceCode,
            address = addressMapper.toDTO(entity.address),
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            items = entity.items.map { toItemOrderDTO(it) }
        )
    }
}
