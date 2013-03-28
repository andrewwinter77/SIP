/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.jsr289;

import org.andrewwinter.jsr289.SipSessionStore;
import javax.servlet.sip.SipSession;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author andrew
 */
public class SipSessionStoreTest extends TestCase {
    
    public SipSessionStoreTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SipSessionStoreTest.class);
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
     * Test of getInstance method, of class SipSessionStore.
     */
    public void testGetInstance() {
        System.out.println("getInstance");
        SipSessionStore expResult = null;
        SipSessionStore result = SipSessionStore.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of put method, of class SipSessionStore.
     */
    public void testPut() {
        System.out.println("put");
        SipSessionImpl sipSession = null;
        SipSessionStore instance = null;
        instance.put(sipSession);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class SipSessionStore.
     */
    public void testGet() {
        System.out.println("get");
        String id = "";
        SipSessionStore instance = null;
        SipSession expResult = null;
        SipSession result = instance.get(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
