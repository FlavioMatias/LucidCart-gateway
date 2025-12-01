package com.LucidCart.controller

import com.LucidCart.service.ProductService
import com.LucidCart.gateway.product.Product
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@Tag(name = "Products", description = "Gerenciamento de produtos da loja")
@RestController
@RequestMapping("/api/v1/products")
class ProductController(private val productService: ProductService) {

    @Operation(summary = "Lista todos os produtos", description = "Retorna uma lista completa de produtos disponíveis na loja com links HATEOAS para cada produto.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
        ]
    )
    @GetMapping()
    fun listAll(): List<EntityModel<Product>> {
        return productService.listAllProducts().map { product ->
            val model = EntityModel.of(product)
            model.add(
                linkTo(methodOn(ProductController::class.java).getById(product.id)).withSelfRel(),
                linkTo(methodOn(ProductController::class.java).listAll()).withRel("all_products")
            )
            model
        }
    }

    @Operation(summary = "Busca um produto por ID", description = "Retorna um produto específico identificado pelo seu ID com links HATEOAS.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso"),
            ApiResponse(responseCode = "404", description = "Produto não encontrado")
        ]
    )
    @GetMapping("/{id}")
    fun getById(
        @Parameter(description = "ID do produto que deseja consultar", required = true)
        @PathVariable id: Int
    ): EntityModel<Product> {
        val product = productService.getProductById(id)
        val model = EntityModel.of(product)
        model.add(
            linkTo(methodOn(ProductController::class.java).getById(id)).withSelfRel(),
            linkTo(methodOn(ProductController::class.java).listAll()).withRel("all_products")
        )
        return model
    }
}
