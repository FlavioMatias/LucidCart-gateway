package com.LucidCart.controller

import com.LucidCart.gateway.order.*
import com.LucidCart.service.OrderService
import com.LucidCart.controller.assemblers.*
import com.LucidCart.gateway.order.jabx.DeleteOrderResponse
import org.springframework.hateoas.EntityModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/orders")
class OrderController(
    private val orderService: OrderService,
    private val addItemAssembler: AddItemResponseAssembler,
    private val updateItemAssembler: UpdateItemResponseAssembler,
    private val deleteItemAssembler: DeleteItemResponseAssembler,
    private val findOrderAssembler: FindOrderResponseAssembler,
    private val listOrdersAssembler: ListOrdersResponseAssembler,
    private val findCartAssembler: FindCartResponseAssembler,
    private val deleteOrderAssembler: DeleteOrderResponseAssembler,
    private val sendOrderAssembler: SendOrderResponseAssembler
) {

    // ---------- ITEMS ----------

    @PostMapping("/items")
    fun addItem(
        @RequestHeader("Authorization") token: String,
        @RequestBody req: ItemOrderRequestDTO
    ): EntityModel<*> {
        val res: AddItemResponseDTO = orderService.addItem(req, token)
        return addItemAssembler.toModel(res)
    }

    @PutMapping("/items/{id}")
    fun updateItem(
        @PathVariable id: Long,
        @RequestHeader("Authorization") token: String,
        @RequestBody req: ItemOrderRequestDTO
    ): EntityModel<*> {
        val res: UpdateItemResponseDTO = orderService.updateItem(id, req, token)
        return updateItemAssembler.toModel(res)
    }

    @DeleteMapping("/items/{id}")
    fun deleteItem(
        @PathVariable id: Long,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<Void> {
        orderService.deleteItem(id, token)
        return ResponseEntity.noContent().build() // 204
    }

    // ---------- ORDERS ----------

    @GetMapping("/{id}")
    fun findOrder(
        @PathVariable id: Long,
        @RequestHeader("Authorization") token: String
    ): EntityModel<*> {
        val res: FindOrderResponseDTO = orderService.findOrder(id, token)
        return findOrderAssembler.toModel(res)
    }

    @GetMapping
    fun listOrders(
        @RequestHeader("Authorization") token: String,
        @RequestParam status: OrderStatusDTO? = null,
        @RequestParam userId: Long? = null,
        @RequestParam orderId: Long? = null
    ): EntityModel<*> {
        val res: ListOrdersResponseDTO = orderService.listOrders(token, status, userId, orderId)
        return listOrdersAssembler.toModel(res)
    }

    @DeleteMapping("/{id}")
    fun deleteOrder(
        @PathVariable id: Long,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<Void> {
        orderService.deleteOrder(id, token)
        return ResponseEntity.noContent().build() // 204
    }

    // ---------- CART ----------

    @GetMapping("/cart")
    fun findCart(
        @RequestHeader("Authorization") token: String
    ): EntityModel<*> {
        val res: FindCartResponseDTO = orderService.findCart(token)
        return findCartAssembler.toModel(res)
    }

    @PostMapping
    fun sendOrder(
        @RequestHeader("Authorization") token: String,
        @RequestParam id: Long
    ): EntityModel<*> {
        val res: SendOrderResponseDTO = orderService.sendOrder(id, token)
        return sendOrderAssembler.toModel(res)
    }
}
