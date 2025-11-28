
package com.LucidCart.gateway.order.jabx;

import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java de ItemOrderSoap complex type.&lt;/p&gt;
 * 
 * &lt;p&gt;O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.&lt;/p&gt;
 * 
 * &lt;pre&gt;{&#064;code
 * &lt;complexType name="ItemOrderSoap"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="productId" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="orderId" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="unitPrice" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="subtotal" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *         &lt;element name="createdAt" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="updatedAt" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * }&lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ItemOrderSoap", propOrder = {
    "id",
    "productId",
    "orderId",
    "quantity",
    "unitPrice",
    "subtotal",
    "createdAt",
    "updatedAt"
})
public class ItemOrderSoap {

    protected Long id;
    protected long productId;
    protected long orderId;
    protected long quantity;
    protected double unitPrice;
    protected double subtotal;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createdAt;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar updatedAt;

    /**
     * Obtém o valor da propriedade id.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o valor da propriedade id.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Obtém o valor da propriedade productId.
     * 
     */
    public long getProductId() {
        return productId;
    }

    /**
     * Define o valor da propriedade productId.
     * 
     */
    public void setProductId(long value) {
        this.productId = value;
    }

    /**
     * Obtém o valor da propriedade orderId.
     * 
     */
    public long getOrderId() {
        return orderId;
    }

    /**
     * Define o valor da propriedade orderId.
     * 
     */
    public void setOrderId(long value) {
        this.orderId = value;
    }

    /**
     * Obtém o valor da propriedade quantity.
     * 
     */
    public long getQuantity() {
        return quantity;
    }

    /**
     * Define o valor da propriedade quantity.
     * 
     */
    public void setQuantity(long value) {
        this.quantity = value;
    }

    /**
     * Obtém o valor da propriedade unitPrice.
     * 
     */
    public double getUnitPrice() {
        return unitPrice;
    }

    /**
     * Define o valor da propriedade unitPrice.
     * 
     */
    public void setUnitPrice(double value) {
        this.unitPrice = value;
    }

    /**
     * Obtém o valor da propriedade subtotal.
     * 
     */
    public double getSubtotal() {
        return subtotal;
    }

    /**
     * Define o valor da propriedade subtotal.
     * 
     */
    public void setSubtotal(double value) {
        this.subtotal = value;
    }

    /**
     * Obtém o valor da propriedade createdAt.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreatedAt() {
        return createdAt;
    }

    /**
     * Define o valor da propriedade createdAt.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreatedAt(XMLGregorianCalendar value) {
        this.createdAt = value;
    }

    /**
     * Obtém o valor da propriedade updatedAt.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Define o valor da propriedade updatedAt.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUpdatedAt(XMLGregorianCalendar value) {
        this.updatedAt = value;
    }

}
