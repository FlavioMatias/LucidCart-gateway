package com.OrderService.utils.factory

import com.OrderService.model.Address
import com.OrderService.utils.dto.AddressRequestDTO
import org.springframework.stereotype.Component

@Component
class AddressFactory {

    fun create(dto: AddressRequestDTO, userId: Long): Address {
        return Address(
            id = null,
            userId = userId,
            fullAddress = dto.fullAddress,
            latitude = dto.latitude,
            longitude = dto.longitude,
            createdAt = null,
            updatedAt = null
        )
    }
}