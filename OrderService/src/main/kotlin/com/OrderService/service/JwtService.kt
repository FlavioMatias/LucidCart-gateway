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
        logger.info("Original token received: '{}'", token)

        try {
            if (token.isBlank()) {
                logger.error("Received empty token")
                return null
            }

            // Remove o prefixo Bearer de forma robusta
            val rawToken = if (token.startsWith("Bearer ", ignoreCase = true)) {
                token.substring(7).trim()
            } else {
                token.trim()
            }
            logger.info("Token after removing 'Bearer': '{}'", rawToken.take(500))

            // Estrutura do JWT: header.payload.signature
            val parts = rawToken.split(".")
            if (parts.size != 3) {
                logger.error("Token does not have 3 parts (header.payload.signature). Parts count: {}", parts.size)
                parts.forEachIndexed { i, part -> logger.debug("Part[{}] length={}", i, part.length) }
                return null
            }

            // Decodifica header e payload para inspeção
            val headerJson = try { String(Base64.getUrlDecoder().decode(parts[0])) } catch (e: Exception) {
                logger.warn("Failed to decode JWT header", e); null
            }
            val payloadJson = try { String(Base64.getUrlDecoder().decode(parts[1])) } catch (e: Exception) {
                logger.warn("Failed to decode JWT payload", e); null
            }
            logger.info("Decoded JWT header: {}", headerJson)
            logger.info("Decoded JWT payload (truncated 500 chars): {}", payloadJson?.take(500))

            // Parse e valida assinatura + claims
            val claims = try {
                Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(rawToken)
                    .body
            } catch (e: Exception) {
                logger.error("Failed to parse JWT or invalid signature: {}", e.message)
                return null
            }

            // Logs detalhados de claims
            val subject = claims.subject
            val expiration: Date? = claims.expiration
            val issuedAt: Date? = claims.issuedAt

            logger.info("JWT parsed successfully. subject='{}', expiration='{}', issuedAt='{}'", subject, expiration, issuedAt)
            logger.debug("All claims keys: {}", claims.keys.joinToString(", "))

            claims.forEach { (k, v) ->
                val valueStr = when (v) {
                    null -> "null"
                    is String -> if (v.length > 300) "${v.take(300)}...(truncated ${v.length} chars)" else v
                    else -> v.toString()
                }
                logger.debug("claim '{}': {}", k, valueStr)
            }

            // Valida expiração
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

            // Converte subject para userId
            val userId = subject?.toLongOrNull()
            if (userId == null) {
                logger.error("Subject is null or not a number: subject='{}'", subject)
            } else {
                logger.info("Resolved userId = {}", userId)
            }

            logger.info("Returning userId: {}", userId)
            return userId

        } catch (jwtEx: JwtException) {
            logger.error("JWT exception while validating token: ${jwtEx.message}", jwtEx)
            return null
        } catch (ex: Exception) {
            logger.error("Unexpected error while validating token: ${ex.message}", ex)
            return null
        }
    }

    private fun loadPublicKey(keyStr: String): PublicKey {
        logger.info("Loading public key...")
        val clean = keyStr
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("\\s".toRegex(), "")
        logger.debug("Public key base64 cleaned: '{}'", clean.take(50) + "...")
        val keyBytes = Base64.getDecoder().decode(clean)
        val spec = X509EncodedKeySpec(keyBytes)
        val key = KeyFactory.getInstance("RSA").generatePublic(spec)
        logger.info("Public key loaded successfully")
        return key
    }
}
