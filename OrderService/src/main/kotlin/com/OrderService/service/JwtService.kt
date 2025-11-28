package com.OrderService.service

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import java.util.Date

@Service
class JwtService(
    @Value("\${jwt.public-key}") private val publicKeyString: String
) {

    private val logger = LoggerFactory.getLogger(JwtService::class.java)

    private val publicKey: PublicKey by lazy { loadPublicKey(publicKeyString) }

    fun validateAndGetUserId(token: String): Long? {
        logger.info("validateAndGetUserId called")

        try {
            if (token.isBlank()) {
                logger.error("Received empty token")
                return null
            }

            logger.debug("Raw token (trimmed to 500 chars): ${token.take(500)}")
            val hasBearerPrefix = token.startsWith("Bearer ", ignoreCase = true)
            logger.debug("Starts with 'Bearer '? {}", hasBearerPrefix)

            val rawToken = token.removePrefix("Bearer").trim()
            logger.debug("Trimmed token length: {}", rawToken.length)

            // quick structural check
            val parts = rawToken.split(".")
            if (parts.size != 3) {
                logger.error("Token does not have 3 parts (header.payload.signature). Parts: {}", parts.size)
                logger.debug("Token parts sizes: header=${parts.getOrNull(0)?.length ?: 0}, payload=${parts.getOrNull(1)?.length ?: 0}, signature=${parts.getOrNull(2)?.length ?: 0}")
                return null
            }

            // try to Base64Url decode header & payload for inspection (safe for debugging)
            try {
                val headerJson = String(Base64.getUrlDecoder().decode(parts[0]))
                val payloadJson = String(Base64.getUrlDecoder().decode(parts[1]))
                logger.debug("JWT header JSON: {}", headerJson)
                logger.debug("JWT payload JSON: {}", payloadJson.take(2000)) // limit length
            } catch (decEx: Exception) {
                logger.warn("Failed to Base64Url-decode JWT header/payload for inspection", decEx)
            }

            // Parse and validate signature + expiry using public key
            val claims = Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(rawToken)
                .body

            // Log important claim info
            val subject = claims.subject
            val expiration: Date? = claims.expiration
            val issuedAt: Date? = claims.issuedAt

            logger.info("JWT parsed successfully. subject='{}', expiration='{}', issuedAt='{}'", subject, expiration, issuedAt)
            logger.debug("All claims keys: {}", claims.keys.joinToString(", "))

            // Dump claims values in debug (but mask long values)
            try {
                claims.forEach { (k, v) ->
                    val valueStr = when (v) {
                        null -> "null"
                        is String -> if (v.length > 300) "${v.take(300)}...(truncated ${v.length} chars)" else v
                        else -> v.toString()
                    }
                    logger.debug("claim '{}': {}", k, valueStr)
                }
            } catch (e: Exception) {
                logger.warn("Could not iterate claims safely", e)
            }

            // check expiration explicitly
            if (expiration != null) {
                val now = Date()
                val expired = expiration.before(now)
                logger.debug("Token expiration check: now='{}', expiration='{}', expired='{}'", now, expiration, expired)
                if (expired) {
                    logger.warn("Token is expired according to 'exp' claim")
                    return null
                }
            } else {
                logger.warn("No 'exp' claim present in token")
            }

            // subject -> user id
            val userId = subject?.toLongOrNull()
            if (userId == null) {
                logger.error("Subject is null or not a number: subject='{}'", subject)
            } else {
                logger.info("Resolved userId = {}", userId)
            }

            return userId
        } catch (jwtEx: JwtException) {
            // Signature, malformed, unsupported, expired exceptions extend JwtException
            logger.error("JWT exception while validating token: ${jwtEx.message}", jwtEx)
            return null
        } catch (ex: Exception) {
            logger.error("Unexpected error while validating token: ${ex.message}", ex)
            return null
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
