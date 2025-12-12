package com.LucidCart.service

import com.LucidCart.gateway.order.*
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val gateway: OrderGateway
) {

    // ============================================================
    // ADDRESS
    // ============================================================
    fun createAddress(token: String, req: AddressRequestDTO): AddressResponseDTO =
        gateway.createAddress(token, req)

    fun updateAddress(token: String, req: AddressRequestDTO): AddressResponseDTO =
        gateway.updateAddress(token, req)

    fun deleteAddress(token: String) =
        gateway.deleteAddress(token)

    fun findAddress(token: String): AddressResponseDTO =
        gateway.findAddress(token)

    // ============================================================
    // ITEM
    // ============================================================
    fun addItem(token: String, req: ItemOrderRequestDTO): ItemOrderResponseDTO =
        gateway.addItem(token, req)

    fun updateItem(token: String, id: Long, req: ItemOrderRequestDTO): ItemOrderResponseDTO =
        gateway.updateItem(token, id, req)

    fun deleteItem(token: String, id: Long) =
        gateway.deleteItem(token, id)

    // ============================================================
    // ORDER
    // ============================================================
    fun findOrder(token: String, id: Long): OrderResponseDTO =
        gateway.findOrder(token, id)

    fun listOrders(
        token: String,
        status: OrderStatus? = null,
        orderId: Long? = null
    ): List<OrderResponseDTO> =
        gateway.listOrders(token, status, orderId)

    fun deleteOrder(token: String, id: Long) =
        gateway.deleteOrder(token, id)

    // ============================================================
    // CART
    // ============================================================
    fun findCart(token: String): OrderResponseDTO =
        gateway.findCart(token)

    fun sendOrder(token: String, id: Long): OrderResponseDTO =
        gateway.sendOrder(token, id)
}
