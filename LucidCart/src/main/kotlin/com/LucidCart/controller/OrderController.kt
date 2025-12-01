package com.LucidCart.controller

import com.LucidCart.gateway.order.*
import com.LucidCart.service.OrderService
import com.LucidCart.controller.assemblers.*
import org.springframework.hateoas.EntityModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@Tag(name = "Orders", description = "Gerenciamento de pedidos e itens do carrinho")
@RestController
@RequestMapping("/api/v1/orders")
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

    @Operation(summary = "Adicionar item ao pedido", description = "Adiciona um produto ao carrinho/pedido do usuário autenticado.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Item adicionado com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados inválidos"),
            ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
        ]
    )
    @PostMapping("/items")
    fun addItem(
        @Parameter(hidden = true)
        @RequestHeader("Authorization") token: String,
        @RequestBody req: ItemOrderRequestDTO
    ): EntityModel<*> {
        val res: AddItemResponseDTO = orderService.addItem(req, token)
        return addItemAssembler.toModel(res)
    }

    @Operation(summary = "Atualizar item do pedido", description = "Atualiza um item existente no carrinho/pedido.")
    @PutMapping("/items/{id}")
    fun updateItem(
        @PathVariable id: Long,
        @Parameter(hidden = true)
        @RequestHeader("Authorization") token: String,
        @RequestBody req: ItemOrderRequestDTO
    ): EntityModel<*> {
        val res: UpdateItemResponseDTO = orderService.updateItem(id, req, token)
        return updateItemAssembler.toModel(res)
    }

    @Operation(summary = "Remover item do pedido", description = "Remove um item do carrinho/pedido do usuário.")
    @DeleteMapping("/items/{id}")
    fun deleteItem(
        @PathVariable id: Long,
        @Parameter(hidden = true)
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<Void> {
        orderService.deleteItem(id, token)
        return ResponseEntity.noContent().build()
    }

    // ---------- ORDERS ----------

    @Operation(summary = "Buscar pedido pelo ID", description = "Retorna um pedido específico do usuário.")
    @GetMapping("/{id}")
    fun findOrder(
        @PathVariable id: Long,
        @Parameter(hidden = true)
        @RequestHeader("Authorization") token: String
    ): EntityModel<*> {
        val res: FindOrderResponseDTO = orderService.findOrder(id, token)
        return findOrderAssembler.toModel(res)
    }

    @Operation(summary = "Listar pedidos", description = "Retorna todos os pedidos do usuário, com filtros opcionais por status, usuário ou ID do pedido.")
    @GetMapping
    fun listOrders(
        @Parameter(hidden = true)
        @RequestHeader("Authorization") token: String,
        @RequestParam status: OrderStatusDTO? = null,
        @RequestParam userId: Long? = null,
        @RequestParam orderId: Long? = null
    ): EntityModel<*> {
        val res: ListOrdersResponseDTO = orderService.listOrders(token, status, userId, orderId)
        return listOrdersAssembler.toModel(res)
    }

    @Operation(summary = "Excluir pedido", description = "Remove um pedido específico do usuário.")
    @DeleteMapping("/{id}")
    fun deleteOrder(
        @PathVariable id: Long,
        @Parameter(hidden = true)
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<Void> {
        orderService.deleteOrder(id, token)
        return ResponseEntity.noContent().build()
    }

    // ---------- CART ----------

    @Operation(summary = "Ver carrinho do usuário", description = "Retorna o pedido que ainda não foi finalizado (carrinho).")
    @GetMapping("/cart")
    fun findCart(
        @Parameter(hidden = true)
        @RequestHeader("Authorization") token: String
    ): EntityModel<*> {
        val res: FindCartResponseDTO = orderService.findCart(token)
        return findCartAssembler.toModel(res)
    }

    @Operation(summary = "Finalizar pedido (enviar)", description = "Finaliza o carrinho atual transformando-o em pedido.")
    @PostMapping
    fun sendOrder(
        @Parameter(hidden = true)
        @RequestHeader("Authorization") token: String,
        @RequestParam id: Long
    ): EntityModel<*> {
        val res: SendOrderResponseDTO = orderService.sendOrder(id, token)
        return sendOrderAssembler.toModel(res)
    }
}
