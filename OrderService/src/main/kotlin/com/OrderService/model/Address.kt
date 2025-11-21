package com.OrderService.model

import jakarta.persistence.*
import java.util.Date

@Entity
@Table(name = "address")
class Address(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true)
    var userId: Long,

    @Column(nullable = false)
    var fullAddress: String,

    @Column(nullable = false)
    var latitude: Double,

    @Column(nullable = false)
    var longitude: Double,

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