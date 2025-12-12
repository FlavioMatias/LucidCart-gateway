package com.LucidCart.gateway.order

import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientResponseException

@Component
class OrderGateway(
    private val client: OrderClientHTTP
) {

    // ---------------- ADDRESS ----------------
    fun createAddress(token: String, req: AddressRequestDTO): AddressResponseDTO =
        execute { client.createAddress(token, req) }

    fun updateAddress(token: String, req: AddressRequestDTO): AddressResponseDTO =
        execute { client.updateAddress(token, req) }

    fun deleteAddress(token: String) =
        execute { client.deleteAddress(token) }

    fun findAddress(token: String): AddressResponseDTO =
        execute { client.findAddress(token) }

    // ---------------- ITEM ----------------
    fun addItem(token: String, req: ItemOrderRequestDTO): ItemOrderResponseDTO =
        execute { client.addItem(token, req) }

    fun updateItem(token: String, id: Long, req: ItemOrderRequestDTO): ItemOrderResponseDTO =
        execute { client.updateItem(token, id, req) }

    fun deleteItem(token: String, id: Long) =
        execute { client.deleteItem(token, id) }

    // ---------------- ORDER ----------------
    fun findOrder(token: String, id: Long): OrderResponseDTO =
        execute { client.findOrder(token, id) }

    fun listOrders(token: String, status: OrderStatus? = null, orderId: Long? = null): List<OrderResponseDTO> =
        execute { client.listOrders(token, status, orderId) }

    fun deleteOrder(token: String, id: Long) =
        execute { client.deleteOrder(token, id) }

    // ---------------- CART ----------------
    fun findCart(token: String): OrderResponseDTO =
        execute { client.findCart(token) }

    fun sendOrder(token: String, id: Long): OrderResponseDTO =
        execute { client.sendOrder(token, id) }

    private fun <T> execute(call: () -> T): T {
        return try {
            call()
        } catch (ex: RestClientResponseException) {
            throw ex
        } catch (ex: Exception) {
            throw RuntimeException("Unexpected error in gateway", ex)
        }
    }
}
