package com.flavex.LucidCart.gateway

import org.springframework.stereotype.Component

@Component
class ProductGateway {
    private val products = mutableListOf<Map<String, Any>>(
        mapOf(
            "id" to 1,
            "title" to "Notebook Gamer X15",
            "rating" to 5,
            "pictureUrl" to "https://cdn.exemplo.com/notebook-x15.jpg",
            "price" to 4999.99
        ),
        mapOf(
            "id" to 2,
            "title" to "Smartphone Aurora Z",
            "rating" to 4,
            "pictureUrl" to "https://cdn.exemplo.com/aurora-z.jpg",
            "price" to 2599.00
        ),
        mapOf(
            "id" to 3,
            "title" to "Fone Bluetooth Wave Pro",
            "rating" to 4,
            "pictureUrl" to "https://cdn.exemplo.com/wave-pro.jpg",
            "price" to 399.90
        ),
        mapOf(
            "id" to 4,
            "title" to "Monitor UltraWide Vision 34''",
            "rating" to 5,
            "pictureUrl" to "https://cdn.exemplo.com/vision34.jpg",
            "price" to 2899.00
        ),
        mapOf(
            "id" to 5,
            "title" to "Teclado Mecânico IronKeys",
            "rating" to 4,
            "pictureUrl" to "https://cdn.exemplo.com/ironkeys.jpg",
            "price" to 499.50
        ),
        mapOf(
            "id" to 6,
            "title" to "Mouse Gamer Falcon RGB",
            "rating" to 5,
            "pictureUrl" to "https://cdn.exemplo.com/falcon-rgb.jpg",
            "price" to 229.90
        ),
        mapOf(
            "id" to 7,
            "title" to "Cadeira Ergonômica Zenith Pro",
            "rating" to 4,
            "pictureUrl" to "https://cdn.exemplo.com/zenith-pro.jpg",
            "price" to 1299.00
        ),
        mapOf(
            "id" to 8,
            "title" to "Webcam HD StreamX",
            "rating" to 3,
            "pictureUrl" to "https://cdn.exemplo.com/streamx.jpg",
            "price" to 169.90
        ),
        mapOf(
            "id" to 9,
            "title" to "SSD NVMe 1TB UltraSpeed",
            "rating" to 5,
            "pictureUrl" to "https://cdn.exemplo.com/ultraspeed1tb.jpg",
            "price" to 549.00
        )
    )

    fun findAll(): List<Map<String, Any>> {
        return products.toList()
    }

    fun findById(id: Int): Map<String, Any>? {
        return products.find { it["id"] == id }
    }

    fun create(product: Map<String, Any>): Map<String, Any> {
        products.add(product)
        return product
    }

    fun update(id: Int, updated: Map<String, Any>): Map<String, Any>? {
        val index = products.indexOfFirst { it["id"] == id }

        if (index == -1) return null

        val newProduct = updated.toMutableMap().also { it["id"] = id }
        products[index] = newProduct

        return newProduct
    }

    fun delete(id: Int): Boolean {
        return products.removeIf { it["id"] == id }
    }
}
