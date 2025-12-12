package com.LucidCart.gateway.order

import java.util.Date


enum class OrderStatus {
    CREATED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELED
}
data class AddressRequestDTO(
    var fullAddress: String,
    var latitude: Double,
    var longitude: Double,
)


data class AddressResponseDTO(
    var id: Long? = null,
    var userId: Long,
    var fullAddress: String,
    var latitude: Double,
    var longitude: Double,
    var createdAt: Date? = null,
    var updatedAt: Date? = null
)

data class ItemOrderRequestDTO(
    val productId: Long,
    val quantity: Long,
    val unitPrice: Double
)

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

data class OrderResponseDTO(
    var id: Long,
    var status: OrderStatus,
    var traceCode: String,
    var address: AddressResponseDTO,
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
    var items : List<ItemOrderResponseDTO>
)