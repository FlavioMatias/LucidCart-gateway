package com.OrderService.utils.mapper

import com.OrderService.model.Address
import com.OrderService.utils.dto.AddressResponseDTO
import org.springframework.stereotype.Component

@Component
class AddressMapper {

    fun toDTO(entity: Address): AddressResponseDTO {
        return AddressResponseDTO(
            id = entity.id,
            userId = entity.userId,
            fullAddress = entity.fullAddress,
            latitude = entity.latitude,
            longitude = entity.longitude,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
}