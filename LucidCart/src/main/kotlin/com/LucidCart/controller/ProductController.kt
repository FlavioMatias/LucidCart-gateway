package com.LucidCart.controller

import com.LucidCart.gateway.product.Product
import com.LucidCart.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*

@Tag(name = "Products", description = "Store product management")
@RestController
@RequestMapping("/api/v1/products")
class ProductController(private val productService: ProductService) {

    @Operation(summary = "List all products", description = "Returns a complete list of available products in the store.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "List returned successfully")
        ]
    )
    @GetMapping
    fun listAll(): List<Product> {
        return productService.listAllProducts()
    }

    @Operation(summary = "Get product by ID", description = "Returns a specific product identified by its ID.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            ApiResponse(responseCode = "404", description = "Product not found")
        ]
    )
    @GetMapping("/{id}")
    fun getById(
        @Parameter(description = "ID of the product to retrieve", required = true)
        @PathVariable id: Int
    ): Product {
        return productService.getProductById(id)
    }
}
