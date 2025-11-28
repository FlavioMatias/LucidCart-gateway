package com.OrderService.endpoint

import com.OrderService.service.AddressService
import com.OrderService.service.OrderService
import com.OrderService.utils.SecurityContextHolder
import com.OrderService.utils.mapper.SoapMapper
import com.orderservice.contract.*
import com.OrderService.model.enuns.OrderStatus as DomainStatus
import org.springframework.ws.server.endpoint.annotation.Endpoint
import org.springframework.ws.server.endpoint.annotation.PayloadRoot
import org.springframework.ws.server.endpoint.annotation.RequestPayload
import org.springframework.ws.server.endpoint.annotation.ResponsePayload

@Endpoint
class ApiEndpoint(
    private val orderService: OrderService,
    private val addressService: AddressService,
    private val mapper: SoapMapper
) {

    companion object {
        private const val NAMESPACE = "http://orderservice.com/contract"
    }

    private fun getCurrentUserId(): Long =
        SecurityContextHolder.getContext()?.userId
            ?: throw RuntimeException("User not authenticated")

    /* ============================================
       ADDRESS OPERATIONS
       ============================================ */
    @PayloadRoot(namespace = NAMESPACE, localPart = "CreateAddressRequest")
    @ResponsePayload
    fun createAddress(@RequestPayload req: CreateAddressRequest): CreateAddressResponse {
        val userId = getCurrentUserId()
        val dto = mapper.toAddressRequestDTO(req.address)
        val saved = addressService.create(userId, dto)
        val resp = CreateAddressResponse()
        resp.address = mapper.toAddressSoap(saved)
        return resp
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "UpdateAddressRequest")
    @ResponsePayload
    fun updateAddress(@RequestPayload req: UpdateAddressRequest): UpdateAddressResponse {
        val userId = getCurrentUserId()
        val dto = addressService.update(userId, mapper.toAddressRequestDTO(req.address))
        val resp = UpdateAddressResponse()
        resp.address = mapper.toAddressSoap(dto)
        return resp
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "DeleteAddressRequest")
    @ResponsePayload
    fun deleteAddress(@RequestPayload req: DeleteAddressRequest): DeleteAddressResponse {
        val userId = getCurrentUserId()
        addressService.delete(userId)
        return DeleteAddressResponse()
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "FindAddressRequest")
    @ResponsePayload
    fun findAddress(@RequestPayload req: FindAddressRequest): FindAddressResponse {
        val userId = getCurrentUserId()
        val dto = addressService.findAddress(userId)
        val resp = FindAddressResponse()
        resp.address = mapper.toAddressSoap(dto)
        return resp
    }

    /* ============================================
       ORDER OPERATIONS
       ============================================ */
    @PayloadRoot(namespace = NAMESPACE, localPart = "AddItemRequest")
    @ResponsePayload
    fun addItem(@RequestPayload req: AddItemRequest): AddItemResponse {
        val userId = getCurrentUserId()
        val dto = orderService.addItem(userId, mapper.toItemOrderRequestDTO(req.item))
        val resp = AddItemResponse()
        resp.item = mapper.toItemOrderSoap(dto)
        return resp
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "UpdateItemRequest")
    @ResponsePayload
    fun updateItem(@RequestPayload req: UpdateItemRequest): UpdateItemResponse {
        val dto = orderService.updateItem(req.id, mapper.toItemOrderRequestDTO(req.item))
        val resp = UpdateItemResponse()
        resp.item = mapper.toItemOrderSoap(dto)
        return resp
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "DeleteItemRequest")
    @ResponsePayload
    fun deleteItem(@RequestPayload req: DeleteItemRequest): DeleteItemResponse {
        orderService.deleteItem(req.id)
        return DeleteItemResponse()
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "DeleteOrderRequest")
    @ResponsePayload
    fun deleteOrder(@RequestPayload req: DeleteOrderRequest): DeleteOrderResponse {
        orderService.deleteOrder(req.id)
        return DeleteOrderResponse()
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "FindOrderRequest")
    @ResponsePayload
    fun findOrder(@RequestPayload req: FindOrderRequest): FindOrderResponse {
        val userId = getCurrentUserId()
        val dto = orderService.findOrder(userId, req.id)
        val resp = FindOrderResponse()
        resp.order = mapper.toOrderSoap(dto)
        return resp
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "ListOrdersRequest")
    @ResponsePayload
    fun listOrders(@RequestPayload req: ListOrdersRequest): ListOrdersResponse {
        val userId = getCurrentUserId()
        val status: DomainStatus? = req.status?.let { DomainStatus.valueOf(it.name) }
        val orders = orderService.listOrder(status = status, userId = userId, orderId = req.orderId)
        val resp = ListOrdersResponse()
        resp.orders = mapper.toOrderListSoap(orders)
        return resp
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "FindCartRequest")
    @ResponsePayload
    fun findCart(@RequestPayload req: FindCartRequest): FindCartResponse {
        val userId = getCurrentUserId()
        val dto = orderService.findCart(userId)
        val resp = FindCartResponse()
        resp.order = mapper.toOrderSoap(dto)
        return resp
    }

    /* ============================================
       SEND ORDER OPERATION
       ============================================ */
    @PayloadRoot(namespace = NAMESPACE, localPart = "SendOrderRequest")
    @ResponsePayload
    fun sendOrder(@RequestPayload req: SendOrderRequest): SendOrderResponse {
        val dto = orderService.sendOrder(req.id)
        val resp = SendOrderResponse()
        resp.order = mapper.toOrderSoap(dto)
        return resp
    }
}
