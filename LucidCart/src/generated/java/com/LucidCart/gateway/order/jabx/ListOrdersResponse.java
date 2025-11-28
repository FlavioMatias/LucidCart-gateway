
package com.LucidCart.gateway.order.jabx;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java de anonymous complex type.&lt;/p&gt;
 * 
 * &lt;p&gt;O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.&lt;/p&gt;
 * 
 * &lt;pre&gt;{&#064;code
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="orders" type="{http://orderservice.com/contract}OrderListSoap"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * }&lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "orders"
})
@XmlRootElement(name = "ListOrdersResponse")
public class ListOrdersResponse {

    @XmlElement(required = true)
    protected OrderListSoap orders;

    /**
     * Obtém o valor da propriedade orders.
     * 
     * @return
     *     possible object is
     *     {@link OrderListSoap }
     *     
     */
    public OrderListSoap getOrders() {
        return orders;
    }

    /**
     * Define o valor da propriedade orders.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderListSoap }
     *     
     */
    public void setOrders(OrderListSoap value) {
        this.orders = value;
    }

}
