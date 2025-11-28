package com.OrderService.utils.dto

data class AddressRequestDTO(
    var fullAddress: String,
    var latitude: Double,
    var longitude: Double,
)