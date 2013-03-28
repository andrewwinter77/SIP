/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.jsr289;

import org.andrewwinter.jsr289.SipURIImpl;
import java.util.Iterator;
import javax.servlet.sip.URI;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author andrew
 */
public class SipURIImplTest extends TestCase {
    
    public SipURIImplTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SipURIImplTest.class);
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
     * Test of getUser method, of class SipURIImpl.
     */
    public void testGetUser() {
        System.out.println("getUser");
        SipURIImpl instance = null;
        String expResult = "";
        String result = instance.getUser();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setUser method, of class SipURIImpl.
     */
    public void testSetUser() {
        System.out.println("setUser");
        String string = "";
        SipURIImpl instance = null;
        instance.setUser(string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserPassword method, of class SipURIImpl.
     */
    public void testGetUserPassword() {
        System.out.println("getUserPassword");
        SipURIImpl instance = null;
        String expResult = "";
        String result = instance.getUserPassword();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setUserPassword method, of class SipURIImpl.
     */
    public void testSetUserPassword() {
        System.out.println("setUserPassword");
        String password = "";
        SipURIImpl instance = null;
        instance.setUserPassword(password);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHost method, of class SipURIImpl.
     */
    public void testGetHost() {
        System.out.println("getHost");
        SipURIImpl instance = null;
        String expResult = "";
        String result = instance.getHost();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setHost method, of class SipURIImpl.
     */
    public void testSetHost() {
        System.out.println("setHost");
        String host = "";
        SipURIImpl instance = null;
        instance.setHost(host);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPort method, of class SipURIImpl.
     */
    public void testGetPort() {
        System.out.println("getPort");
        SipURIImpl instance = null;
        int expResult = 0;
        int result = instance.getPort();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPort method, of class SipURIImpl.
     */
    public void testSetPort() {
        System.out.println("setPort");
        int port = 0;
        SipURIImpl instance = null;
        instance.setPort(port);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isSecure method, of class SipURIImpl.
     */
    public void testIsSecure() {
        System.out.println("isSecure");
        SipURIImpl instance = null;
        boolean expResult = false;
        boolean result = instance.isSecure();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSecure method, of class SipURIImpl.
     */
    public void testSetSecure() {
        System.out.println("setSecure");
        boolean bln = false;
        SipURIImpl instance = null;
        instance.setSecure(bln);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTransportParam method, of class SipURIImpl.
     */
    public void testGetTransportParam() {
        System.out.println("getTransportParam");
        SipURIImpl instance = null;
        String expResult = "";
        String result = instance.getTransportParam();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTransportParam method, of class SipURIImpl.
     */
    public void testSetTransportParam() {
        System.out.println("setTransportParam");
        String string = "";
        SipURIImpl instance = null;
        instance.setTransportParam(string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMAddrParam method, of class SipURIImpl.
     */
    public void testGetMAddrParam() {
        System.out.println("getMAddrParam");
        SipURIImpl instance = null;
        String expResult = "";
        String result = instance.getMAddrParam();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setMAddrParam method, of class SipURIImpl.
     */
    public void testSetMAddrParam() {
        System.out.println("setMAddrParam");
        String maddr = "";
        SipURIImpl instance = null;
        instance.setMAddrParam(maddr);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMethodParam method, of class SipURIImpl.
     */
    public void testGetMethodParam() {
        System.out.println("getMethodParam");
        SipURIImpl instance = null;
        String expResult = "";
        String result = instance.getMethodParam();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setMethodParam method, of class SipURIImpl.
     */
    public void testSetMethodParam() {
        System.out.println("setMethodParam");
        String method = "";
        SipURIImpl instance = null;
        instance.setMethodParam(method);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTTLParam method, of class SipURIImpl.
     */
    public void testGetTTLParam() {
        System.out.println("getTTLParam");
        SipURIImpl instance = null;
        int expResult = 0;
        int result = instance.getTTLParam();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTTLParam method, of class SipURIImpl.
     */
    public void testSetTTLParam() {
        System.out.println("setTTLParam");
        int i = 0;
        SipURIImpl instance = null;
        instance.setTTLParam(i);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserParam method, of class SipURIImpl.
     */
    public void testGetUserParam() {
        System.out.println("getUserParam");
        SipURIImpl instance = null;
        String expResult = "";
        String result = instance.getUserParam();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setUserParam method, of class SipURIImpl.
     */
    public void testSetUserParam() {
        System.out.println("setUserParam");
        String string = "";
        SipURIImpl instance = null;
        instance.setUserParam(string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLrParam method, of class SipURIImpl.
     */
    public void testGetLrParam() {
        System.out.println("getLrParam");
        SipURIImpl instance = null;
        boolean expResult = false;
        boolean result = instance.getLrParam();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLrParam method, of class SipURIImpl.
     */
    public void testSetLrParam() {
        System.out.println("setLrParam");
        boolean bln = false;
        SipURIImpl instance = null;
        instance.setLrParam(bln);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHeader method, of class SipURIImpl.
     */
    public void testGetHeader() {
        System.out.println("getHeader");
        String string = "";
        SipURIImpl instance = null;
        String expResult = "";
        String result = instance.getHeader(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setHeader method, of class SipURIImpl.
     */
    public void testSetHeader() {
        System.out.println("setHeader");
        String string = "";
        String string1 = "";
        SipURIImpl instance = null;
        instance.setHeader(string, string1);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeHeader method, of class SipURIImpl.
     */
    public void testRemoveHeader() {
        System.out.println("removeHeader");
        String string = "";
        SipURIImpl instance = null;
        instance.removeHeader(string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHeaderNames method, of class SipURIImpl.
     */
    public void testGetHeaderNames() {
        System.out.println("getHeaderNames");
        SipURIImpl instance = null;
        Iterator expResult = null;
        Iterator result = instance.getHeaderNames();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clone method, of class SipURIImpl.
     */
    public void testClone() {
        System.out.println("clone");
        SipURIImpl instance = null;
        URI expResult = null;
        URI result = instance.clone();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
