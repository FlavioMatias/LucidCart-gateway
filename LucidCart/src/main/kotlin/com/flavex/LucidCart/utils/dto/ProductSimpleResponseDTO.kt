package com.flavex.LucidCart.utils.dto

import org.springframework.hateoas.RepresentationModel

class ProductSimpleResponseDTO(
    var id         : Long,
    var title      : String,
    var rating     : Long,
    var pictureUrl : String,
    var price      : Double
) : RepresentationModel<ProductSimpleResponseDTO>() {}