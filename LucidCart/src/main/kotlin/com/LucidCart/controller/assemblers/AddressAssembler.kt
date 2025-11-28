package com.LucidCart.controller.assemblers

import com.LucidCart.controller.AddressController
import com.LucidCart.controller.OrderController
import com.LucidCart.gateway.order.*
import com.LucidCart.gateway.order.jabx.*
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*
import org.springframework.stereotype.Component

// ==================== ADDRESS ASSEMBLERS ====================

@Component
class CreateAddressResponseAssembler :
    RepresentationModelAssembler<CreateAddressResponseDTO, EntityModel<CreateAddressResponseDTO>> {

    override fun toModel(res: CreateAddressResponseDTO): EntityModel<CreateAddressResponseDTO> =
        EntityModel.of(
            res,
            linkTo(methodOn(AddressController::class.java).findAddress("TOKEN")).withSelfRel(),
            linkTo(methodOn(AddressController::class.java).updateAddress("TOKEN", AddressRequestDTO("fulladdress", 3.3,3.3))).withRel("update"),
            linkTo(methodOn(AddressController::class.java).deleteAddress("TOKEN")).withRel("delete")
        )
}

@Component
class UpdateAddressResponseAssembler :
    RepresentationModelAssembler<UpdateAddressResponseDTO, EntityModel<UpdateAddressResponseDTO>> {

    override fun toModel(res: UpdateAddressResponseDTO): EntityModel<UpdateAddressResponseDTO> =
        EntityModel.of(
            res,
            linkTo(methodOn(AddressController::class.java).findAddress("TOKEN")).withSelfRel(),
            linkTo(methodOn(AddressController::class.java).deleteAddress("TOKEN")).withRel("delete"),
            linkTo(methodOn(AddressController::class.java).updateAddress("TOKEN", AddressRequestDTO("fulladdress", 3.3,3.3))).withRel("update")
        )
}

@Component
class DeleteAddressResponseAssembler :
    RepresentationModelAssembler<DeleteAddressResponse, EntityModel<DeleteAddressResponse>> {

    override fun toModel(res: DeleteAddressResponse): EntityModel<DeleteAddressResponse> =
        EntityModel.of(
            res,
            linkTo(methodOn(AddressController::class.java).createAddress("TOKEN", AddressRequestDTO("fulladdress", 3.3,3.3))).withRel("create")
        )
}

@Component
class FindAddressResponseAssembler :
    RepresentationModelAssembler<FindAddressResponseDTO, EntityModel<FindAddressResponseDTO>> {

    override fun toModel(res: FindAddressResponseDTO): EntityModel<FindAddressResponseDTO> =
        EntityModel.of(
            res,
            linkTo(methodOn(AddressController::class.java).findAddress("TOKEN")).withSelfRel(),
            linkTo(methodOn(AddressController::class.java).updateAddress("TOKEN", AddressRequestDTO("fulladdress", 3.3,3.3))).withRel("update"),
            linkTo(methodOn(AddressController::class.java).deleteAddress("TOKEN")).withRel("delete")
        )
}
