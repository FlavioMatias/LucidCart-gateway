package com.OrderService.utils.factory

import com.OrderService.model.Address
import com.OrderService.model.Order
import com.OrderService.model.enuns.OrderStatus
import org.springframework.stereotype.Component

@Component
class OrderFactory {

    fun create(address: Address): Order {
        return Order(
            status = OrderStatus.CREATED,
            traceCode = generateTraceCode(),
            address = address,
            createdAt = null,
            updatedAt = null
        )
    }

    private fun generateTraceCode(): String {
        val raw = "${System.currentTimeMillis()}-${Math.random()}"
        val md = java.security.MessageDigest.getInstance("MD5")
        val hash = md.digest(raw.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }.substring(0, 8)
    }
}