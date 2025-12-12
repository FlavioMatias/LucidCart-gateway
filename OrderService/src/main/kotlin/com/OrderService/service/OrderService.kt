package com.OrderService.service

import com.OrderService.mapper.OrderMapper
import com.OrderService.model.ItemOrder
import com.OrderService.model.Order
import com.OrderService.model.enuns.OrderStatus
import com.OrderService.repository.AddressRepository
import com.OrderService.repository.ItemOrderRepository
import com.OrderService.repository.OrderRepository
import com.OrderService.utils.builder.OrderQueryBuilder
import com.OrderService.utils.dto.ItemOrderRequestDTO
import com.OrderService.utils.dto.ItemOrderResponseDTO
import com.OrderService.utils.dto.OrderResponseDTO
import com.OrderService.utils.events.ItemPayload
import com.OrderService.utils.events.OrderCreatedEvent
import com.OrderService.utils.events.OrderEventPublisher
import com.OrderService.utils.factory.ItemOrderFactory
import com.OrderService.utils.factory.OrderFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val orderRepo : OrderRepository,
    private val addressRepo : AddressRepository,
    private val itemRepo: ItemOrderRepository,
    private val itemOrderFactory: ItemOrderFactory,
    private val orderFactory: OrderFactory,
    private val mapper: OrderMapper,
    private val publisher: OrderEventPublisher
) {

    fun addItem(userId : Long, dto: ItemOrderRequestDTO) : ItemOrderResponseDTO{
        val order = orderRepo.findByStatus(OrderStatus.CREATED)
            ?: createOrder(userId)

        val item : ItemOrder =  itemOrderFactory.create(dto, order, userId)
        return mapper.toItemOrderDTO(
            itemRepo.save(item)
        )
    }

    fun updateItem( id: Long, dto: ItemOrderRequestDTO) : ItemOrderResponseDTO{
        val item = itemRepo.findByIdOrNull(id)
            ?: throw RuntimeException("item not found")
        item.quantity = dto.quantity

        return mapper.toItemOrderDTO(
            itemRepo.save(item)
        )
    }

    fun deleteItem(id : Long){ itemRepo.deleteById(id) }

    fun deleteOrder(id : Long){ orderRepo.deleteById(id) }

    fun createOrder(userId: Long): Order {
        val address = addressRepo.findByUserId(userId)
            ?: throw IllegalStateException("User $userId has no address registered")

        val order = orderFactory.create(address = address)

        return orderRepo.save(order)
    }
    fun findOrder(userId : Long, id : Long): OrderResponseDTO{
        val order : Order = orderRepo.findByUserIdAndOrderId(userId, id)
            ?: throw RuntimeException("order not found")

        return mapper.toOrderDTO(order);
    }
    fun listOrder(status: OrderStatus?, userId: Long?, orderId: Long?): List<OrderResponseDTO> {

        val orders = OrderQueryBuilder()
            .status(status)
            .user(userId)
            .id(orderId)
            .build(orderRepo)

        return orders.map(mapper::toOrderDTO)
    }

    fun findCart(userId : Long) : OrderResponseDTO{
        val order = orderRepo.findByUserIdAndStatus(userId, OrderStatus.CREATED)
            ?: throw RuntimeException("Order not found")

        return mapper.toOrderDTO(order)
    }
    @Transactional
    fun sendOrder(userId: Long, orderId: Long): OrderResponseDTO {
        val order = orderRepo.findByIdOrNull(orderId)
            ?: throw RuntimeException("Order not found")

        if (order.status != OrderStatus.CREATED)
            throw IllegalStateException("Only orders in CREATED state can be sent")

        val updated = order.apply { status = OrderStatus.PROCESSING }
        orderRepo.save(updated)

        val event = OrderCreatedEvent(
            orderId = updated.id!!,
            userId = userId,
            items = updated.items.map {
                ItemPayload(
                    productId = it.productId,
                    quantity = it.quantity
                )
            }
        )

        publisher.publishOrderCreated(event)

        return mapper.toOrderDTO(updated)
    }
}