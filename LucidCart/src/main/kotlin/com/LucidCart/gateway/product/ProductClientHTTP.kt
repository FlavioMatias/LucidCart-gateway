package com.LucidCart.gateway.product

import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.bind.annotation.PathVariable

interface ProductClientHTTP {

    @GetExchange("/api/v1/products")
    fun list(): List<Product>

    @GetExchange("/api/v1/products/{id}")
    fun getById(@PathVariable id: Int): Product
}