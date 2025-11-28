package com.OrderService.utils.dto

import java.util.Date

data class AddressResponseDTO(
    var id: Long? = null,
    var userId: Long,
    var fullAddress: String,
    var latitude: Double,
    var longitude: Double,
    var createdAt: Date? = null,
    var updatedAt: Date? = null
)
