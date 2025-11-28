package com.LucidCart.config

import com.LucidCart.gateway.order.OrderClientHTTP
import com.LucidCart.gateway.user.UserClientHTTP
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class OrderClientConfig {

    @Bean
    fun orderRestClient(@Value("\${services.order.url}") baseUrl: String): RestClient {
        return RestClient.builder()
            .baseUrl(baseUrl)
            .build()
    }

    @Bean
    fun orderClientHttp(@Qualifier("orderRestClient") restClient: RestClient): OrderClientHTTP {
        val adapter = RestClientAdapter.create(restClient)
        val factory = HttpServiceProxyFactory.builder()
            .exchangeAdapter(adapter)
            .build()
        return factory.createClient(OrderClientHTTP::class.java)
    }
}

@Configuration
class UserClientConfig {

    @Bean
    fun userRestClient(@Value("\${services.auth.url}") baseUrl: String): RestClient {
        return RestClient.builder()
            .baseUrl(baseUrl)
            .build()
    }

    @Bean
    fun userClientHttp(@Qualifier("userRestClient") restClient: RestClient): UserClientHTTP {
        val adapter = RestClientAdapter.create(restClient)
        val factory = HttpServiceProxyFactory.builder()
            .exchangeAdapter(adapter)
            .build()
        return factory.createClient(UserClientHTTP::class.java)
    }
}