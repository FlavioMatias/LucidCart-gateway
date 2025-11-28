package com.LucidCart.controller.assemblers

import com.LucidCart.controller.OrderController
import com.LucidCart.controller.AddressController
import com.LucidCart.gateway.order.*
import com.LucidCart.gateway.order.jabx.*
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*
import org.springframework.stereotype.Component

// ==================== ORDER / ITEM ASSEMBLERS ====================

@Component
class AddItemResponseAssembler :
    RepresentationModelAssembler<AddItemResponseDTO, EntityModel<AddItemResponseDTO>> {

    override fun toModel(res: AddItemResponseDTO): EntityModel<AddItemResponseDTO> {
        val id = res.item.id ?: throw RuntimeException("Item.id não pode ser nulo")
        return EntityModel.of(
            res,
            linkTo(methodOn(OrderController::class.java).updateItem(id, "TOKEN", ItemOrderRequestDTO(1, 2, 3.3))).withRel("update"),
            linkTo(methodOn(OrderController::class.java).deleteItem(id, "TOKEN")).withRel("delete"),
            linkTo(methodOn(OrderController::class.java).findOrder(res.item.orderId, "TOKEN")).withRel("order")
        )
    }
}

@Component
class UpdateItemResponseAssembler :
    RepresentationModelAssembler<UpdateItemResponseDTO, EntityModel<UpdateItemResponseDTO>> {

    override fun toModel(res: UpdateItemResponseDTO): EntityModel<UpdateItemResponseDTO> {
        val id = res.item.id ?: throw RuntimeException("Item.id não pode ser nulo")
        return EntityModel.of(
            res,
            linkTo(methodOn(OrderController::class.java).updateItem(id, "TOKEN", ItemOrderRequestDTO(1, 2, 3.3))).withSelfRel(),
            linkTo(methodOn(OrderController::class.java).deleteItem(id, "TOKEN")).withRel("delete"),
            linkTo(methodOn(OrderController::class.java).findOrder(res.item.orderId, "TOKEN")).withRel("order")
        )
    }
}

@Component
class DeleteItemResponseAssembler :
    RepresentationModelAssembler<DeleteItemResponse, EntityModel<DeleteItemResponse>> {

    override fun toModel(res: DeleteItemResponse): EntityModel<DeleteItemResponse> =
        EntityModel.of(
            res,
            linkTo(methodOn(OrderController::class.java).findCart("TOKEN")).withRel("cart"),
            linkTo(methodOn(OrderController::class.java).listOrders("TOKEN", null, null, null)).withRel("orders")
        )
}

@Component
class FindOrderResponseAssembler :
    RepresentationModelAssembler<FindOrderResponseDTO, EntityModel<FindOrderResponseDTO>> {

    override fun toModel(res: FindOrderResponseDTO): EntityModel<FindOrderResponseDTO> {
        val orderId = res.order.id
        return EntityModel.of(
            res,
            linkTo(methodOn(OrderController::class.java).findOrder(orderId, "TOKEN")).withSelfRel(),
            linkTo(methodOn(OrderController::class.java).deleteOrder(orderId, "TOKEN")).withRel("delete"),
            linkTo(methodOn(OrderController::class.java).listOrders("TOKEN", null, null, null)).withRel("all-orders"),
            res.order.items.firstOrNull()?.let { firstItem ->
                linkTo(methodOn(OrderController::class.java).addItem("TOKEN", ItemOrderRequestDTO(1, 2, 3.3))).withRel("add-item")
            }
        )
    }
}

@Component
class ListOrdersResponseAssembler :
    RepresentationModelAssembler<ListOrdersResponseDTO, EntityModel<ListOrdersResponseDTO>> {

    override fun toModel(res: ListOrdersResponseDTO): EntityModel<ListOrdersResponseDTO> =
        EntityModel.of(
            res,
            linkTo(methodOn(OrderController::class.java).listOrders("TOKEN", null, null, null)).withSelfRel(),
            linkTo(methodOn(OrderController::class.java).findCart("TOKEN")).withRel("cart")
        )
}

@Component
class FindCartResponseAssembler :
    RepresentationModelAssembler<FindCartResponseDTO, EntityModel<FindCartResponseDTO>> {

    override fun toModel(res: FindCartResponseDTO): EntityModel<FindCartResponseDTO> {
        val orderId = res.order.id
        return EntityModel.of(
            res,
            linkTo(methodOn(OrderController::class.java).findCart("TOKEN")).withSelfRel(),
            res.order.items.firstOrNull()?.let { firstItem ->
                linkTo(methodOn(OrderController::class.java).addItem("TOKEN", ItemOrderRequestDTO(1, 2, 3.3))).withRel("add-item")
            },
            linkTo(methodOn(OrderController::class.java).listOrders("TOKEN", null, null, null)).withRel("orders")
        )
    }
}

@Component
class DeleteOrderResponseAssembler :
    RepresentationModelAssembler<DeleteOrderResponse, EntityModel<DeleteOrderResponse>> {

    override fun toModel(res: DeleteOrderResponse): EntityModel<DeleteOrderResponse> =
        EntityModel.of(
            res,
            linkTo(methodOn(OrderController::class.java).listOrders("TOKEN", null, null, null)).withRel("all-orders"),
            linkTo(methodOn(OrderController::class.java).findCart("TOKEN")).withRel("cart")
        )
}
@Component
class SendOrderResponseAssembler :
    RepresentationModelAssembler<SendOrderResponseDTO, EntityModel<SendOrderResponseDTO>> {

    override fun toModel(res: SendOrderResponseDTO): EntityModel<SendOrderResponseDTO> {
        val orderId = res.order.id
        return EntityModel.of(
            res,
            linkTo(methodOn(OrderController::class.java).findOrder(orderId, "TOKEN")).withRel("order"),
            linkTo(methodOn(OrderController::class.java).listOrders("TOKEN", null, null, null)).withRel("all-orders"),
            linkTo(methodOn(OrderController::class.java).findCart("TOKEN")).withRel("cart"),
            res.order.items.firstOrNull()?.let { firstItem ->
                linkTo(methodOn(OrderController::class.java).addItem("TOKEN", ItemOrderRequestDTO(1, 2, 3.3))).withRel("add-item")
            }
        )
    }
}
