package com.flavex.LucidCart.utils.mapper

import com.flavex.LucidCart.utils.dto.ProductSimpleResponseDTO
import org.springframework.stereotype.Component

@Component
class ProductMapper {

    fun toMap(dto: ProductSimpleResponseDTO): Map<String, Any> {
        return mapOf(
            "id" to dto.id,
            "title" to dto.title,
            "rating" to dto.rating,
            "pictureUrl" to dto.pictureUrl,
            "price" to dto.price
        )
    }

    fun toDto(map: Map<String, Any>): ProductSimpleResponseDTO {
        return ProductSimpleResponseDTO(
            id = (map["id"] as Number).toLong(),
            title = map["title"] as String,
            rating = (map["rating"] as Number).toLong(),
            pictureUrl = map["pictureUrl"] as String,
            price = (map["price"] as Number).toDouble()
        )
    }

    fun toDtoList(list: List<Map<String, Any>>): List<ProductSimpleResponseDTO> {
        return list.map { toDto(it) }
    }

    fun toMapList(list: List<ProductSimpleResponseDTO>): List<Map<String, Any>> {
        return list.map { toMap(it) }
    }
}
