/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.jsr289;

import org.andrewwinter.jsr289.SipServletRequestImpl;
import java.io.BufferedReader;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.sip.Address;
import javax.servlet.sip.AuthInfo;
import javax.servlet.sip.B2buaHelper;
import javax.servlet.sip.Proxy;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.URI;
import javax.servlet.sip.ar.SipApplicationRoutingDirective;
import javax.servlet.sip.ar.SipApplicationRoutingRegion;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author andrew
 */
public class SipServletRequestImplTest extends TestCase {
    
    public SipServletRequestImplTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SipServletRequestImplTest.class);
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
     * Test of getRequestURI method, of class SipServletRequestImpl.
     */
    public void testGetRequestURI() {
        System.out.println("getRequestURI");
        SipServletRequestImpl instance = null;
        URI expResult = null;
        URI result = instance.getRequestURI();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setRequestURI method, of class SipServletRequestImpl.
     */
    public void testSetRequestURI() {
        System.out.println("setRequestURI");
        URI uri = null;
        SipServletRequestImpl instance = null;
        instance.setRequestURI(uri);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pushRoute method, of class SipServletRequestImpl.
     */
    public void testPushRoute_SipURI() {
        System.out.println("pushRoute");
        SipURI sipuri = null;
        SipServletRequestImpl instance = null;
        instance.pushRoute(sipuri);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pushRoute method, of class SipServletRequestImpl.
     */
    public void testPushRoute_Address() {
        System.out.println("pushRoute");
        Address adrs = null;
        SipServletRequestImpl instance = null;
        instance.pushRoute(adrs);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMaxForwards method, of class SipServletRequestImpl.
     */
    public void testGetMaxForwards() {
        System.out.println("getMaxForwards");
        SipServletRequestImpl instance = null;
        int expResult = 0;
        int result = instance.getMaxForwards();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setMaxForwards method, of class SipServletRequestImpl.
     */
    public void testSetMaxForwards() {
        System.out.println("setMaxForwards");
        int i = 0;
        SipServletRequestImpl instance = null;
        instance.setMaxForwards(i);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isInitial method, of class SipServletRequestImpl.
     */
    public void testIsInitial() {
        System.out.println("isInitial");
        SipServletRequestImpl instance = null;
        boolean expResult = false;
        boolean result = instance.isInitial();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInputStream method, of class SipServletRequestImpl.
     */
    public void testGetInputStream() throws Exception {
        System.out.println("getInputStream");
        SipServletRequestImpl instance = null;
        ServletInputStream expResult = null;
        ServletInputStream result = instance.getInputStream();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getReader method, of class SipServletRequestImpl.
     */
    public void testGetReader() throws Exception {
        System.out.println("getReader");
        SipServletRequestImpl instance = null;
        BufferedReader expResult = null;
        BufferedReader result = instance.getReader();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProxy method, of class SipServletRequestImpl.
     */
    public void testGetProxy_0args() throws Exception {
        System.out.println("getProxy");
        SipServletRequestImpl instance = null;
        Proxy expResult = null;
        Proxy result = instance.getProxy();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProxy method, of class SipServletRequestImpl.
     */
    public void testGetProxy_boolean() throws Exception {
        System.out.println("getProxy");
        boolean bln = false;
        SipServletRequestImpl instance = null;
        Proxy expResult = null;
        Proxy result = instance.getProxy(bln);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createResponse method, of class SipServletRequestImpl.
     */
    public void testCreateResponse_int() {
        System.out.println("createResponse");
        int i = 0;
        SipServletRequestImpl instance = null;
        SipServletResponse expResult = null;
        SipServletResponse result = instance.createResponse(i);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createResponse method, of class SipServletRequestImpl.
     */
    public void testCreateResponse_int_String() {
        System.out.println("createResponse");
        int i = 0;
        String string = "";
        SipServletRequestImpl instance = null;
        SipServletResponse expResult = null;
        SipServletResponse result = instance.createResponse(i, string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createCancel method, of class SipServletRequestImpl.
     */
    public void testCreateCancel() {
        System.out.println("createCancel");
        SipServletRequestImpl instance = null;
        SipServletRequest expResult = null;
        SipServletRequest result = instance.createCancel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pushPath method, of class SipServletRequestImpl.
     */
    public void testPushPath() {
        System.out.println("pushPath");
        Address adrs = null;
        SipServletRequestImpl instance = null;
        instance.pushPath(adrs);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getB2buaHelper method, of class SipServletRequestImpl.
     */
    public void testGetB2buaHelper() {
        System.out.println("getB2buaHelper");
        SipServletRequestImpl instance = null;
        B2buaHelper expResult = null;
        B2buaHelper result = instance.getB2buaHelper();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPoppedRoute method, of class SipServletRequestImpl.
     */
    public void testGetPoppedRoute() {
        System.out.println("getPoppedRoute");
        SipServletRequestImpl instance = null;
        Address expResult = null;
        Address result = instance.getPoppedRoute();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInitialPoppedRoute method, of class SipServletRequestImpl.
     */
    public void testGetInitialPoppedRoute() {
        System.out.println("getInitialPoppedRoute");
        SipServletRequestImpl instance = null;
        Address expResult = null;
        Address result = instance.getInitialPoppedRoute();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setRoutingDirective method, of class SipServletRequestImpl.
     */
    public void testSetRoutingDirective() {
        System.out.println("setRoutingDirective");
        SipApplicationRoutingDirective sard = null;
        SipServletRequest ssr = null;
        SipServletRequestImpl instance = null;
        instance.setRoutingDirective(sard, ssr);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRoutingDirective method, of class SipServletRequestImpl.
     */
    public void testGetRoutingDirective() {
        System.out.println("getRoutingDirective");
        SipServletRequestImpl instance = null;
        SipApplicationRoutingDirective expResult = null;
        SipApplicationRoutingDirective result = instance.getRoutingDirective();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRegion method, of class SipServletRequestImpl.
     */
    public void testGetRegion() {
        System.out.println("getRegion");
        SipServletRequestImpl instance = null;
        SipApplicationRoutingRegion expResult = null;
        SipApplicationRoutingRegion result = instance.getRegion();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSubscriberURI method, of class SipServletRequestImpl.
     */
    public void testGetSubscriberURI() {
        System.out.println("getSubscriberURI");
        SipServletRequestImpl instance = null;
        URI expResult = null;
        URI result = instance.getSubscriberURI();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addAuthHeader method, of class SipServletRequestImpl.
     */
    public void testAddAuthHeader_SipServletResponse_AuthInfo() {
        System.out.println("addAuthHeader");
        SipServletResponse ssr = null;
        AuthInfo ai = null;
        SipServletRequestImpl instance = null;
        instance.addAuthHeader(ssr, ai);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addAuthHeader method, of class SipServletRequestImpl.
     */
    public void testAddAuthHeader_3args() {
        System.out.println("addAuthHeader");
        SipServletResponse ssr = null;
        String string = "";
        String string1 = "";
        SipServletRequestImpl instance = null;
        instance.addAuthHeader(ssr, string, string1);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameter method, of class SipServletRequestImpl.
     */
    public void testGetParameter() {
        System.out.println("getParameter");
        String string = "";
        SipServletRequestImpl instance = null;
        String expResult = "";
        String result = instance.getParameter(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameterNames method, of class SipServletRequestImpl.
     */
    public void testGetParameterNames() {
        System.out.println("getParameterNames");
        SipServletRequestImpl instance = null;
        Enumeration expResult = null;
        Enumeration result = instance.getParameterNames();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameterValues method, of class SipServletRequestImpl.
     */
    public void testGetParameterValues() {
        System.out.println("getParameterValues");
        String string = "";
        SipServletRequestImpl instance = null;
        String[] expResult = null;
        String[] result = instance.getParameterValues(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameterMap method, of class SipServletRequestImpl.
     */
    public void testGetParameterMap() {
        System.out.println("getParameterMap");
        SipServletRequestImpl instance = null;
        Map expResult = null;
        Map result = instance.getParameterMap();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getScheme method, of class SipServletRequestImpl.
     */
    public void testGetScheme() {
        System.out.println("getScheme");
        SipServletRequestImpl instance = null;
        String expResult = "";
        String result = instance.getScheme();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getServerName method, of class SipServletRequestImpl.
     */
    public void testGetServerName() {
        System.out.println("getServerName");
        SipServletRequestImpl instance = null;
        String expResult = "";
        String result = instance.getServerName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getServerPort method, of class SipServletRequestImpl.
     */
    public void testGetServerPort() {
        System.out.println("getServerPort");
        SipServletRequestImpl instance = null;
        int expResult = 0;
        int result = instance.getServerPort();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRemoteHost method, of class SipServletRequestImpl.
     */
    public void testGetRemoteHost() {
        System.out.println("getRemoteHost");
        SipServletRequestImpl instance = null;
        String expResult = "";
        String result = instance.getRemoteHost();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLocale method, of class SipServletRequestImpl.
     */
    public void testGetLocale() {
        System.out.println("getLocale");
        SipServletRequestImpl instance = null;
        Locale expResult = null;
        Locale result = instance.getLocale();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLocales method, of class SipServletRequestImpl.
     */
    public void testGetLocales() {
        System.out.println("getLocales");
        SipServletRequestImpl instance = null;
        Enumeration expResult = null;
        Enumeration result = instance.getLocales();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRequestDispatcher method, of class SipServletRequestImpl.
     */
    public void testGetRequestDispatcher() {
        System.out.println("getRequestDispatcher");
        String string = "";
        SipServletRequestImpl instance = null;
        RequestDispatcher expResult = null;
        RequestDispatcher result = instance.getRequestDispatcher(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRealPath method, of class SipServletRequestImpl.
     */
    public void testGetRealPath() {
        System.out.println("getRealPath");
        String string = "";
        SipServletRequestImpl instance = null;
        String expResult = "";
        String result = instance.getRealPath(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLocalName method, of class SipServletRequestImpl.
     */
    public void testGetLocalName() {
        System.out.println("getLocalName");
        SipServletRequestImpl instance = null;
        String expResult = "";
        String result = instance.getLocalName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class SipServletRequestImpl.
     */
    public void testToString() {
        System.out.println("toString");
        SipServletRequestImpl instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of send method, of class SipServletRequestImpl.
     */
    public void testSend() throws Exception {
        System.out.println("send");
        SipServletRequestImpl instance = null;
        instance.send();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
