//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.27 at 01:21:42 PM BST 
//


package org.jcp.xml.ns.sipservlet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *         Specifies a variable. Example: 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;var xmlns="http://www.jcp.org/xml/ns/sipservlet" xmlns:javaee="http://java.sun.com/xml/ns/javaee" xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;request.uri.user&lt;/var&gt;
 * </pre>
 * 
 * 
 * <p>Java class for var complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="var">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.jcp.org/xml/ns/sipservlet}var"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "var", propOrder = {
    "var"
})
@XmlSeeAlso({
    SubdomainOf.class,
    Exists.class,
    Contains.class,
    Equal.class
})
public class Var {

    @XmlElement(required = true)
    protected VarProperty var;

    /**
     * Gets the value of the var property.
     * 
     * @return
     *     possible object is
     *     {@link VarProperty }
     *     
     */
    public VarProperty getVar() {
        return var;
    }

    /**
     * Sets the value of the var property.
     * 
     * @param value
     *     allowed object is
     *     {@link VarProperty }
     *     
     */
    public void setVar(VarProperty value) {
        this.var = value;
    }

}
