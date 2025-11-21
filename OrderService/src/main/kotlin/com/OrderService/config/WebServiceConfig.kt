package com.OrderService.config

import com.OrderService.utils.JwtInterceptor
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.ws.config.annotation.EnableWs
import org.springframework.ws.config.annotation.WsConfigurer
import org.springframework.ws.server.EndpointInterceptor
import org.springframework.ws.transport.http.MessageDispatcherServlet
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition
import org.springframework.xml.xsd.SimpleXsdSchema
import org.springframework.xml.xsd.XsdSchema
import org.springframework.oxm.jaxb.Jaxb2Marshaller

@EnableWs
@Configuration
class WebServiceConfig(
    private val jwtInterceptor: JwtInterceptor
) : WsConfigurer {

    @Bean
    fun messageDispatcherServlet(context: ApplicationContext): ServletRegistrationBean<MessageDispatcherServlet> {
        val servlet = MessageDispatcherServlet()
        servlet.setApplicationContext(context)
        servlet.isTransformWsdlLocations = true
        return ServletRegistrationBean(servlet, "/ws/*")
    }

    @Bean
    fun orderSchema(): XsdSchema =
        SimpleXsdSchema(ClassPathResource("xsd/order.xsd"))

    @Bean(name = ["order"])
    fun defaultWsdl(orderSchema: XsdSchema): DefaultWsdl11Definition {
        val wsdl = DefaultWsdl11Definition()
        wsdl.setPortTypeName("OrderPort")
        wsdl.setLocationUri("/ws")
        wsdl.setTargetNamespace("http://orderservice.com/contract")
        wsdl.setSchema(orderSchema)
        return wsdl
    }

    @Bean
    fun marshaller(): Jaxb2Marshaller =
        Jaxb2Marshaller().apply {
            setPackagesToScan("com.orderservice.contract")
        }

    override fun addInterceptors(interceptors: MutableList<EndpointInterceptor>) {
        interceptors.add(jwtInterceptor)
    }
}
