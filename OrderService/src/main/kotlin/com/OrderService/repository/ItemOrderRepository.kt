package com.OrderService.repository

import com.OrderService.model.ItemOrder
import org.springframework.data.jpa.repository.JpaRepository

interface ItemOrderRepository : JpaRepository <ItemOrder, Long>