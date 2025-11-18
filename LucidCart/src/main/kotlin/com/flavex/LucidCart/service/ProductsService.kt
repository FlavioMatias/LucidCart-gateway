package com.flavex.LucidCart.service

import com.flavex.LucidCart.gateway.ProductGateway
import com.flavex.LucidCart.utils.dto.ProductSimpleResponseDTO
import com.flavex.LucidCart.utils.mapper.ProductMapper
import org.springframework.stereotype.Service

@Service
class ProductsService(
    private val gateway: ProductGateway,
    private val mapper: ProductMapper
) {

    fun findAll(): List<ProductSimpleResponseDTO> {
        val data = gateway.findAll()
        return mapper.toDtoList(data)
    }

    fun findById(id: Int): ProductSimpleResponseDTO? {
        val product = gateway.findById(id) ?: return null
        return mapper.toDto(product)
    }

    fun create(dto: ProductSimpleResponseDTO): ProductSimpleResponseDTO {
        val map = mapper.toMap(dto)
        val saved = gateway.create(map)
        return mapper.toDto(saved)
    }

    fun update(id: Int, dto: ProductSimpleResponseDTO): ProductSimpleResponseDTO? {
        val map = mapper.toMap(dto)
        val updated = gateway.update(id, map) ?: return null
        return mapper.toDto(updated)
    }

    fun delete(id: Int): Boolean {
        return gateway.delete(id)
    }
}
