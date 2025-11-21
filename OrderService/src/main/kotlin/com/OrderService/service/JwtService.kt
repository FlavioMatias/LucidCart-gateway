package com.OrderService.service

import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

@Service
class JwtService(
    @Value("\${jwt.public-key}") private val publicKeyString: String
) {

    private val publicKey: PublicKey by lazy { loadPublicKey(publicKeyString) }

    fun validateAndGetUserId(token: String): Long? {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .body

            claims["userId"]?.toString()?.toLong()
        } catch (e: Exception) {
            null
        }
    }

    private fun loadPublicKey(keyStr: String): PublicKey {
        val clean = keyStr
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("\\s".toRegex(), "")
        val keyBytes = Base64.getDecoder().decode(clean)
        val spec = X509EncodedKeySpec(keyBytes)
        return KeyFactory.getInstance("RSA").generatePublic(spec)
    }
}
