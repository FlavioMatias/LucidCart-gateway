package com.LucidCart.gateway.user
import com.fasterxml.jackson.annotation.JsonProperty

data class SignUpDTO(
    @JsonProperty("Email") val email: String,
    @JsonProperty("Password") val password: String
)

data class SignInDTO(
    @JsonProperty("Email") val email: String,
    @JsonProperty("Password") val password: String
)

data class SignInResponse(
    val token: String
)

data class UserResponse(
    val id: Long?,
    val email: String?
)