//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.27 at 01:21:42 PM BST 
//


package org.jcp.xml.ns.sipservlet;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 *         A pattern is a condition: a predicate over the set of SIP requests.
 *       
 * 
 * <p>Java class for patternType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="patternType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.jcp.org/xml/ns/sipservlet}condition"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "patternType", propOrder = {
    "condition"
})
public class PatternType {

    @XmlElementRef(name = "condition", namespace = "http://www.jcp.org/xml/ns/sipservlet", type = JAXBElement.class)
    protected JAXBElement<?> condition;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the condition property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Contains }{@code >}
     *     {@link JAXBElement }{@code <}{@link Or }{@code >}
     *     {@link JAXBElement }{@code <}{@link Not }{@code >}
     *     {@link JAXBElement }{@code <}{@link Equal }{@code >}
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     {@link JAXBElement }{@code <}{@link SubdomainOf }{@code >}
     *     {@link JAXBElement }{@code <}{@link And }{@code >}
     *     {@link JAXBElement }{@code <}{@link Exists }{@code >}
     *     
     */
    public JAXBElement<?> getCondition() {
        return condition;
    }

    /**
     * Sets the value of the condition property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Contains }{@code >}
     *     {@link JAXBElement }{@code <}{@link Or }{@code >}
     *     {@link JAXBElement }{@code <}{@link Not }{@code >}
     *     {@link JAXBElement }{@code <}{@link Equal }{@code >}
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     {@link JAXBElement }{@code <}{@link SubdomainOf }{@code >}
     *     {@link JAXBElement }{@code <}{@link And }{@code >}
     *     {@link JAXBElement }{@code <}{@link Exists }{@code >}
     *     
     */
    public void setCondition(JAXBElement<?> value) {
        this.condition = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}