package com.LucidCart.controller

import com.LucidCart.gateway.order.*
import com.LucidCart.service.OrderService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*

@Tag(name = "Orders", description = "Management of orders and cart items")
@RestController
@RequestMapping("/api/v1/orders")
class OrderController(
    private val orderService: OrderService
) {

    // ---------- ITEMS ----------

    @Operation(summary = "Add item to order", description = "Adds a product to the authenticated user's cart/order.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Item added successfully"),
            ApiResponse(responseCode = "400", description = "Invalid data"),
            ApiResponse(responseCode = "401", description = "Invalid or missing token")
        ]
    )
    @PostMapping("/items")
    fun addItem(
        @Parameter(hidden = true)
        @RequestHeader("Authorization") token: String,
        @RequestBody req: ItemOrderRequestDTO
    ): ItemOrderResponseDTO {
        return orderService.addItem(token, req)
    }

    @Operation(summary = "Update order item", description = "Updates an existing item in the cart/order.")
    @PutMapping("/items/{id}")
    fun updateItem(
        @PathVariable id: Long,
        @Parameter(hidden = true)
        @RequestHeader("Authorization") token: String,
        @RequestBody req: ItemOrderRequestDTO
    ): ItemOrderResponseDTO {
        return orderService.updateItem(token, id, req)
    }

    @Operation(summary = "Remove order item", description = "Removes an item from the authenticated user's cart/order.")
    @DeleteMapping("/items/{id}")
    fun deleteItem(
        @PathVariable id: Long,
        @Parameter(hidden = true)
        @RequestHeader("Authorization") token: String
    ) {
        orderService.deleteItem(token, id)
    }

    // ---------- ORDERS ----------

    @Operation(summary = "Get order by ID", description = "Returns a specific order of the authenticated user.")
    @GetMapping("/{id}")
    fun findOrder(
        @PathVariable id: Long,
        @Parameter(hidden = true)
        @RequestHeader("Authorization") token: String
    ): OrderResponseDTO {
        return orderService.findOrder(token, id)
    }

    @Operation(summary = "List orders", description = "Returns all orders of the authenticated user, optionally filtered by status or order ID.")
    @GetMapping
    fun listOrders(
        @Parameter(hidden = true)
        @RequestHeader("Authorization") token: String,
        @RequestParam status: OrderStatus? = null,
        @RequestParam orderId: Long? = null
    ): List<OrderResponseDTO> {
        return orderService.listOrders(token, status, orderId = orderId)
    }

    @Operation(summary = "Delete order", description = "Removes a specific order of the authenticated user.")
    @DeleteMapping("/{id}")
    fun deleteOrder(
        @PathVariable id: Long,
        @Parameter(hidden = true)
        @RequestHeader("Authorization") token: String
    ) {
        orderService.deleteOrder(token, id)
    }

    // ---------- CART ----------

    @Operation(summary = "Get user cart", description = "Returns the current order that has not been finalized (cart).")
    @GetMapping("/cart")
    fun findCart(
        @Parameter(hidden = true)
        @RequestHeader("Authorization") token: String
    ): OrderResponseDTO {
        return orderService.findCart(token)
    }

    @Operation(summary = "Finalize order (send)", description = "Finalizes the current cart, converting it into an order.")
    @PostMapping("/{id}/send")
    fun sendOrder(
        @PathVariable id: Long,
        @Parameter(hidden = true)
        @RequestHeader("Authorization") token: String
    ): OrderResponseDTO {
        return orderService.sendOrder(token, id)
    }
}
