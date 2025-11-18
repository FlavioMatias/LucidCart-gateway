package com.flavex.LucidCart.controller

import com.flavex.LucidCart.service.ProductsService
import com.flavex.LucidCart.utils.dto.ProductSimpleResponseDTO
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/product")
class ProductController(
    private val service: ProductsService
) {

    @GetMapping
    fun findAll(): CollectionModel<EntityModel<ProductSimpleResponseDTO>> {
        val products = service.findAll()

        val productModels = products.map { dto ->
            EntityModel.of(
                dto,
                linkTo(methodOn(ProductController::class.java).findById(dto.id.toInt())).withSelfRel(),
                linkTo(methodOn(ProductController::class.java).delete(dto.id.toInt())).withRel("delete"),
                linkTo(methodOn(ProductController::class.java).update(dto.id.toInt(), dto)).withRel("update")
            )
        }

        return CollectionModel.of(
            productModels,
            linkTo(methodOn(ProductController::class.java).findAll()).withSelfRel()
        )
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): EntityModel<ProductSimpleResponseDTO> {
        val dto = service.findById(id)
            ?: throw RuntimeException("Product not found")

        return EntityModel.of(
            dto,
            linkTo(methodOn(ProductController::class.java).findById(id)).withSelfRel(),
            linkTo(methodOn(ProductController::class.java).findAll()).withRel("all-products")
        )
    }

    @PostMapping
    fun create(@RequestBody dto: ProductSimpleResponseDTO): EntityModel<ProductSimpleResponseDTO> {
        val saved = service.create(dto)

        return EntityModel.of(
            saved,
            linkTo(methodOn(ProductController::class.java).findById(saved.id.toInt())).withSelfRel()
        )
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Int, @RequestBody dto: ProductSimpleResponseDTO): EntityModel<ProductSimpleResponseDTO> {
        val updated = service.update(id, dto)
            ?: throw RuntimeException("Product not found")

        return EntityModel.of(
            updated,
            linkTo(methodOn(ProductController::class.java).findById(id)).withSelfRel()
        )
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): EntityModel<String> {
        val deleted = service.delete(id)

        return EntityModel.of(
            if (deleted) "Deleted" else "Not found",
            linkTo(methodOn(ProductController::class.java).findAll()).withRel("all-products")
        )
    }
}
