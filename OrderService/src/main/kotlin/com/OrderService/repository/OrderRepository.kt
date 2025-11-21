package com.OrderService.repository

import com.OrderService.model.Order
import com.OrderService.model.enuns.OrderStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository :
    JpaRepository<Order, Long>,
    JpaSpecificationExecutor<Order>
{
    fun findByStatus(status: OrderStatus): Order?
    @Query("""
        SELECT DISTINCT o FROM Order o
        JOIN FETCH o.items i
        WHERE i.userId = :userId
        AND o.id = :orderId
    """)
    fun findByUserIdAndOrderId(userId: Long, orderId: Long): Order?
    @Query("""
        SELECT DISTINCT o FROM Order o
        JOIN FETCH o.items i
        WHERE i.userId = :userId
        AND o.status = :status
    """)
    fun findByUserIdAndStatus(userId: Long, status: OrderStatus): Order?
}
