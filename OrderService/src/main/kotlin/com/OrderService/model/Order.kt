package com.OrderService.model

import com.OrderService.model.enuns.OrderStatus
import jakarta.persistence.*
import java.util.Date

@Entity
@Table(name = "orders")
class Order(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var status: OrderStatus,

    @Column(nullable = false, unique = true)
    var traceCode: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    var address: Address,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    var items: MutableList<ItemOrder> = mutableListOf(),

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
}
