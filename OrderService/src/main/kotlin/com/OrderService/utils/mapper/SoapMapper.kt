package com.OrderService.utils.mapper

import com.OrderService.model.enuns.OrderStatus
import com.OrderService.utils.dto.AddressRequestDTO
import com.OrderService.utils.dto.AddressResponseDTO
import com.OrderService.utils.dto.ItemOrderRequestDTO
import com.OrderService.utils.dto.ItemOrderResponseDTO
import com.OrderService.utils.dto.OrderResponseDTO
import com.orderservice.contract.*
import org.springframework.stereotype.Component
import java.util.*
import javax.xml.datatype.DatatypeFactory
import javax.xml.datatype.XMLGregorianCalendar
import java.util.GregorianCalendar

@Component
class SoapMapper {

    /* ===============================
       ITEM
       =============================== */

    fun toItemOrderRequestDTO(item: ItemOrderRequestSoap): ItemOrderRequestDTO =
        ItemOrderRequestDTO(
            productId = item.productId,
            quantity = item.quantity.toLong(),
            unitPrice = item.unitPrice.toDouble()
        )

    fun toItemOrderSoap(dto: ItemOrderResponseDTO): ItemOrderSoap {
        val soap = ItemOrderSoap()
        dto.id?.let { soap.id = it }
        soap.productId = dto.productId
        soap.orderId = dto.orderId
        soap.quantity = dto.quantity.toLong()
        soap.unitPrice = dto.unitPrice.toDouble()
        soap.subtotal = dto.subtotal.toDouble()
        dto.createdAt?.let { soap.createdAt = it.toXMLGregorianCalendar() }
        dto.updatedAt?.let { soap.updatedAt = it.toXMLGregorianCalendar() }
        return soap
    }

    fun toItemListSoap(items: List<ItemOrderResponseDTO>): ItemOrderListSoap {
        val list = ItemOrderListSoap()
        items.forEach { list.item.add(toItemOrderSoap(it)) }
        return list
    }

    /* ===============================
       ADDRESS
       =============================== */

    fun toAddressRequestDTO(soap: AddressRequestSoap): AddressRequestDTO =
        AddressRequestDTO(
            fullAddress = soap.fullAddress,
            latitude = soap.latitude,
            longitude = soap.longitude
        )

    fun toAddressSoap(dto: AddressResponseDTO): AddressSoap {
        val soap = AddressSoap()
        dto.id?.let { soap.id = it }
        soap.userId = dto.userId
        soap.fullAddress = dto.fullAddress
        soap.latitude = dto.latitude
        soap.longitude = dto.longitude
        dto.createdAt?.let { soap.createdAt = it.toXMLGregorianCalendar() }
        dto.updatedAt?.let { soap.updatedAt = it.toXMLGregorianCalendar() }
        return soap
    }

    /* ===============================
       ORDER
       =============================== */

    fun toOrderSoap(dto: OrderResponseDTO): OrderSoap {
        val soap = OrderSoap()
        soap.id = dto.id
        soap.status = try {
            com.orderservice.contract.OrderStatus.valueOf(dto.status.name)
        } catch (_: Exception) {
            throw RuntimeException("Invalid mapping OrderStatus: ${dto.status}")
        }
        soap.traceCode = dto.traceCode
        soap.address = toAddressSoap(dto.address)
        dto.createdAt?.let { soap.createdAt = it.toXMLGregorianCalendar() }
        dto.updatedAt?.let { soap.updatedAt = it.toXMLGregorianCalendar() }
        soap.items = toItemListSoap(dto.items)
        return soap
    }

    fun toOrderListSoap(list: List<OrderResponseDTO>): OrderListSoap {
        val soapList = OrderListSoap()
        list.forEach { soapList.order.add(toOrderSoap(it)) }
        return soapList
    }

    /* ===============================
       STATUS
       =============================== */

    fun toDomainStatus(status: OrderStatus?): OrderStatus? =
        status?.let { OrderStatus.valueOf(it.name) }

    /* ===============================
       UTIL XMLGregorianCalendar
       =============================== */

    fun Date.toXMLGregorianCalendar(): XMLGregorianCalendar {
        val cal = GregorianCalendar()
        cal.time = this
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal)
    }
}
