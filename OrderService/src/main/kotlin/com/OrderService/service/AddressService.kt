package com.OrderService.service

import com.OrderService.repository.AddressRepository
import com.OrderService.utils.dto.AddressRequestDTO
import com.OrderService.utils.dto.AddressResponseDTO
import com.OrderService.utils.factory.AddressFactory
import com.OrderService.utils.mapper.AddressMapper
import org.springframework.stereotype.Service


@Service
class AddressService(
    private val addressRepo: AddressRepository,
    private val addressFactory: AddressFactory,
    private val mapper: AddressMapper
) {

    fun create(userId: Long, dto: AddressRequestDTO): AddressResponseDTO {
        if (addressRepo.findByUserId(userId) != null) {
            throw IllegalStateException("User $userId already has an address")
        }

        val address = addressFactory.create(dto, userId)
        return mapper.toDTO(addressRepo.save(address))
    }

    fun update(userId: Long, dto: AddressRequestDTO): AddressResponseDTO {
        val existing = addressRepo.findByUserId(userId)
            ?: throw IllegalStateException("User $userId has no address to update")

        existing.fullAddress = dto.fullAddress
        existing.latitude = dto.latitude
        existing.longitude = dto.longitude

        return mapper.toDTO(addressRepo.save(existing))
    }

    fun delete(userId: Long) {
        val existing = addressRepo.findByUserId(userId)
            ?: throw IllegalStateException("User $userId has no address to delete")

        addressRepo.delete(existing)
    }

    fun findAddress(userId: Long): AddressResponseDTO {
        val address = addressRepo.findByUserId(userId)
            ?: throw IllegalStateException("User $userId has no address registered")

        return mapper.toDTO(address)
    }
}
