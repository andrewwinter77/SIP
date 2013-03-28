package org.andrewwinter.jsr289;

import java.util.Iterator;
import javax.servlet.sip.URI;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.andrewwinter.sip.parser.Uri;

/**
 *
 * @author andrew
 */
public class URIImplTest extends TestCase {
    
    public URIImplTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(URIImplTest.class);
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
     * Test of getRfc3261Uri method, of class URIImpl.
     */
    public void testGetRfc3261Uri() {
        System.out.println("getRfc3261Uri");
        URIImpl instance = null;
        Uri expResult = null;
        Uri result = instance.getRfc3261Uri();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getScheme method, of class URIImpl.
     */
    public void testGetScheme() {
        System.out.println("getScheme");
        URIImpl instance = null;
        String expResult = "";
        String result = instance.getScheme();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isSipURI method, of class URIImpl.
     */
    public void testIsSipURI() {
        System.out.println("isSipURI");
        URIImpl instance = null;
        boolean expResult = false;
        boolean result = instance.isSipURI();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameter method, of class URIImpl.
     */
    public void testGetParameter() {
        System.out.println("getParameter");
        String key = "";
        URIImpl instance = null;
        String expResult = "";
        String result = instance.getParameter(key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setParameter method, of class URIImpl.
     */
    public void testSetParameter() {
        System.out.println("setParameter");
        String name = "";
        String value = "";
        URIImpl instance = null;
        instance.setParameter(name, value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeParameter method, of class URIImpl.
     */
    public void testRemoveParameter() {
        System.out.println("removeParameter");
        String name = "";
        URIImpl instance = null;
        instance.removeParameter(name);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParameterNames method, of class URIImpl.
     */
    public void testGetParameterNames() {
        System.out.println("getParameterNames");
        URIImpl instance = null;
        Iterator expResult = null;
        Iterator result = instance.getParameterNames();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clone method, of class URIImpl.
     */
    public void testClone() {
        System.out.println("clone");
        URIImpl instance = null;
        URI expResult = null;
        URI result = instance.clone();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class URIImplImpl extends URIImpl {

        public URIImplImpl() {
            super(null);
        }

        @Override
        public URI clone() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int hashCode() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean equals(Object obj) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
