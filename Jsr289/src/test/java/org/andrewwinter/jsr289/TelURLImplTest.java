/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.jsr289;

import org.andrewwinter.jsr289.TelURLImpl;
import javax.servlet.sip.URI;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author andrew
 */
public class TelURLImplTest extends TestCase {
    
    public TelURLImplTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(TelURLImplTest.class);
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
     * Test of getPhoneNumber method, of class TelURLImpl.
     */
    public void testGetPhoneNumber() {
        System.out.println("getPhoneNumber");
        TelURLImpl instance = null;
        String expResult = "";
        String result = instance.getPhoneNumber();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPhoneContext method, of class TelURLImpl.
     */
    public void testGetPhoneContext() {
        System.out.println("getPhoneContext");
        TelURLImpl instance = null;
        String expResult = "";
        String result = instance.getPhoneContext();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPhoneNumber method, of class TelURLImpl.
     */
    public void testSetPhoneNumber_String() {
        System.out.println("setPhoneNumber");
        String string = "";
        TelURLImpl instance = null;
        instance.setPhoneNumber(string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPhoneNumber method, of class TelURLImpl.
     */
    public void testSetPhoneNumber_String_String() {
        System.out.println("setPhoneNumber");
        String string = "";
        String string1 = "";
        TelURLImpl instance = null;
        instance.setPhoneNumber(string, string1);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isGlobal method, of class TelURLImpl.
     */
    public void testIsGlobal() {
        System.out.println("isGlobal");
        TelURLImpl instance = null;
        boolean expResult = false;
        boolean result = instance.isGlobal();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clone method, of class TelURLImpl.
     */
    public void testClone() {
        System.out.println("clone");
        TelURLImpl instance = null;
        URI expResult = null;
        URI result = instance.clone();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
