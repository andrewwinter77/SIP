package org.andrewwinter.jsr289;

import java.util.Iterator;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author andrew
 */
public class AbstractParameterableTest extends TestCase {
    
    public AbstractParameterableTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(AbstractParameterableTest.class);
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
     * Test of getValue method, of class ParameterableImpl.
     */
    public void testGetValue() {
        System.out.println("getValue");
        AbstractParameterable instance = null;
        String expResult = "";
        String result = instance.getValue();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setValue method, of class ParameterableImpl.
     */
    public void testSetValue() {
        System.out.println("setValue");
        String string = "";
        AbstractParameterable instance = null;
        instance.setValue(string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameter method, of class ParameterableImpl.
     */
    public void testGetParameter() {
        System.out.println("getParameter");
        String string = "";
        AbstractParameterable instance = null;
        String expResult = "";
        String result = instance.getParameter(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setParameter method, of class ParameterableImpl.
     */
    public void testSetParameter() {
        System.out.println("setParameter");
        String name = "";
        String value = "";
        AbstractParameterable instance = null;
        instance.setParameter(name, value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeParameter method, of class ParameterableImpl.
     */
    public void testRemoveParameter() {
        System.out.println("removeParameter");
        String name = "";
        AbstractParameterable instance = null;
        instance.removeParameter(name);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameterNames method, of class ParameterableImpl.
     */
    public void testGetParameterNames() {
        System.out.println("getParameterNames");
        AbstractParameterable instance = null;
        Iterator expResult = null;
        Iterator result = instance.getParameterNames();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameters method, of class ParameterableImpl.
     */
    public void testGetParameters() {
        System.out.println("getParameters");
        AbstractParameterable instance = null;
        Set expResult = null;
        Set result = instance.getParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clone method, of class ParameterableImpl.
     */
    public void testClone() {
        System.out.println("clone");
        AbstractParameterable instance = null;
        Object expResult = null;
        Object result = instance.clone();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class ParameterableImplImpl extends AbstractParameterable {

        public ParameterableImplImpl() {
            super(null, null);
        }

        @Override
        public Object clone() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
