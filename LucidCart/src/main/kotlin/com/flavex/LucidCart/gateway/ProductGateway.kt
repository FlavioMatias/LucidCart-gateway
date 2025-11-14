package com.flavex.LucidCart.gateway;

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Component
class ProductGateway(
    private val webClient: WebClient = WebClient.builder().build()
) {

    private val baseUrl = "https://external-api.com/products"


}