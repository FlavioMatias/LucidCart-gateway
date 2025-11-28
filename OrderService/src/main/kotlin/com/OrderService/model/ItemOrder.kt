package com.OrderService.model

import jakarta.persistence.*
import java.util.Date

@Entity
@Table(name = "item_order")
class ItemOrder(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var userId: Long,

    @Column(nullable = false)
    var productId: Long,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    var order: Order,

    @Column(nullable = false)
    var quantity: Long,

    @Column(nullable = false)
    var unitPrice: Double,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    var createdAt: Date? = null,

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    var updatedAt: Date? = null

) {

    @PrePersist
    fun onCreate() {
        val now = Date()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    fun onUpdate() {
        updatedAt = Date()
    }

    fun subtotal(): Double {
        return quantity * unitPrice
    }
}
