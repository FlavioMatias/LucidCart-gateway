package com.LucidCart.controller

import com.LucidCart.controller.assemblers.*
import com.LucidCart.gateway.order.AddressRequestDTO
import com.LucidCart.service.OrderService
import org.springframework.hateoas.EntityModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/address")
class AddressController(
    private val orderService: OrderService,
    private val createAssembler: CreateAddressResponseAssembler,
    private val updateAssembler: UpdateAddressResponseAssembler,
    private val findAssembler: FindAddressResponseAssembler
) {

    @PostMapping
    fun createAddress(
        @RequestHeader("Authorization") token: String,
        @RequestBody req: AddressRequestDTO
    ): EntityModel<*> {
        val res = orderService.createAddress(req, token)
        return createAssembler.toModel(res)
    }

    @PutMapping
    fun updateAddress(
        @RequestHeader("Authorization") token: String,
        @RequestBody req: AddressRequestDTO
    ): EntityModel<*> {
        val res = orderService.updateAddress(req, token)
        return updateAssembler.toModel(res)
    }

    @DeleteMapping
    fun deleteAddress(
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<Void> {
        orderService.deleteAddress(token)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun findAddress(
        @RequestHeader("Authorization") token: String
    ): EntityModel<*> {
        val res = orderService.findAddress(token)
        return findAssembler.toModel(res)
    }
}
