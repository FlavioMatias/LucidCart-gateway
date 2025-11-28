package com.LucidCart.gateway.order

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange

@HttpExchange(
    url = "/ws",
    // SOAP 1.1 → text/xml; charset obrigatório
    contentType = "text/xml;charset=UTF-8",
    accept = ["text/xml", "application/soap+xml"]
)
interface OrderClientHTTP {

    @PostExchange(
        // reforço explícito do content-type, para evitar auto-negociação errada
        contentType = "text/xml;charset=UTF-8",
        accept = ["text/xml", "application/soap+xml"]
    )
    fun call(
        @RequestHeader("Authorization")
        authorization: String,

        @RequestHeader("SOAPAction")
        soapAction: String,

        @RequestBody
        body: String
    ): ResponseEntity<String>
}
