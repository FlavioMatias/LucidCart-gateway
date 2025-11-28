
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
 *         &lt;element name="item" type="{http://orderservice.com/contract}ItemOrderSoap"/&gt;
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
    "item"
})
@XmlRootElement(name = "AddItemResponse")
public class AddItemResponse {

    @XmlElement(required = true)
    protected ItemOrderSoap item;

    /**
     * Obtém o valor da propriedade item.
     * 
     * @return
     *     possible object is
     *     {@link ItemOrderSoap }
     *     
     */
    public ItemOrderSoap getItem() {
        return item;
    }

    /**
     * Define o valor da propriedade item.
     * 
     * @param value
     *     allowed object is
     *     {@link ItemOrderSoap }
     *     
     */
    public void setItem(ItemOrderSoap value) {
        this.item = value;
    }

}
