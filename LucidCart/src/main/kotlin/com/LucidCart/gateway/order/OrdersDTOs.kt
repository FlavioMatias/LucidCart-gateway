package com.LucidCart.gateway.order

import com.LucidCart.gateway.order.jabx.*
import org.springframework.hateoas.RepresentationModel

// ==================== ENUM ====================
enum class OrderStatusDTO {
    CREATED, PROCESSING, SHIPPED, DELIVERED, CANCELED;

    companion object {
        fun fromJaxb(jaxb: OrderStatus): OrderStatusDTO = valueOf(jaxb.name)
        fun toJaxb(dto: OrderStatusDTO): OrderStatus = OrderStatus.valueOf(dto.name)
    }
}

// ==================== ITEM ====================
data class ItemOrderDTO(
    val id: Long?,
    val productId: Long,
    val orderId: Long,
    val quantity: Long,
    val unitPrice: Double,
    val subtotal: Double
) {
    companion object {
        fun fromJaxb(jaxb: ItemOrderSoap) = ItemOrderDTO(
            id = jaxb.id,
            productId = jaxb.productId,
            orderId = jaxb.orderId,
            quantity = jaxb.quantity,
            unitPrice = jaxb.unitPrice,
            subtotal = jaxb.subtotal
        )
    }

    fun toJaxb() = ItemOrderSoap().apply {
        id = this@ItemOrderDTO.id
        productId = this@ItemOrderDTO.productId
        orderId = this@ItemOrderDTO.orderId
        quantity = this@ItemOrderDTO.quantity
        unitPrice = this@ItemOrderDTO.unitPrice
        subtotal = this@ItemOrderDTO.subtotal
    }
}

// ==================== ITEM REQUEST ====================
data class ItemOrderRequestDTO(
    val productId: Long,
    val quantity: Long,
    val unitPrice: Double
) {
    fun toJaxb() = ItemOrderRequestSoap().apply {
        productId = this@ItemOrderRequestDTO.productId
        quantity = this@ItemOrderRequestDTO.quantity
        unitPrice = this@ItemOrderRequestDTO.unitPrice
    }

    companion object {
        fun fromJaxb(jaxb: ItemOrderRequestSoap) = ItemOrderRequestDTO(
            productId = jaxb.productId,
            quantity = jaxb.quantity,
            unitPrice = jaxb.unitPrice
        )
    }
}

// ==================== ADDRESS ====================
data class AddressDTO(
    val id: Long?,
    val userId: Long,
    val fullAddress: String,
    val latitude: Double,
    val longitude: Double
) {
    companion object {
        fun fromJaxb(jaxb: AddressSoap) = AddressDTO(
            id = jaxb.id,
            userId = jaxb.userId,
            fullAddress = jaxb.fullAddress,
            latitude = jaxb.latitude,
            longitude = jaxb.longitude
        )
    }

    fun toJaxb() = AddressSoap().apply {
        id = this@AddressDTO.id
        userId = this@AddressDTO.userId
        fullAddress = this@AddressDTO.fullAddress
        latitude = this@AddressDTO.latitude
        longitude = this@AddressDTO.longitude
    }
}

// ==================== ADDRESS REQUEST ====================
data class AddressRequestDTO(
    val fullAddress: String,
    val latitude: Double,
    val longitude: Double
) {
    fun toJaxb() = AddressRequestSoap().apply {
        fullAddress = this@AddressRequestDTO.fullAddress
        latitude = this@AddressRequestDTO.latitude
        longitude = this@AddressRequestDTO.longitude
    }

    companion object {
        fun fromJaxb(jaxb: AddressRequestSoap) = AddressRequestDTO(
            fullAddress = jaxb.fullAddress,
            latitude = jaxb.latitude,
            longitude = jaxb.longitude
        )
    }
}


// ==================== ORDER ====================
data class OrderDTO(
    val id: Long,
    val status: OrderStatusDTO,
    val traceCode: String,
    val address: AddressDTO,
    val items: List<ItemOrderDTO>
) {
    companion object {
        fun fromJaxb(jaxb: OrderSoap) = OrderDTO(
            id = jaxb.id,
            status = OrderStatusDTO.fromJaxb(jaxb.status),
            traceCode = jaxb.traceCode,
            address = AddressDTO.fromJaxb(jaxb.address),
            items = jaxb.items.item.map { ItemOrderDTO.fromJaxb(it) }
        )
    }

    fun toJaxb() = OrderSoap().apply {
        id = this@OrderDTO.id
        status = OrderStatusDTO.toJaxb(this@OrderDTO.status)
        traceCode = this@OrderDTO.traceCode
        address = this@OrderDTO.address.toJaxb()
        items = ItemOrderListSoap().apply {
            item.addAll(this@OrderDTO.items.map { it.toJaxb() })
        }
    }
}

