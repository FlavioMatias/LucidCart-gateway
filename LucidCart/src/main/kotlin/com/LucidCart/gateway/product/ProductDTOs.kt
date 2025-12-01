package com.LucidCart.gateway.product

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val image: String,
    val stock: Int,
    val category: String
)
