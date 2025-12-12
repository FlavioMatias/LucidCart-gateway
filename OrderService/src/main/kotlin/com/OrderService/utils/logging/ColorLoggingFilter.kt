package com.OrderService.utils.logging

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.json.JSONObject
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class ColorLoggingFilter : OncePerRequestFilter() {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val wrappedRequest = if (request.method != "GET" && request.method != "DELETE") {
            ContentCachingRequestWrapper(request)
        } else request

        val start = System.currentTimeMillis()
        filterChain.doFilter(wrappedRequest, response)
        val duration = System.currentTimeMillis() - start

        val ip = request.getHeader("X-Forwarded-For")?.takeIf { it.isNotBlank() } ?: request.remoteAddr
        val method = request.method
        val uri = request.requestURI
        val status = response.status
        val timestamp = LocalDateTime.now().format(dateTimeFormatter)
        val statusText = getHttpStatusText(status)

        val methodColor = when (method) {
            "GET" -> "\u001B[32m"
            "POST" -> "\u001B[34m"
            "PUT" -> "\u001B[33m"
            "DELETE" -> "\u001B[31m"
            else -> "\u001B[35m"
        }

        val statusColor = when (status / 100) {
            2 -> "\u001B[32m"
            3 -> "\u001B[36m"
            4, 5 -> "\u001B[31m"
            else -> "\u001B[0m"
        }

        val log = String.format(
            "%s [\u001B[90m%s\u001B[0m] : %s%-6s\u001B[0m %-40s %s%d %s\u001B[0m (%dms)",
            ip,
            timestamp,
            methodColor, method,
            uri,
            statusColor, status, statusText,
            duration
        )
        println(log)

        // log body somente para POST, PUT, PATCH
        if (wrappedRequest is ContentCachingRequestWrapper) {
            val content = wrappedRequest.contentAsByteArray
            if (content.isNotEmpty()) {
                val body = String(content, wrappedRequest.characterEncoding?.let { Charsets.UTF_8 } ?: Charsets.UTF_8)
                if (body.isNotBlank()) {
                    val prettyBody = try {
                        JSONObject(body).toString(2)
                    } catch (e: Exception) {
                        body
                    }
                    println("\u001B[90mRequest Body:\n$prettyBody\u001B[0m")
                }
            }
        }
    }

    private fun getHttpStatusText(status: Int): String {
        return when (status) {
            100 -> "Continue"; 101 -> "Switching Protocols"; 102 -> "Processing"
            200 -> "OK"; 201 -> "Created"; 202 -> "Accepted"; 203 -> "Non-Authoritative Information"
            204 -> "No Content"; 205 -> "Reset Content"; 206 -> "Partial Content"
            300 -> "Multiple Choices"; 301 -> "Moved Permanently"; 302 -> "Found"; 303 -> "See Other"; 304 -> "Not Modified"; 307 -> "Temporary Redirect"; 308 -> "Permanent Redirect"
            400 -> "Bad Request"; 401 -> "Unauthorized"; 402 -> "Payment Required"; 403 -> "Forbidden"; 404 -> "Not Found"; 405 -> "Method Not Allowed"; 406 -> "Not Acceptable"; 407 -> "Proxy Authentication Required"; 408 -> "Request Timeout"; 409 -> "Conflict"; 410 -> "Gone"; 411 -> "Length Required"; 412 -> "Precondition Failed"; 413 -> "Payload Too Large"; 414 -> "URI Too Long"; 415 -> "Unsupported Media Type"; 416 -> "Range Not Satisfiable"; 417 -> "Expectation Failed"; 418 -> "I'm a teapot"; 422 -> "Unprocessable Entity"; 425 -> "Too Early"; 426 -> "Upgrade Required"; 428 -> "Precondition Required"; 429 -> "Too Many Requests"; 431 -> "Request Header Fields Too Large"; 451 -> "Unavailable For Legal Reasons"
            500 -> "Internal Server Error"; 501 -> "Not Implemented"; 502 -> "Bad Gateway"; 503 -> "Service Unavailable"; 504 -> "Gateway Timeout"; 505 -> "HTTP Version Not Supported"
            else -> "Unknown"
        }
    }
}
