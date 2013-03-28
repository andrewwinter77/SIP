/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.jsr289;

import org.andrewwinter.jsr289.ApplicationSessionStore;
import javax.servlet.sip.SipApplicationSession;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author andrew
 */
public class ApplicationSessionStoreTest extends TestCase {
    
    public ApplicationSessionStoreTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ApplicationSessionStoreTest.class);
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
     * Test of getInstance method, of class ApplicationSessionStore.
     */
    public void testGetInstance() {
        System.out.println("getInstance");
        ApplicationSessionStore expResult = null;
        ApplicationSessionStore result = ApplicationSessionStore.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of put method, of class ApplicationSessionStore.
     */
    public void testPut() {
        System.out.println("put");
        SipApplicationSession appSession = null;
        ApplicationSessionStore instance = null;
        instance.put(appSession);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class ApplicationSessionStore.
     */
    public void testGet() {
        System.out.println("get");
        String id = "";
        ApplicationSessionStore instance = null;
        SipApplicationSession expResult = null;
        SipApplicationSession result = instance.get(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
