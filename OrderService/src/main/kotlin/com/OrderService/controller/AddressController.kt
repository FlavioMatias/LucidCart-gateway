package com.OrderService.endpoint

import com.OrderService.service.AddressService
import com.OrderService.utils.dto.AddressRequestDTO
import com.OrderService.utils.dto.AddressResponseDTO
import org.springframework.web.bind.annotation.*
import com.OrderService.utils.*
import org.springframework.security.core.context.SecurityContextHolder

@RestController
@RequestMapping("/api/v1/address")
class AddressController(
    private val addressService: AddressService
) {

    fun getCurrentUserId(): Long {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw RuntimeException("Usuário não autenticado")

        val principal = authentication.principal
        return when (principal) {
            is Long -> principal
            is String -> principal.toLongOrNull()
                ?: throw RuntimeException("Principal não é um número válido")
            else -> throw RuntimeException("Principal do token inválido")
        }
    }
    @PostMapping
    fun create(@RequestBody req: AddressRequestDTO): AddressResponseDTO {
        val userId = getCurrentUserId()
        return addressService.create(userId, req)
    }

    @PutMapping
    fun update(@RequestBody req: AddressRequestDTO): AddressResponseDTO {
        val userId = getCurrentUserId()
        return addressService.update(userId, req)
    }

    @DeleteMapping
    fun delete() {
        val userId = getCurrentUserId()
        addressService.delete(userId)
    }

    @GetMapping
    fun find(): AddressResponseDTO {
        val userId = getCurrentUserId()
        return addressService.findAddress(userId)
    }
}