// ==================== ORDER LIST ====================
data class OrderListDTO(
    val orders: List<OrderDTO>
) {
    companion object {
        fun fromJaxb(jaxb: OrderListSoap) = OrderListDTO(
            orders = jaxb.order.map { OrderDTO.fromJaxb(it) }
        )
    }

    fun toJaxb() = OrderListSoap().apply {
        order.addAll(this@OrderListDTO.orders.map { it.toJaxb() })
    }
}

// ==================== RESPONSE DTOs ====================
class AddItemResponseDTO(val item: ItemOrderDTO) : RepresentationModel<AddItemResponseDTO>() {
    companion object {
        fun fromJaxb(jaxb: AddItemResponse) = AddItemResponseDTO(
            item = ItemOrderDTO.fromJaxb(jaxb.item)
        )
    }

    fun toJaxb() = AddItemResponse().apply {
        item = this@AddItemResponseDTO.item.toJaxb()
    }
}

class UpdateItemResponseDTO(val item: ItemOrderDTO) : RepresentationModel<UpdateItemResponseDTO>() {
    companion object {
        fun fromJaxb(jaxb: UpdateItemResponse) = UpdateItemResponseDTO(
            item = ItemOrderDTO.fromJaxb(jaxb.item)
        )
    }

    fun toJaxb() = UpdateItemResponse().apply {
        item = this@UpdateItemResponseDTO.item.toJaxb()
    }
}

class FindOrderResponseDTO(val order: OrderDTO) : RepresentationModel<FindOrderResponseDTO>() {
    companion object {
        fun fromJaxb(jaxb: FindOrderResponse) = FindOrderResponseDTO(
            order = OrderDTO.fromJaxb(jaxb.order)
        )
    }

    fun toJaxb() = FindOrderResponse().apply {
        order = this@FindOrderResponseDTO.order.toJaxb()
    }
}

class ListOrdersResponseDTO(val orders: List<OrderDTO>) : RepresentationModel<ListOrdersResponseDTO>() {
    companion object {
        fun fromJaxb(jaxb: OrderListSoap) = ListOrdersResponseDTO(
            orders = jaxb.order.map { OrderDTO.fromJaxb(it) }
        )
    }

    fun toJaxb() = OrderListSoap().apply {
        order.addAll(this@ListOrdersResponseDTO.orders.map { it.toJaxb() })
    }
}

// ==================== ADDRESS RESPONSE DTOs ====================
class CreateAddressResponseDTO(val address: AddressDTO) : RepresentationModel<CreateAddressResponseDTO>() {
    companion object {
        fun fromJaxb(jaxb: CreateAddressResponse) = CreateAddressResponseDTO(
            address = AddressDTO.fromJaxb(jaxb.address)
        )
    }

    fun toJaxb() = CreateAddressResponse().apply {
        address = this@CreateAddressResponseDTO.address.toJaxb()
    }
}

class UpdateAddressResponseDTO(val address: AddressDTO) : RepresentationModel<UpdateAddressResponseDTO>() {
    companion object {
        fun fromJaxb(jaxb: UpdateAddressResponse) = UpdateAddressResponseDTO(
            address = AddressDTO.fromJaxb(jaxb.address)
        )
    }

    fun toJaxb() = UpdateAddressResponse().apply {
        address = this@UpdateAddressResponseDTO.address.toJaxb()
    }
}

class FindAddressResponseDTO(val address: AddressDTO) : RepresentationModel<FindAddressResponseDTO>() {
    companion object {
        fun fromJaxb(jaxb: FindAddressResponse) = FindAddressResponseDTO(
            address = AddressDTO.fromJaxb(jaxb.address)
        )
    }

    fun toJaxb() = FindAddressResponse().apply {
        address = this@FindAddressResponseDTO.address.toJaxb()
    }
}

class FindCartResponseDTO(val order: OrderDTO) : RepresentationModel<FindCartResponseDTO>() {
    companion object {
        fun fromJaxb(jaxb: FindCartResponse) = FindCartResponseDTO(
            order = OrderDTO.fromJaxb(jaxb.order)
        )
    }

    fun toJaxb() = FindCartResponse().apply {
        order = this@FindCartResponseDTO.order.toJaxb()
    }
}
class SendOrderResponseDTO(val order: OrderDTO) : RepresentationModel<SendOrderResponseDTO>() {
    companion object {
        fun fromJaxb(jaxb: SendOrderResponse) = SendOrderResponseDTO(
            order = OrderDTO.fromJaxb(jaxb.order)
        )
    }

    fun toJaxb() = SendOrderResponse().apply {
        order = this@SendOrderResponseDTO.order.toJaxb()
    }
}
