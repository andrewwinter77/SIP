/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.jsr289;

import org.andrewwinter.jsr289.SipSessionImpl;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Enumeration;
import javax.servlet.ServletContext;
import javax.servlet.sip.Address;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipSession.State;
import javax.servlet.sip.URI;
import javax.servlet.sip.ar.SipApplicationRoutingRegion;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author andrew
 */
public class SipSessionImplTest extends TestCase {
    
    public SipSessionImplTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SipSessionImplTest.class);
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
     * Test of getCreationTime method, of class SipSessionImpl.
     */
    public void testGetCreationTime() {
        System.out.println("getCreationTime");
        SipSessionImpl instance = null;
        long expResult = 0L;
        long result = instance.getCreationTime();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getId method, of class SipSessionImpl.
     */
    public void testGetId() {
        System.out.println("getId");
        SipSessionImpl instance = null;
        String expResult = "";
        String result = instance.getId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLastAccessedTime method, of class SipSessionImpl.
     */
    public void testGetLastAccessedTime() {
        System.out.println("getLastAccessedTime");
        SipSessionImpl instance = null;
        long expResult = 0L;
        long result = instance.getLastAccessedTime();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of invalidate method, of class SipSessionImpl.
     */
    public void testInvalidate() {
        System.out.println("invalidate");
        SipSessionImpl instance = null;
        instance.invalidate();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isReadyToInvalidate method, of class SipSessionImpl.
     */
    public void testIsReadyToInvalidate() {
        System.out.println("isReadyToInvalidate");
        SipSessionImpl instance = null;
        boolean expResult = false;
        boolean result = instance.isReadyToInvalidate();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setInvalidateWhenReady method, of class SipSessionImpl.
     */
    public void testSetInvalidateWhenReady() {
        System.out.println("setInvalidateWhenReady");
        boolean bln = false;
        SipSessionImpl instance = null;
        instance.setInvalidateWhenReady(bln);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInvalidateWhenReady method, of class SipSessionImpl.
     */
    public void testGetInvalidateWhenReady() {
        System.out.println("getInvalidateWhenReady");
        SipSessionImpl instance = null;
        boolean expResult = false;
        boolean result = instance.getInvalidateWhenReady();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getApplicationSession method, of class SipSessionImpl.
     */
    public void testGetApplicationSession() {
        System.out.println("getApplicationSession");
        SipSessionImpl instance = null;
        SipApplicationSession expResult = null;
        SipApplicationSession result = instance.getApplicationSession();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCallId method, of class SipSessionImpl.
     */
    public void testGetCallId() {
        System.out.println("getCallId");
        SipSessionImpl instance = null;
        String expResult = "";
        String result = instance.getCallId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLocalParty method, of class SipSessionImpl.
     */
    public void testGetLocalParty() {
        System.out.println("getLocalParty");
        SipSessionImpl instance = null;
        Address expResult = null;
        Address result = instance.getLocalParty();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRemoteParty method, of class SipSessionImpl.
     */
    public void testGetRemoteParty() {
        System.out.println("getRemoteParty");
        SipSessionImpl instance = null;
        Address expResult = null;
        Address result = instance.getRemoteParty();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createRequest method, of class SipSessionImpl.
     */
    public void testCreateRequest() {
        System.out.println("createRequest");
        String string = "";
        SipSessionImpl instance = null;
        SipServletRequest expResult = null;
        SipServletRequest result = instance.createRequest(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setHandler method, of class SipSessionImpl.
     */
    public void testSetHandler() throws Exception {
        System.out.println("setHandler");
        String string = "";
        SipSessionImpl instance = null;
        instance.setHandler(string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAttribute method, of class SipSessionImpl.
     */
    public void testGetAttribute() {
        System.out.println("getAttribute");
        String string = "";
        SipSessionImpl instance = null;
        Object expResult = null;
        Object result = instance.getAttribute(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAttributeNames method, of class SipSessionImpl.
     */
    public void testGetAttributeNames() {
        System.out.println("getAttributeNames");
        SipSessionImpl instance = null;
        Enumeration expResult = null;
        Enumeration result = instance.getAttributeNames();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setAttribute method, of class SipSessionImpl.
     */
    public void testSetAttribute() {
        System.out.println("setAttribute");
        String name = "";
        Object attribute = null;
        SipSessionImpl instance = null;
        instance.setAttribute(name, attribute);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeAttribute method, of class SipSessionImpl.
     */
    public void testRemoveAttribute() {
        System.out.println("removeAttribute");
        String string = "";
        SipSessionImpl instance = null;
        instance.removeAttribute(string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getState method, of class SipSessionImpl.
     */
    public void testGetState() {
        System.out.println("getState");
        SipSessionImpl instance = null;
        State expResult = null;
        State result = instance.getState();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setOutboundInterface method, of class SipSessionImpl.
     */
    public void testSetOutboundInterface_InetSocketAddress() {
        System.out.println("setOutboundInterface");
        InetSocketAddress isa = null;
        SipSessionImpl instance = null;
        instance.setOutboundInterface(isa);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setOutboundInterface method, of class SipSessionImpl.
     */
    public void testSetOutboundInterface_InetAddress() {
        System.out.println("setOutboundInterface");
        InetAddress ia = null;
        SipSessionImpl instance = null;
        instance.setOutboundInterface(ia);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isValid method, of class SipSessionImpl.
     */
    public void testIsValid() {
        System.out.println("isValid");
        SipSessionImpl instance = null;
        boolean expResult = false;
        boolean result = instance.isValid();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRegion method, of class SipSessionImpl.
     */
    public void testGetRegion() {
        System.out.println("getRegion");
        SipSessionImpl instance = null;
        SipApplicationRoutingRegion expResult = null;
        SipApplicationRoutingRegion result = instance.getRegion();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSubscriberURI method, of class SipSessionImpl.
     */
    public void testGetSubscriberURI() {
        System.out.println("getSubscriberURI");
        SipSessionImpl instance = null;
        URI expResult = null;
        URI result = instance.getSubscriberURI();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getServletContext method, of class SipSessionImpl.
     */
    public void testGetServletContext() {
        System.out.println("getServletContext");
        SipSessionImpl instance = null;
        ServletContext expResult = null;
        ServletContext result = instance.getServletContext();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
