package com.LucidCart.service

import com.LucidCart.gateway.order.*
import com.LucidCart.gateway.order.OrderGateway
import com.LucidCart.gateway.order.jabx.*
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val gateway: OrderGateway
) {

    // ============================================================
    // ADDRESS
    // ============================================================

    fun createAddress(req: AddressRequestDTO, token: String): CreateAddressResponseDTO {
        val jabxReq = CreateAddressRequest().apply { address = req.toJaxb() }
        val resp = gateway.createAddress(token, jabxReq)
        return CreateAddressResponseDTO.fromJaxb(resp)
    }

    fun updateAddress(req: AddressRequestDTO, token: String): UpdateAddressResponseDTO {
        val jabxReq = UpdateAddressRequest().apply { address = req.toJaxb() }
        val resp = gateway.updateAddress(token, jabxReq)
        return UpdateAddressResponseDTO.fromJaxb(resp)
    }

    fun deleteAddress(token: String): DeleteAddressResponse {
        val req = DeleteAddressRequest()
        return gateway.deleteAddress(token, req)
    }

    fun findAddress(token: String): FindAddressResponseDTO {
        val req = FindAddressRequest().apply {}
        val resp = gateway.findAddress(token, req)
        return FindAddressResponseDTO.fromJaxb(resp)
    }

    // ============================================================
    // ITEM
    // ============================================================

    fun addItem(req: ItemOrderRequestDTO, token: String): AddItemResponseDTO {
        val jabxReq = AddItemRequest().apply { item = req.toJaxb() }
        val resp = gateway.addItem(token, jabxReq)
        return AddItemResponseDTO.fromJaxb(resp)
    }

    fun updateItem(id : Long,req: ItemOrderRequestDTO, token: String): UpdateItemResponseDTO {
        val jabxReq = UpdateItemRequest().apply {
            this.id = id
            item = req.toJaxb()
        }
        val resp = gateway.updateItem(token, jabxReq)
        return UpdateItemResponseDTO.fromJaxb(resp)
    }

    fun deleteItem(id: Long, token: String): DeleteItemResponse {
        val req = DeleteItemRequest().apply {
            this.id = id
        }
        return gateway.deleteItem(token, req)
    }

    // ============================================================
    // ORDER
    // ============================================================

    fun findOrder(id: Long, token: String): FindOrderResponseDTO {
        val req = FindOrderRequest().apply {
            this.id = id
        }
        val resp = gateway.findOrder(token, req)
        return FindOrderResponseDTO.fromJaxb(resp)
    }

    fun listOrders(
        token: String,
        status: OrderStatusDTO? = null,
        userId: Long? = null,
        orderId: Long? = null
    ): ListOrdersResponseDTO {
        val req = ListOrdersRequest().apply {
            this.status = status?.let { OrderStatusDTO.toJaxb(it) }
            this.userId = userId
            this.orderId = orderId
        }
        val resp = gateway.listOrders(token, req)
        return ListOrdersResponseDTO.fromJaxb(resp.orders)
    }

    fun deleteOrder(id: Long, token: String) {
        val req = DeleteOrderRequest().apply {
            this.id = id
        }
        gateway.deleteOrder(token, req)
    }

    // ============================================================
    // CART
    // ============================================================

    fun findCart(token: String): FindCartResponseDTO {
        val req = FindCartRequest()
        val resp = gateway.findCart(token, req)
        return FindCartResponseDTO.fromJaxb(resp)
    }
    fun sendOrder(id: Long, token: String): SendOrderResponseDTO {
        val req = SendOrderRequest().apply { this.id = id }
        val resp = gateway.sendOrder(token, req)
        return SendOrderResponseDTO.fromJaxb(resp)
    }
}
