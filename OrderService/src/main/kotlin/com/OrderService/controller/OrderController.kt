package com.OrderService.controller

import com.OrderService.model.enuns.OrderStatus
import com.OrderService.service.OrderService
import com.OrderService.utils.dto.ItemOrderRequestDTO
import com.OrderService.utils.dto.ItemOrderResponseDTO
import com.OrderService.utils.dto.OrderResponseDTO
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.Base64

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(
    private val orderService: OrderService
) {

    fun getCurrentUserId(): Long {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw RuntimeException("Usuário não autenticado")

        val principal = authentication.principal
        return when (principal) {
            is Long -> principal
            is String -> principal.toLongOrNull()
                ?: throw RuntimeException("Principal não é um número válido")
            else -> throw RuntimeException("Principal do token inválido")
        }
    }
    @PostMapping("/items")
    fun addItem(@RequestBody req: ItemOrderRequestDTO): ItemOrderResponseDTO {
        val userId = getCurrentUserId()
        return orderService.addItem(userId, req)
    }

    @PutMapping("/items/{id}")
    fun updateItem(@PathVariable id: Long, @RequestBody req: ItemOrderRequestDTO): ItemOrderResponseDTO {
        return orderService.updateItem(id, req)
    }

    @DeleteMapping("/items/{id}")
    fun deleteItem(@PathVariable id: Long) {
        orderService.deleteItem(id)
    }

    @GetMapping("/{id}")
    fun findOrder(@PathVariable id: Long): OrderResponseDTO {
        val userId = getCurrentUserId()
        return orderService.findOrder(userId, id)
    }

    @GetMapping
    fun listOrders(
        @RequestParam(required = false) status: OrderStatus?,
        @RequestParam(required = false) orderId: Long?
    ): List<OrderResponseDTO> {
        val userId = getCurrentUserId()
        return orderService.listOrder(
            status = status,
            userId = userId,
            orderId = orderId
        )
    }

    @DeleteMapping("/{id}")
    fun deleteOrder(@PathVariable id: Long) {
        orderService.deleteOrder(id)
    }

    @GetMapping("/cart")
    fun findCart(): OrderResponseDTO {
        val userId = getCurrentUserId()
        return orderService.findCart(userId)
    }

    @PostMapping("/{id}/send")
    fun sendOrder(@PathVariable id: Long): OrderResponseDTO {
        val userId = getCurrentUserId()
        return orderService.sendOrder(userId ,id)
    }
}
