package com.LucidCart.controller

import com.LucidCart.controller.assemblers.*
import com.LucidCart.gateway.order.AddressRequestDTO
import com.LucidCart.service.OrderService
import org.springframework.hateoas.EntityModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses

@Tag(name = "Address", description = "Gerenciamento de endereços de entrega")
@RestController
@RequestMapping("/api/v1/address")
class AddressController(
    private val orderService: OrderService,
    private val createAssembler: CreateAddressResponseAssembler,
    private val updateAssembler: UpdateAddressResponseAssembler,
    private val findAssembler: FindAddressResponseAssembler
) {

    @Operation(summary = "Criar endereço", description = "Cria um novo endereço de entrega para o usuário autenticado.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Endereço criado com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados inválidos"),
            ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
        ]
    )
    @PostMapping
    fun createAddress(
        @Parameter(description = "Token JWT do usuário", required = true)
        @RequestHeader("Authorization") token: String,
        @Parameter(description = "Dados do endereço a ser criado", required = true)
        @RequestBody req: AddressRequestDTO
    ): EntityModel<*> {
        val res = orderService.createAddress(req, token)
        return createAssembler.toModel(res)
    }

    @Operation(summary = "Atualizar endereço", description = "Atualiza o endereço de entrega do usuário autenticado.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso"),
            ApiResponse(responseCode = "400", description = "Dados inválidos"),
            ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
        ]
    )
    @PutMapping
    fun updateAddress(
        @Parameter(description = "Token JWT do usuário", required = true)
        @RequestHeader("Authorization") token: String,
        @Parameter(description = "Dados do endereço a ser atualizado", required = true)
        @RequestBody req: AddressRequestDTO
    ): EntityModel<*> {
        val res = orderService.updateAddress(req, token)
        return updateAssembler.toModel(res)
    }

    @Operation(summary = "Excluir endereço", description = "Exclui o endereço de entrega do usuário autenticado.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Endereço excluído com sucesso"),
            ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
        ]
    )
    @DeleteMapping
    fun deleteAddress(
        @Parameter(description = "Token JWT do usuário", required = true)
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<Void> {
        orderService.deleteAddress(token)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Buscar endereço", description = "Retorna o endereço de entrega do usuário autenticado.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Endereço encontrado com sucesso"),
            ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
            ApiResponse(responseCode = "401", description = "Token inválido ou ausente")
        ]
    )
    @GetMapping
    fun findAddress(
        @Parameter(description = "Token JWT do usuário", required = true)
        @RequestHeader("Authorization") token: String
    ): EntityModel<*> {
        val res = orderService.findAddress(token)
        return findAssembler.toModel(res)
    }
}
