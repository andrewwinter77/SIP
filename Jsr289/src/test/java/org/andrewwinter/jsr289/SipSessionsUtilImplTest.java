/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.jsr289;

import org.andrewwinter.jsr289.SipSessionsUtilImpl;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipSession;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author andrew
 */
public class SipSessionsUtilImplTest extends TestCase {
    
    public SipSessionsUtilImplTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SipSessionsUtilImplTest.class);
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
     * Test of getApplicationSessionById method, of class SipSessionsUtilImpl.
     */
    public void testGetApplicationSessionById() {
        System.out.println("getApplicationSessionById");
        String id = "";
        SipSessionsUtilImpl instance = new SipSessionsUtilImpl(null);
        SipApplicationSession expResult = null;
        SipApplicationSession result = instance.getApplicationSessionById(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getApplicationSessionByKey method, of class SipSessionsUtilImpl.
     */
    public void testGetApplicationSessionByKey() {
        System.out.println("getApplicationSessionByKey");
        String string = "";
        boolean bln = false;
        SipSessionsUtilImpl instance = new SipSessionsUtilImpl(null);
        SipApplicationSession expResult = null;
        SipApplicationSession result = instance.getApplicationSessionByKey(string, bln);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCorrespondingSipSession method, of class SipSessionsUtilImpl.
     */
    public void testGetCorrespondingSipSession() {
        System.out.println("getCorrespondingSipSession");
        SipSession ss = null;
        String string = "";
        SipSessionsUtilImpl instance = new SipSessionsUtilImpl(null);
        SipSession expResult = null;
        SipSession result = instance.getCorrespondingSipSession(ss, string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
