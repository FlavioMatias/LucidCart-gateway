package com.LucidCart.gateway.order

import com.LucidCart.gateway.order.jabx.*
import org.springframework.stereotype.Component
import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.Marshaller
import jakarta.xml.bind.Unmarshaller
import java.io.StringReader
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import org.w3c.dom.Node

@Component
class OrderGateway(
    private val client: OrderClientHTTP
) {

    private val ns = "http://orderservice.com/contract"

    private val jaxb: JAXBContext = JAXBContext.newInstance(
        "com.LucidCart.gateway.order.jabx"
    )

    private val marshaller: Marshaller = jaxb.createMarshaller().apply {
        setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false)
    }

    private val unmarshaller: Unmarshaller = jaxb.createUnmarshaller()


    // =====================================================
    // WRAP SOAP
    // =====================================================
    private fun wrap(body: String, token: String): String {
        return """
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ord="$ns">
                <soapenv:Header>
                    <Authorization xmlns="http://orderservice.com/soap">$token</Authorization>
                </soapenv:Header>
                <soapenv:Body>
                    $body
                </soapenv:Body>
            </soapenv:Envelope>
        """.trimIndent()
    }

    // =====================================================
    // MARSHAL JAVA -> XML
    // =====================================================
    private fun marshal(obj: Any): String {
        val writer = StringWriter()
        marshaller.marshal(obj, writer)
        return writer.toString()
            .replaceFirst("""<\?xml.*?\?>""".toRegex(), "")
            .trim()
    }

    // =====================================================
    // PARSE SOAP COM DOM E EXTRAIR O BODY CORRETAMENTE
    // =====================================================
    private fun extractSoapBodyNode(rawXml: String): Node {
        val factory = DocumentBuilderFactory.newInstance()
        factory.isNamespaceAware = true
        val doc = factory.newDocumentBuilder().parse(rawXml.byteInputStream())

        // pega todos Body (qualquer prefixo)
        val bodies = doc.getElementsByTagNameNS(
            "http://schemas.xmlsoap.org/soap/envelope/",
            "Body"
        )

        if (bodies.length == 0) {
            throw IllegalStateException("SOAP Body não encontrado")
        }

        val body = bodies.item(0)

        // primeiro elemento dentro do Body
        var node = body.firstChild
        while (node != null && node.nodeType != Node.ELEMENT_NODE) {
            node = node.nextSibling
        }

        return node ?: throw IllegalStateException("Nenhum elemento dentro do Body")
    }

    // =====================================================
    // SERIALIZE DOM NODE DE VOLTA PARA XML STRING
    // =====================================================
    private fun serializeNode(node: Node): String {
        val transformer = TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
        transformer.setOutputProperty(OutputKeys.INDENT, "no")

        val writer = StringWriter()
        transformer.transform(DOMSource(node), StreamResult(writer))
        return writer.toString()
    }

    // =====================================================
    // UNMARSHAL XML REAL
    // =====================================================
    private fun <T : Any> unmarshalFixed(xml: String, clazz: Class<T>): T =
        try {
            val root = unmarshaller.unmarshal(StringReader(xml))
            val value = if (root is jakarta.xml.bind.JAXBElement<*>) root.value else root
            clazz.cast(value)
        } catch (e: Exception) {
            println("====== ERRO NO UNMARSHAL ======")
            println(xml)
            e.printStackTrace()
            throw e
        }


    // =====================================================
    // CALL COM LOG PROFISSIONAL E SEM REGEX
    // =====================================================
    private fun <REQ : Any, RES : Any> call(
        soapAction: String,
        request: REQ,
        responseClass: Class<RES>,
        token: String
    ): RES {

        val bodyXml = marshal(request)
        println("\n==================== XML MARSHALLED (REQUEST) ====================")
        println(bodyXml)

        val envelope = wrap(bodyXml, token)
        println("\n==================== SOAP ENVELOPE ENVIADO ====================")
        println(envelope)


        val http = client.call(
            authorization = token,
            soapAction = soapAction,
            body = envelope
        )

        val raw = http.body ?: throw IllegalStateException("Empty SOAP response")

        println("\n==================== RAW SOAP RECEBIDO ====================")
        println(raw)


        // DOM parse aqui — SEM regex
        val node = extractSoapBodyNode(raw)
        println("\n==================== NODE BODY EXTRAÍDO ====================")
        println(node.nodeName)


        val cleanXml = serializeNode(node)
        println("\n==================== XML DO BODY (LIMPO) ====================")
        println(cleanXml)


        // Validação XML
        println("\n==================== VALIDANDO XML ====================")
        try {
            DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(cleanXml.byteInputStream())
            println("XML OK: bem formado ✓")
        } catch (e: Exception) {
            println("XML MALFORMADO ⛔")
            println(cleanXml)
            e.printStackTrace()
        }


        println("\n==================== TENTANDO UNMARSHAL ====================")
        return unmarshalFixed(cleanXml, responseClass)
    }


    // ======================================================
    // ADDRESS
    // ======================================================
    fun createAddress(token: String, req: CreateAddressRequest): CreateAddressResponse =
        call("CreateAddressRequest", req, CreateAddressResponse::class.java, token)

    fun updateAddress(token: String, req: UpdateAddressRequest): UpdateAddressResponse =
        call("UpdateAddressRequest", req, UpdateAddressResponse::class.java, token)

    fun deleteAddress(token: String, req: DeleteAddressRequest): DeleteAddressResponse =
        call("DeleteAddressRequest", req, DeleteAddressResponse::class.java, token)

    fun findAddress(token: String, req: FindAddressRequest): FindAddressResponse =
        call("FindAddressRequest", req, FindAddressResponse::class.java, token)

    // ======================================================
    // ITEM
    // ======================================================
    fun addItem(token: String, req: AddItemRequest): AddItemResponse =
        call("AddItemRequest", req, AddItemResponse::class.java, token)

    fun updateItem(token: String, req: UpdateItemRequest): UpdateItemResponse =
        call("UpdateItemRequest", req, UpdateItemResponse::class.java, token)

    fun deleteItem(token: String, req: DeleteItemRequest): DeleteItemResponse =
        call("DeleteItemRequest", req, DeleteItemResponse::class.java, token)

    // ======================================================
    // ORDER
    // ======================================================

    fun deleteOrder(token: String, req: DeleteOrderRequest): DeleteOrderResponse =
        call("DeleteOrderRequest", req, DeleteOrderResponse::class.java, token)

    fun findOrder(token: String, req: FindOrderRequest): FindOrderResponse =
        call("FindOrderRequest", req, FindOrderResponse::class.java, token)

    fun listOrders(token: String, req: ListOrdersRequest): ListOrdersResponse =
        call("ListOrdersRequest", req, ListOrdersResponse::class.java, token)

    fun findCart(token: String, req: FindCartRequest): FindCartResponse =
        call("FindCartRequest", req, FindCartResponse::class.java, token)
    fun sendOrder(token: String, req: SendOrderRequest): SendOrderResponse =
        call("SendOrderRequest", req, SendOrderResponse::class.java, token)
}
