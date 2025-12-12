package com.OrderService.utils

import com.OrderService.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {

            val path = request.requestURI

            // Bypass para Swagger, docs e login
            if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/login")) {
                chain.doFilter(request, response)
                return
            }

            val authHeader = request.getHeader("Authorization")
            if (authHeader.isNullOrBlank()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header missing")
                return
            }

            val userId = try {
                jwtService.validateAndGetUserId(authHeader)
            } catch (ex: Exception) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token")
                return
            }

            if (userId == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token")
                return
            }

            // Popula o Spring Security context
            val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
            val authentication = UsernamePasswordAuthenticationToken(userId, null, authorities)
            SecurityContextHolder.getContext().authentication = authentication

            chain.doFilter(request, response)

    }
}
