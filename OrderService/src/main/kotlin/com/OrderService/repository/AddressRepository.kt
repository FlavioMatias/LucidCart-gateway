package com.OrderService.repository

import com.OrderService.model.Address
import org.springframework.data.jpa.repository.JpaRepository

interface AddressRepository : JpaRepository<Address, Long> {
    fun findByUserId(userId: Long): Address?
}