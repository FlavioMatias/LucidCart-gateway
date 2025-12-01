package com.LucidCart.gateway.product

import org.springframework.stereotype.Component

@Component
class ProductGateway(
    private val client: ProductClientHTTP
) {

    fun listProducts(): List<Product> = client.list()

    fun getProductById(id: Int): Product = client.getById(id)
}