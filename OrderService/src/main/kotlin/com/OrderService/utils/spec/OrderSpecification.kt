package com.OrderService.utils.spec

import com.OrderService.model.Order
import com.OrderService.model.enuns.OrderStatus
import org.springframework.data.jpa.domain.Specification
import jakarta.persistence.criteria.JoinType

object OrderSpecification {

    fun hasStatus(status: OrderStatus?): Specification<Order>? {
        if (status == null) return null

        return Specification { root, _, cb ->
            cb.equal(root.get<OrderStatus>("status"), status)
        }
    }

    fun hasId(id: Long?): Specification<Order>? {
        if (id == null) return null

        return Specification { root, _, cb ->
            cb.equal(root.get<Long>("id"), id)
        }
    }

    fun belongsToUser(userId: Long?): Specification<Order>? {
        if (userId == null) return null

        return Specification { root, query, cb ->
            val joinItems = root.join<Any, Any>("items", JoinType.INNER)
            cb.equal(joinItems.get<Long>("userId"), userId)
        }
    }
}
