package com.LucidCart.gateway.user

import org.springframework.web.service.annotation.PostExchange
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader


interface UserClientHTTP {

    @PostExchange(
        "/api/v1/auth/signup",
        headers = ["Content-Type=application/json", "Accept=application/json"]
    )
    fun signup(@RequestBody req: SignUpDTO): UserResponse

    @PostExchange(
        "/api/v1/auth/signin",
        headers = ["Content-Type=application/json", "Accept=application/json"]
    )
    fun signin(@RequestBody req: SignInDTO): SignInResponse

    @PostExchange(
        "/api/v1/profile/photo",
        headers = ["Content-Type=multipart/form-data", "Accept=application/json"]
    )
    fun uploadProfilePhoto(
        @RequestHeader("Authorization") token: String,
        @RequestBody multipart: MultiValueMap<String, Any>
    ): Map<String, String>
}
