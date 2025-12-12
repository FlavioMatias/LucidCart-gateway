package com.LucidCart.gateway.order

import org.springframework.web.service.annotation.*
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PathVariable

interface OrderClientHTTP {

    // ---------------- ADDRESS ----------------
    @PostExchange(
        "/api/v1/address",
        headers = ["Content-Type=application/json", "Accept=application/json"]
    )
    fun createAddress(
        @RequestHeader("Authorization") token: String,
        @RequestBody req: AddressRequestDTO
    ): AddressResponseDTO

    @PutExchange(
        "/api/v1/address",
        headers = ["Content-Type=application/json", "Accept=application/json"]
    )
    fun updateAddress(
        @RequestHeader("Authorization") token: String,
        @RequestBody req: AddressRequestDTO
    ): AddressResponseDTO

    @DeleteExchange("/api/v1/address")
    fun deleteAddress(@RequestHeader("Authorization") token: String)

    @GetExchange("/api/v1/address")
    fun findAddress(@RequestHeader("Authorization") token: String): AddressResponseDTO

    // ---------------- ITEM ----------------
    @PostExchange(
        "/api/v1/orders/items",
        headers = ["Content-Type=application/json", "Accept=application/json"]
    )
    fun addItem(
        @RequestHeader("Authorization") token: String,
        @RequestBody req: ItemOrderRequestDTO
    ): ItemOrderResponseDTO

    @PutExchange(
        "/api/v1/orders/items/{id}",
        headers = ["Content-Type=application/json", "Accept=application/json"]
    )
    fun updateItem(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: Long,
        @RequestBody req: ItemOrderRequestDTO
    ): ItemOrderResponseDTO

    @DeleteExchange("/api/v1/orders/items/{id}")
    fun deleteItem(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: Long
    )

    // ---------------- ORDER ----------------
    @GetExchange("/api/v1/orders/{id}")
    fun findOrder(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: Long
    ): OrderResponseDTO

    @GetExchange("/api/v1/orders")
    fun listOrders(
        @RequestHeader("Authorization") token: String,
        @RequestParam("status", required = false) status: OrderStatus?,
        @RequestParam("orderId", required = false) orderId: Long?
    ): List<OrderResponseDTO>

    @DeleteExchange("/api/v1/orders/{id}")
    fun deleteOrder(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: Long
    )

    @GetExchange("/api/v1/orders/cart")
    fun findCart(@RequestHeader("Authorization") token: String): OrderResponseDTO

    @PostExchange("/api/v1/orders/{id}/send")
    fun sendOrder(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: Long
    ): OrderResponseDTO
}
