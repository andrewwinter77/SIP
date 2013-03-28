/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.jsr289;

import org.andrewwinter.jsr289.SipServletResponseImpl;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.sip.Proxy;
import javax.servlet.sip.ProxyBranch;
import javax.servlet.sip.SipServletRequest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author andrew
 */
public class SipServletResponseImplTest extends TestCase {
    
    public SipServletResponseImplTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SipServletResponseImplTest.class);
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
     * Test of getRequest method, of class SipServletResponseImpl.
     */
    public void testGetRequest() {
        System.out.println("getRequest");
        SipServletResponseImpl instance = null;
        SipServletRequest expResult = null;
        SipServletRequest result = instance.getRequest();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStatus method, of class SipServletResponseImpl.
     */
    public void testGetStatus() {
        System.out.println("getStatus");
        SipServletResponseImpl instance = null;
        int expResult = 0;
        int result = instance.getStatus();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setStatus method, of class SipServletResponseImpl.
     */
    public void testSetStatus_int() {
        System.out.println("setStatus");
        int i = 0;
        SipServletResponseImpl instance = null;
        instance.setStatus(i);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setStatus method, of class SipServletResponseImpl.
     */
    public void testSetStatus_int_String() {
        System.out.println("setStatus");
        int i = 0;
        String string = "";
        SipServletResponseImpl instance = null;
        instance.setStatus(i, string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getReasonPhrase method, of class SipServletResponseImpl.
     */
    public void testGetReasonPhrase() {
        System.out.println("getReasonPhrase");
        SipServletResponseImpl instance = null;
        String expResult = "";
        String result = instance.getReasonPhrase();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOutputStream method, of class SipServletResponseImpl.
     */
    public void testGetOutputStream() throws Exception {
        System.out.println("getOutputStream");
        SipServletResponseImpl instance = null;
        ServletOutputStream expResult = null;
        ServletOutputStream result = instance.getOutputStream();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getWriter method, of class SipServletResponseImpl.
     */
    public void testGetWriter() throws Exception {
        System.out.println("getWriter");
        SipServletResponseImpl instance = null;
        PrintWriter expResult = null;
        PrintWriter result = instance.getWriter();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProxy method, of class SipServletResponseImpl.
     */
    public void testGetProxy() {
        System.out.println("getProxy");
        SipServletResponseImpl instance = null;
        Proxy expResult = null;
        Proxy result = instance.getProxy();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProxyBranch method, of class SipServletResponseImpl.
     */
    public void testGetProxyBranch() {
        System.out.println("getProxyBranch");
        SipServletResponseImpl instance = null;
        ProxyBranch expResult = null;
        ProxyBranch result = instance.getProxyBranch();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of sendReliably method, of class SipServletResponseImpl.
     */
    public void testSendReliably() throws Exception {
        System.out.println("sendReliably");
        SipServletResponseImpl instance = null;
        instance.sendReliably();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createAck method, of class SipServletResponseImpl.
     */
    public void testCreateAck() {
        System.out.println("createAck");
        SipServletResponseImpl instance = null;
        SipServletRequest expResult = null;
        SipServletRequest result = instance.createAck();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createPrack method, of class SipServletResponseImpl.
     */
    public void testCreatePrack() throws Exception {
        System.out.println("createPrack");
        SipServletResponseImpl instance = null;
        SipServletRequest expResult = null;
        SipServletRequest result = instance.createPrack();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getChallengeRealms method, of class SipServletResponseImpl.
     */
    public void testGetChallengeRealms() {
        System.out.println("getChallengeRealms");
        SipServletResponseImpl instance = null;
        Iterator expResult = null;
        Iterator result = instance.getChallengeRealms();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isBranchResponse method, of class SipServletResponseImpl.
     */
    public void testIsBranchResponse() {
        System.out.println("isBranchResponse");
        SipServletResponseImpl instance = null;
        boolean expResult = false;
        boolean result = instance.isBranchResponse();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setBufferSize method, of class SipServletResponseImpl.
     */
    public void testSetBufferSize() {
        System.out.println("setBufferSize");
        int i = 0;
        SipServletResponseImpl instance = null;
        instance.setBufferSize(i);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBufferSize method, of class SipServletResponseImpl.
     */
    public void testGetBufferSize() {
        System.out.println("getBufferSize");
        SipServletResponseImpl instance = null;
        int expResult = 0;
        int result = instance.getBufferSize();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of flushBuffer method, of class SipServletResponseImpl.
     */
    public void testFlushBuffer() throws Exception {
        System.out.println("flushBuffer");
        SipServletResponseImpl instance = null;
        instance.flushBuffer();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of resetBuffer method, of class SipServletResponseImpl.
     */
    public void testResetBuffer() {
        System.out.println("resetBuffer");
        SipServletResponseImpl instance = null;
        instance.resetBuffer();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of reset method, of class SipServletResponseImpl.
     */
    public void testReset() {
        System.out.println("reset");
        SipServletResponseImpl instance = null;
        instance.reset();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLocale method, of class SipServletResponseImpl.
     */
    public void testSetLocale() {
        System.out.println("setLocale");
        Locale locale = null;
        SipServletResponseImpl instance = null;
        instance.setLocale(locale);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLocale method, of class SipServletResponseImpl.
     */
    public void testGetLocale() {
        System.out.println("getLocale");
        SipServletResponseImpl instance = null;
        Locale expResult = null;
        Locale result = instance.getLocale();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class SipServletResponseImpl.
     */
    public void testToString() {
        System.out.println("toString");
        SipServletResponseImpl instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
