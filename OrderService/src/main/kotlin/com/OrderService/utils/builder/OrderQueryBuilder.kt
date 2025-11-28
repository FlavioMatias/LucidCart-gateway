package com.OrderService.utils.builder

import com.OrderService.model.Order
import com.OrderService.model.enuns.OrderStatus
import com.OrderService.repository.OrderRepository
import com.OrderService.utils.spec.OrderSpecification
import org.springframework.data.jpa.domain.Specification

class OrderQueryBuilder {

    private var status: OrderStatus? = null
    private var userId: Long? = null
    private var orderId: Long? = null

    fun status(status: OrderStatus?): OrderQueryBuilder {
        this.status = status
        return this
    }

    fun user(id: Long?): OrderQueryBuilder {
        this.userId = id
        return this
    }

    fun id(id: Long?): OrderQueryBuilder {
        this.orderId = id
        return this
    }

    fun build(repo: OrderRepository): List<Order> {

        var spec: Specification<Order> =
            Specification { _, _, cb -> cb.conjunction() }

        OrderSpecification.hasStatus(status)?.let { spec = spec.and(it) }
        OrderSpecification.belongsToUser(userId)?.let { spec = spec.and(it) }
        OrderSpecification.hasId(orderId)?.let { spec = spec.and(it) }

        return repo.findAll(spec)
    }
}
