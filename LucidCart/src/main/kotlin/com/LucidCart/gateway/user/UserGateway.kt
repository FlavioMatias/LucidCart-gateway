package com.LucidCart.gateway.user

import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.core.io.ByteArrayResource

@Component
class UserGateway(
    private val client: UserClientHTTP
) {

    fun signup(req: SignUpDTO): UserResponse = client.signup(req)

    fun signin(req: SignInDTO): SignInResponse = client.signin(req)

    fun uploadProfilePhoto(token: String, fileBytes: ByteArray, filename: String): String {
        val multipart = LinkedMultiValueMap<String, Any>()

        val resource = object : ByteArrayResource(fileBytes) {
            override fun getFilename(): String = filename
        }

        multipart.add("photo", resource)

        val response = client.uploadProfilePhoto("Bearer $token", multipart)
        return response["photo_url"] ?: throw IllegalStateException("photo_url missing in response")
    }
}
