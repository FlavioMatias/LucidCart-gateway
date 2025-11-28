package com.OrderService.utils

import com.OrderService.service.JwtService
import org.springframework.stereotype.Component
import org.springframework.ws.context.MessageContext
import org.springframework.ws.server.EndpointInterceptor
import org.springframework.ws.soap.SoapHeader
import org.springframework.ws.soap.SoapMessage
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

data class UserContext(val userId: Long)

object SecurityContextHolder {
    private val context = ThreadLocal<UserContext>()
    fun setContext(userContext: UserContext) = context.set(userContext)
    fun getContext(): UserContext = context.get() ?: throw RuntimeException("User not authenticated")
    fun clear() = context.remove()
}

@Component
class JwtInterceptor(
    private val jwtService: JwtService
) : EndpointInterceptor {

    override fun handleRequest(request: MessageContext, endpoint: Any): Boolean {
        val soapMessage = request.request as? SoapMessage
            ?: throw RuntimeException("Expected SOAP message")

        val soapHeader: SoapHeader? = soapMessage.soapHeader

        // Tenta pegar do SOAP Header
        var authHeader: String? = soapHeader?.examineAllHeaderElements()
            ?.asSequence()
            ?.firstOrNull { it.name.localPart == "Authorization" }
            ?.text

        // Se não tiver no SOAP Header, tenta pegar do HTTP Header
        if (authHeader.isNullOrEmpty()) {
            val servletRequest = (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)?.request
            authHeader = servletRequest?.getHeader("Authorization")
        }

        if (authHeader.isNullOrEmpty()) {
            throw RuntimeException("Authorization header missing")
        }

        // Valida token e pega userId
        val userId = jwtService.validateAndGetUserId(authHeader)
            ?: throw RuntimeException("Invalid or expired token")

        SecurityContextHolder.setContext(UserContext(userId))
        return true
    }

    override fun handleResponse(request: MessageContext?, endpoint: Any?) = true
    override fun handleFault(request: MessageContext?, endpoint: Any?) = true

    override fun afterCompletion(messageContext: MessageContext?, endpoint: Any?, ex: Exception?) {
        SecurityContextHolder.clear()
    }
}
