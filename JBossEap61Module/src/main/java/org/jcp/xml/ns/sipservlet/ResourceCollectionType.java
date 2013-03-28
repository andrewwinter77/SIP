//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.27 at 01:21:42 PM BST 
//


package org.jcp.xml.ns.sipservlet;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.sun.java.xml.ns.javaee.DescriptionType;
import com.sun.java.xml.ns.javaee.NonEmptyStringType;
import com.sun.java.xml.ns.javaee.ServletNameType;


/**
 * 
 *         The resource-collection element is used to identify a subset
 *         of the resources and SIP methods on those resources within a servlet
 *         application to which a security constraint applies. If no SIP methods
 *         are specified, then the security constraint applies to all SIP methods.
 *       
 * 
 * <p>Java class for resource-collectionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resource-collectionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="resource-name" type="{http://java.sun.com/xml/ns/javaee}string"/>
 *         &lt;element name="description" type="{http://java.sun.com/xml/ns/javaee}descriptionType" minOccurs="0"/>
 *         &lt;element name="servlet-name" type="{http://java.sun.com/xml/ns/javaee}servlet-nameType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="sip-method" type="{http://java.sun.com/xml/ns/javaee}nonEmptyStringType" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "resource-collectionType", propOrder = {
    "resourceName",
    "description",
    "servletName",
    "sipMethod"
})
public class ResourceCollectionType {

    @XmlElement(name = "resource-name", required = true)
    protected com.sun.java.xml.ns.javaee.String resourceName;
    protected DescriptionType description;
    @XmlElement(name = "servlet-name")
    protected List<ServletNameType> servletName;
    @XmlElement(name = "sip-method")
    protected List<NonEmptyStringType> sipMethod;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected java.lang.String id;

    /**
     * Gets the value of the resourceName property.
     * 
     * @return
     *     possible object is
     *     {@link com.sun.java.xml.ns.javaee.String }
     *     
     */
    public com.sun.java.xml.ns.javaee.String getResourceName() {
        return resourceName;
    }

    /**
     * Sets the value of the resourceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link com.sun.java.xml.ns.javaee.String }
     *     
     */
    public void setResourceName(com.sun.java.xml.ns.javaee.String value) {
        this.resourceName = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link DescriptionType }
     *     
     */
    public DescriptionType getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescriptionType }
     *     
     */
    public void setDescription(DescriptionType value) {
        this.description = value;
    }

    /**
     * Gets the value of the servletName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the servletName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServletName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ServletNameType }
     * 
     * 
     */
    public List<ServletNameType> getServletName() {
        if (servletName == null) {
            servletName = new ArrayList<ServletNameType>();
        }
        return this.servletName;
    }

    /**
     * Gets the value of the sipMethod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sipMethod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSipMethod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NonEmptyStringType }
     * 
     * 
     */
    public List<NonEmptyStringType> getSipMethod() {
        if (sipMethod == null) {
            sipMethod = new ArrayList<NonEmptyStringType>();
        }
        return this.sipMethod;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setId(java.lang.String value) {
        this.id = value;
    }

}
