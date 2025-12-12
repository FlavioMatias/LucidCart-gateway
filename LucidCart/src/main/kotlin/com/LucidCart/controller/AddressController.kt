package com.LucidCart.controller

import com.LucidCart.gateway.order.AddressRequestDTO
import com.LucidCart.gateway.order.AddressResponseDTO
import com.LucidCart.service.OrderService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Address", description = "Delivery address management")
@RestController
@RequestMapping("/api/v1/address")
class AddressController(
    private val orderService: OrderService
) {

    @Operation(summary = "Create address", description = "Creates a new delivery address for the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Address created successfully"),
            ApiResponse(responseCode = "400", description = "Invalid data"),
            ApiResponse(responseCode = "401", description = "Invalid or missing token")
        ]
    )
    @PostMapping
    fun createAddress(
        @Parameter(hidden = true)
        @RequestHeader("Authorization") token: String,
        @RequestBody req: AddressRequestDTO
    ): AddressResponseDTO {
        return orderService.createAddress(token, req)
    }

    @Operation(summary = "Update address", description = "Updates the delivery address of the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Address updated successfully"),
            ApiResponse(responseCode = "400", description = "Invalid data"),
            ApiResponse(responseCode = "401", description = "Invalid or missing token")
        ]
    )
    @PutMapping
    fun updateAddress(
        @Parameter(hidden = true)
        @RequestHeader("Authorization") token: String,
        @RequestBody req: AddressRequestDTO
    ): AddressResponseDTO {
        return orderService.updateAddress(token, req)
    }

    @Operation(summary = "Delete address", description = "Deletes the delivery address of the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Address deleted successfully"),
            ApiResponse(responseCode = "401", description = "Invalid or missing token")
        ]
    )
    @DeleteMapping
    fun deleteAddress(
        @Parameter(hidden = true)
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<Void> {
        orderService.deleteAddress(token)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Get address", description = "Returns the delivery address of the authenticated user.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Address retrieved successfully"),
            ApiResponse(responseCode = "404", description = "Address not found"),
            ApiResponse(responseCode = "401", description = "Invalid or missing token")
        ]
    )
    @GetMapping
    fun findAddress(
        @Parameter(hidden = true)
        @RequestHeader("Authorization") token: String
    ): AddressResponseDTO {
        return orderService.findAddress(token)
    }
}
