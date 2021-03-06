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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.sun.java.xml.ns.javaee.NonEmptyStringType;


/**
 * <p>Java class for servlet-selectionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="servlet-selectionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="main-servlet" type="{http://java.sun.com/xml/ns/javaee}nonEmptyStringType"/>
 *         &lt;element name="servlet-mapping" type="{http://www.jcp.org/xml/ns/sipservlet}servlet-mappingType" maxOccurs="unbounded"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "servlet-selectionType", propOrder = {
    "mainServlet",
    "servletMapping"
})
public class ServletSelectionType {

    @XmlElement(name = "main-servlet")
    protected NonEmptyStringType mainServlet;
    @XmlElement(name = "servlet-mapping")
    protected List<ServletMappingType> servletMapping;

    /**
     * Gets the value of the mainServlet property.
     * 
     * @return
     *     possible object is
     *     {@link NonEmptyStringType }
     *     
     */
    public NonEmptyStringType getMainServlet() {
        return mainServlet;
    }

    /**
     * Sets the value of the mainServlet property.
     * 
     * @param value
     *     allowed object is
     *     {@link NonEmptyStringType }
     *     
     */
    public void setMainServlet(NonEmptyStringType value) {
        this.mainServlet = value;
    }

    /**
     * Gets the value of the servletMapping property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the servletMapping property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServletMapping().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ServletMappingType }
     * 
     * 
     */
    public List<ServletMappingType> getServletMapping() {
        if (servletMapping == null) {
            servletMapping = new ArrayList<ServletMappingType>();
        }
        return this.servletMapping;
    }

}
