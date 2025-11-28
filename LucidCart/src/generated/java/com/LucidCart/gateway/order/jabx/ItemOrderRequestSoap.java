
package com.LucidCart.gateway.order.jabx;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Classe Java de ItemOrderRequestSoap complex type.&lt;/p&gt;
 * 
 * &lt;p&gt;O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.&lt;/p&gt;
 * 
 * &lt;pre&gt;{&#064;code
 * &lt;complexType name="ItemOrderRequestSoap"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="productId" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="unitPrice" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * }&lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ItemOrderRequestSoap", propOrder = {
    "productId",
    "quantity",
    "unitPrice"
})
public class ItemOrderRequestSoap {

    protected long productId;
    protected long quantity;
    protected double unitPrice;

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

}
