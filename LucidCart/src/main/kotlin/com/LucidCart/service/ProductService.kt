package com.LucidCart.service

import com.LucidCart.gateway.product.ProductGateway
import com.LucidCart.gateway.product.Product
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val gateway: ProductGateway
) {


    fun listAllProducts(): List<Product> {
        return gateway.listProducts()
    }

    fun getProductById(id: Int): Product {
        return gateway.getProductById(id)
    }
}
