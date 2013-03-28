/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.jsr289;

import java.security.Principal;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Locale;
import javax.servlet.sip.Address;
import javax.servlet.sip.Parameterable;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipServletMessage.HeaderForm;
import javax.servlet.sip.SipSession;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author andrew
 */
public class SipServletMessageImplTest extends TestCase {
    
    public SipServletMessageImplTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SipServletMessageImplTest.class);
        return suite;
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getFrom method, of class SipServletMessageImpl.
     */
    public void testGetFrom() {
        System.out.println("getFrom");
        SipServletMessageImpl instance = null;
        Address expResult = null;
        Address result = instance.getFrom();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTo method, of class SipServletMessageImpl.
     */
    public void testGetTo() {
        System.out.println("getTo");
        SipServletMessageImpl instance = null;
        Address expResult = null;
        Address result = instance.getTo();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMethod method, of class SipServletMessageImpl.
     */
    public void testGetMethod() {
        System.out.println("getMethod");
        SipServletMessageImpl instance = null;
        String expResult = "";
        String result = instance.getMethod();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProtocol method, of class SipServletMessageImpl.
     */
    public void testGetProtocol() {
        System.out.println("getProtocol");
        SipServletMessageImpl instance = null;
        String expResult = "";
        String result = instance.getProtocol();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHeader method, of class SipServletMessageImpl.
     */
    public void testGetHeader() {
        System.out.println("getHeader");
        String string = "";
        SipServletMessageImpl instance = null;
        String expResult = "";
        String result = instance.getHeader(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHeaders method, of class SipServletMessageImpl.
     */
    public void testGetHeaders() {
        System.out.println("getHeaders");
        String string = "";
        SipServletMessageImpl instance = null;
        ListIterator expResult = null;
        ListIterator result = instance.getHeaders(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHeaderNames method, of class SipServletMessageImpl.
     */
    public void testGetHeaderNames() {
        System.out.println("getHeaderNames");
        SipServletMessageImpl instance = null;
        Iterator expResult = null;
        Iterator result = instance.getHeaderNames();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setHeader method, of class SipServletMessageImpl.
     */
    public void testSetHeader() {
        System.out.println("setHeader");
        String string = "";
        String string1 = "";
        SipServletMessageImpl instance = null;
        instance.setHeader(string, string1);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addHeader method, of class SipServletMessageImpl.
     */
    public void testAddHeader() {
        System.out.println("addHeader");
        String string = "";
        String string1 = "";
        SipServletMessageImpl instance = null;
        instance.addHeader(string, string1);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeHeader method, of class SipServletMessageImpl.
     */
    public void testRemoveHeader() {
        System.out.println("removeHeader");
        String string = "";
        SipServletMessageImpl instance = null;
        instance.removeHeader(string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAddressHeader method, of class SipServletMessageImpl.
     */
    public void testGetAddressHeader() throws Exception {
        System.out.println("getAddressHeader");
        String string = "";
        SipServletMessageImpl instance = null;
        Address expResult = null;
        Address result = instance.getAddressHeader(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAddressHeaders method, of class SipServletMessageImpl.
     */
    public void testGetAddressHeaders() throws Exception {
        System.out.println("getAddressHeaders");
        String string = "";
        SipServletMessageImpl instance = null;
        ListIterator expResult = null;
        ListIterator result = instance.getAddressHeaders(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setAddressHeader method, of class SipServletMessageImpl.
     */
    public void testSetAddressHeader() {
        System.out.println("setAddressHeader");
        String string = "";
        Address adrs = null;
        SipServletMessageImpl instance = null;
        instance.setAddressHeader(string, adrs);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addAddressHeader method, of class SipServletMessageImpl.
     */
    public void testAddAddressHeader() {
        System.out.println("addAddressHeader");
        String string = "";
        Address adrs = null;
        boolean bln = false;
        SipServletMessageImpl instance = null;
        instance.addAddressHeader(string, adrs, bln);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameterableHeader method, of class SipServletMessageImpl.
     */
    public void testGetParameterableHeader() throws Exception {
        System.out.println("getParameterableHeader");
        String string = "";
        SipServletMessageImpl instance = null;
        Parameterable expResult = null;
        Parameterable result = instance.getParameterableHeader(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameterableHeaders method, of class SipServletMessageImpl.
     */
    public void testGetParameterableHeaders() throws Exception {
        System.out.println("getParameterableHeaders");
        String string = "";
        SipServletMessageImpl instance = null;
        ListIterator expResult = null;
        ListIterator result = instance.getParameterableHeaders(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setParameterableHeader method, of class SipServletMessageImpl.
     */
    public void testSetParameterableHeader() {
        System.out.println("setParameterableHeader");
        String string = "";
        Parameterable p = null;
        SipServletMessageImpl instance = null;
        instance.setParameterableHeader(string, p);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addParameterableHeader method, of class SipServletMessageImpl.
     */
    public void testAddParameterableHeader() {
        System.out.println("addParameterableHeader");
        String string = "";
        Parameterable p = null;
        boolean bln = false;
        SipServletMessageImpl instance = null;
        instance.addParameterableHeader(string, p, bln);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCallId method, of class SipServletMessageImpl.
     */
    public void testGetCallId() {
        System.out.println("getCallId");
        SipServletMessageImpl instance = null;
        String expResult = "";
        String result = instance.getCallId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getExpires method, of class SipServletMessageImpl.
     */
    public void testGetExpires() {
        System.out.println("getExpires");
        SipServletMessageImpl instance = null;
        int expResult = 0;
        int result = instance.getExpires();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setExpires method, of class SipServletMessageImpl.
     */
    public void testSetExpires() {
        System.out.println("setExpires");
        int i = 0;
        SipServletMessageImpl instance = null;
        instance.setExpires(i);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCharacterEncoding method, of class SipServletMessageImpl.
     */
    public void testGetCharacterEncoding() {
        System.out.println("getCharacterEncoding");
        SipServletMessageImpl instance = null;
        String expResult = "";
        String result = instance.getCharacterEncoding();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCharacterEncoding method, of class SipServletMessageImpl.
     */
    public void testSetCharacterEncoding() {
        System.out.println("setCharacterEncoding");
        String string = "";
        SipServletMessageImpl instance = null;
        instance.setCharacterEncoding(string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getContentLength method, of class SipServletMessageImpl.
     */
    public void testGetContentLength() {
        System.out.println("getContentLength");
        SipServletMessageImpl instance = null;
        int expResult = 0;
        int result = instance.getContentLength();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getContentType method, of class SipServletMessageImpl.
     */
    public void testGetContentType() {
        System.out.println("getContentType");
        SipServletMessageImpl instance = null;
        String expResult = "";
        String result = instance.getContentType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRawContent method, of class SipServletMessageImpl.
     */
    public void testGetRawContent() throws Exception {
        System.out.println("getRawContent");
        SipServletMessageImpl instance = null;
        byte[] expResult = null;
        byte[] result = instance.getRawContent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getContent method, of class SipServletMessageImpl.
     */
    public void testGetContent() throws Exception {
        System.out.println("getContent");
        SipServletMessageImpl instance = null;
        Object expResult = null;
        Object result = instance.getContent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setContent method, of class SipServletMessageImpl.
     */
    public void testSetContent() throws Exception {
        System.out.println("setContent");
        Object o = null;
        String string = "";
        SipServletMessageImpl instance = null;
        instance.setContent(o, string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setContentLength method, of class SipServletMessageImpl.
     */
    public void testSetContentLength() {
        System.out.println("setContentLength");
        int i = 0;
        SipServletMessageImpl instance = null;
        instance.setContentLength(i);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setContentType method, of class SipServletMessageImpl.
     */
    public void testSetContentType() {
        System.out.println("setContentType");
        String string = "";
        SipServletMessageImpl instance = null;
        instance.setContentType(string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAttribute method, of class SipServletMessageImpl.
     */
    public void testGetAttribute() {
        System.out.println("getAttribute");
        String string = "";
        SipServletMessageImpl instance = null;
        Object expResult = null;
        Object result = instance.getAttribute(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAttributeNames method, of class SipServletMessageImpl.
     */
    public void testGetAttributeNames() {
        System.out.println("getAttributeNames");
        SipServletMessageImpl instance = null;
        Enumeration expResult = null;
        Enumeration result = instance.getAttributeNames();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setAttribute method, of class SipServletMessageImpl.
     */
    public void testSetAttribute() {
        System.out.println("setAttribute");
        String string = "";
        Object o = null;
        SipServletMessageImpl instance = null;
        instance.setAttribute(string, o);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeAttribute method, of class SipServletMessageImpl.
     */
    public void testRemoveAttribute() {
        System.out.println("removeAttribute");
        String string = "";
        SipServletMessageImpl instance = null;
        instance.removeAttribute(string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSession method, of class SipServletMessageImpl.
     */
    public void testGetSession_0args() {
        System.out.println("getSession");
        SipServletMessageImpl instance = null;
        SipSession expResult = null;
        SipSession result = instance.getSession();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSession method, of class SipServletMessageImpl.
     */
    public void testGetSession_boolean() {
        System.out.println("getSession");
        boolean bln = false;
        SipServletMessageImpl instance = null;
        SipSession expResult = null;
        SipSession result = instance.getSession(bln);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getApplicationSession method, of class SipServletMessageImpl.
     */
    public void testGetApplicationSession_0args() {
        System.out.println("getApplicationSession");
        SipServletMessageImpl instance = null;
        SipApplicationSession expResult = null;
        SipApplicationSession result = instance.getApplicationSession();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getApplicationSession method, of class SipServletMessageImpl.
     */
    public void testGetApplicationSession_boolean() {
        System.out.println("getApplicationSession");
        boolean bln = false;
        SipServletMessageImpl instance = null;
        SipApplicationSession expResult = null;
        SipApplicationSession result = instance.getApplicationSession(bln);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAcceptLanguage method, of class SipServletMessageImpl.
     */
    public void testGetAcceptLanguage() {
        System.out.println("getAcceptLanguage");
        SipServletMessageImpl instance = null;
        Locale expResult = null;
        Locale result = instance.getAcceptLanguage();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAcceptLanguages method, of class SipServletMessageImpl.
     */
    public void testGetAcceptLanguages() {
        System.out.println("getAcceptLanguages");
        SipServletMessageImpl instance = null;
        Iterator expResult = null;
        Iterator result = instance.getAcceptLanguages();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setAcceptLanguage method, of class SipServletMessageImpl.
     */
    public void testSetAcceptLanguage() {
        System.out.println("setAcceptLanguage");
        Locale locale = null;
        SipServletMessageImpl instance = null;
        instance.setAcceptLanguage(locale);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addAcceptLanguage method, of class SipServletMessageImpl.
     */
    public void testAddAcceptLanguage() {
        System.out.println("addAcceptLanguage");
        Locale locale = null;
        SipServletMessageImpl instance = null;
        instance.addAcceptLanguage(locale);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setContentLanguage method, of class SipServletMessageImpl.
     */
    public void testSetContentLanguage() {
        System.out.println("setContentLanguage");
        Locale locale = null;
        SipServletMessageImpl instance = null;
        instance.setContentLanguage(locale);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getContentLanguage method, of class SipServletMessageImpl.
     */
    public void testGetContentLanguage() {
        System.out.println("getContentLanguage");
        SipServletMessageImpl instance = null;
        Locale expResult = null;
        Locale result = instance.getContentLanguage();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of send method, of class SipServletMessageImpl.
     */
    public void testSend() throws Exception {
        System.out.println("send");
        SipServletMessageImpl instance = null;
        instance.send();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isSecure method, of class SipServletMessageImpl.
     */
    public void testIsSecure() {
        System.out.println("isSecure");
        SipServletMessageImpl instance = null;
        boolean expResult = false;
        boolean result = instance.isSecure();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isCommitted method, of class SipServletMessageImpl.
     */
    public void testIsCommitted() {
        System.out.println("isCommitted");
        SipServletMessageImpl instance = null;
        boolean expResult = false;
        boolean result = instance.isCommitted();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRemoteUser method, of class SipServletMessageImpl.
     */
    public void testGetRemoteUser() {
        System.out.println("getRemoteUser");
        SipServletMessageImpl instance = null;
        String expResult = "";
        String result = instance.getRemoteUser();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isUserInRole method, of class SipServletMessageImpl.
     */
    public void testIsUserInRole() {
        System.out.println("isUserInRole");
        String string = "";
        SipServletMessageImpl instance = null;
        boolean expResult = false;
        boolean result = instance.isUserInRole(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserPrincipal method, of class SipServletMessageImpl.
     */
    public void testGetUserPrincipal() {
        System.out.println("getUserPrincipal");
        SipServletMessageImpl instance = null;
        Principal expResult = null;
        Principal result = instance.getUserPrincipal();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLocalAddr method, of class SipServletMessageImpl.
     */
    public void testGetLocalAddr() {
        System.out.println("getLocalAddr");
        SipServletMessageImpl instance = null;
        String expResult = "";
        String result = instance.getLocalAddr();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLocalPort method, of class SipServletMessageImpl.
     */
    public void testGetLocalPort() {
        System.out.println("getLocalPort");
        SipServletMessageImpl instance = null;
        int expResult = 0;
        int result = instance.getLocalPort();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRemoteAddr method, of class SipServletMessageImpl.
     */
    public void testGetRemoteAddr() {
        System.out.println("getRemoteAddr");
        SipServletMessageImpl instance = null;
        String expResult = "";
        String result = instance.getRemoteAddr();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRemotePort method, of class SipServletMessageImpl.
     */
    public void testGetRemotePort() {
        System.out.println("getRemotePort");
        SipServletMessageImpl instance = null;
        int expResult = 0;
        int result = instance.getRemotePort();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTransport method, of class SipServletMessageImpl.
     */
    public void testGetTransport() {
        System.out.println("getTransport");
        SipServletMessageImpl instance = null;
        String expResult = "";
        String result = instance.getTransport();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInitialRemoteAddr method, of class SipServletMessageImpl.
     */
    public void testGetInitialRemoteAddr() {
        System.out.println("getInitialRemoteAddr");
        SipServletMessageImpl instance = null;
        String expResult = "";
        String result = instance.getInitialRemoteAddr();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInitialRemotePort method, of class SipServletMessageImpl.
     */
    public void testGetInitialRemotePort() {
        System.out.println("getInitialRemotePort");
        SipServletMessageImpl instance = null;
        int expResult = 0;
        int result = instance.getInitialRemotePort();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInitialTransport method, of class SipServletMessageImpl.
     */
    public void testGetInitialTransport() {
        System.out.println("getInitialTransport");
        SipServletMessageImpl instance = null;
        String expResult = "";
        String result = instance.getInitialTransport();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setHeaderForm method, of class SipServletMessageImpl.
     */
    public void testSetHeaderForm() {
        System.out.println("setHeaderForm");
        HeaderForm hf = null;
        SipServletMessageImpl instance = null;
        instance.setHeaderForm(hf);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHeaderForm method, of class SipServletMessageImpl.
     */
    public void testGetHeaderForm() {
        System.out.println("getHeaderForm");
        SipServletMessageImpl instance = null;
        HeaderForm expResult = null;
        HeaderForm result = instance.getHeaderForm();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
