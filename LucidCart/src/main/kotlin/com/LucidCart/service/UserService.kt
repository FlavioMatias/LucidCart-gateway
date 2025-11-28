package com.LucidCart.service
import com.LucidCart.gateway.user.*
import org.springframework.stereotype.Service

@Service
class UserService(
    private val gateway: UserGateway
) {

    // ---------- AUTH ----------

    fun signup(email: String, password: String): UserResponse {
        val dto = SignUpDTO(email, password)
        return gateway.signup(dto)
    }

    fun login(email: String, password: String): String {
        val dto = SignInDTO(email, password)
        val response = gateway.signin(dto)
        return response.token
    }

    // ---------- PROFILE ----------

    fun uploadProfilePhoto(userId: Long, token: String, fileBytes: ByteArray, filename: String): String {
        return gateway.uploadProfilePhoto(token, fileBytes, filename)
    }
}
