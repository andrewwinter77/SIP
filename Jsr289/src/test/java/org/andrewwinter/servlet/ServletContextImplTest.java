/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.servlet;

import org.andrewwinter.servlet.ServletContextImpl;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author andrew
 */
public class ServletContextImplTest extends TestCase {
    
    public ServletContextImplTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ServletContextImplTest.class);
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
     * Test of getContextPath method, of class ServletContextImpl.
     */
    public void testGetContextPath() {
        System.out.println("getContextPath");
        ServletContextImpl instance = new ServletContextImpl();
        String expResult = "";
        String result = instance.getContextPath();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getContext method, of class ServletContextImpl.
     */
    public void testGetContext() {
        System.out.println("getContext");
        String string = "";
        ServletContextImpl instance = new ServletContextImpl();
        ServletContext expResult = null;
        ServletContext result = instance.getContext(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMajorVersion method, of class ServletContextImpl.
     */
    public void testGetMajorVersion() {
        System.out.println("getMajorVersion");
        ServletContextImpl instance = new ServletContextImpl();
        int expResult = 0;
        int result = instance.getMajorVersion();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMinorVersion method, of class ServletContextImpl.
     */
    public void testGetMinorVersion() {
        System.out.println("getMinorVersion");
        ServletContextImpl instance = new ServletContextImpl();
        int expResult = 0;
        int result = instance.getMinorVersion();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMimeType method, of class ServletContextImpl.
     */
    public void testGetMimeType() {
        System.out.println("getMimeType");
        String string = "";
        ServletContextImpl instance = new ServletContextImpl();
        String expResult = "";
        String result = instance.getMimeType(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getResourcePaths method, of class ServletContextImpl.
     */
    public void testGetResourcePaths() {
        System.out.println("getResourcePaths");
        String string = "";
        ServletContextImpl instance = new ServletContextImpl();
        Set expResult = null;
        Set result = instance.getResourcePaths(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getResource method, of class ServletContextImpl.
     */
    public void testGetResource() throws Exception {
        System.out.println("getResource");
        String string = "";
        ServletContextImpl instance = new ServletContextImpl();
        URL expResult = null;
        URL result = instance.getResource(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getResourceAsStream method, of class ServletContextImpl.
     */
    public void testGetResourceAsStream() {
        System.out.println("getResourceAsStream");
        String string = "";
        ServletContextImpl instance = new ServletContextImpl();
        InputStream expResult = null;
        InputStream result = instance.getResourceAsStream(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRequestDispatcher method, of class ServletContextImpl.
     */
    public void testGetRequestDispatcher() {
        System.out.println("getRequestDispatcher");
        String string = "";
        ServletContextImpl instance = new ServletContextImpl();
        RequestDispatcher expResult = null;
        RequestDispatcher result = instance.getRequestDispatcher(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNamedDispatcher method, of class ServletContextImpl.
     */
    public void testGetNamedDispatcher() {
        System.out.println("getNamedDispatcher");
        String string = "";
        ServletContextImpl instance = new ServletContextImpl();
        RequestDispatcher expResult = null;
        RequestDispatcher result = instance.getNamedDispatcher(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getServlet method, of class ServletContextImpl.
     */
    public void testGetServlet() throws Exception {
        System.out.println("getServlet");
        String string = "";
        ServletContextImpl instance = new ServletContextImpl();
        Servlet expResult = null;
        Servlet result = instance.getServlet(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getServlets method, of class ServletContextImpl.
     */
    public void testGetServlets() {
        System.out.println("getServlets");
        ServletContextImpl instance = new ServletContextImpl();
        Enumeration expResult = null;
        Enumeration result = instance.getServlets();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getServletNames method, of class ServletContextImpl.
     */
    public void testGetServletNames() {
        System.out.println("getServletNames");
        ServletContextImpl instance = new ServletContextImpl();
        Enumeration expResult = null;
        Enumeration result = instance.getServletNames();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of log method, of class ServletContextImpl.
     */
    public void testLog_String() {
        System.out.println("log");
        String string = "";
        ServletContextImpl instance = new ServletContextImpl();
        instance.log(string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of log method, of class ServletContextImpl.
     */
    public void testLog_Exception_String() {
        System.out.println("log");
        Exception excptn = null;
        String string = "";
        ServletContextImpl instance = new ServletContextImpl();
        instance.log(excptn, string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of log method, of class ServletContextImpl.
     */
    public void testLog_String_Throwable() {
        System.out.println("log");
        String string = "";
        Throwable thrwbl = null;
        ServletContextImpl instance = new ServletContextImpl();
        instance.log(string, thrwbl);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRealPath method, of class ServletContextImpl.
     */
    public void testGetRealPath() {
        System.out.println("getRealPath");
        String string = "";
        ServletContextImpl instance = new ServletContextImpl();
        String expResult = "";
        String result = instance.getRealPath(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getServerInfo method, of class ServletContextImpl.
     */
    public void testGetServerInfo() {
        System.out.println("getServerInfo");
        ServletContextImpl instance = new ServletContextImpl();
        String expResult = "";
        String result = instance.getServerInfo();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInitParameter method, of class ServletContextImpl.
     */
    public void testGetInitParameter() {
        System.out.println("getInitParameter");
        String string = "";
        ServletContextImpl instance = new ServletContextImpl();
        String expResult = "";
        String result = instance.getInitParameter(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInitParameterNames method, of class ServletContextImpl.
     */
    public void testGetInitParameterNames() {
        System.out.println("getInitParameterNames");
        ServletContextImpl instance = new ServletContextImpl();
        Enumeration expResult = null;
        Enumeration result = instance.getInitParameterNames();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAttribute method, of class ServletContextImpl.
     */
    public void testGetAttribute() {
        System.out.println("getAttribute");
        String string = "";
        ServletContextImpl instance = new ServletContextImpl();
        Object expResult = null;
        Object result = instance.getAttribute(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAttributeNames method, of class ServletContextImpl.
     */
    public void testGetAttributeNames() {
        System.out.println("getAttributeNames");
        ServletContextImpl instance = new ServletContextImpl();
        Enumeration expResult = null;
        Enumeration result = instance.getAttributeNames();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setAttribute method, of class ServletContextImpl.
     */
    public void testSetAttribute() {
        System.out.println("setAttribute");
        String string = "";
        Object o = null;
        ServletContextImpl instance = new ServletContextImpl();
        instance.setAttribute(string, o);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeAttribute method, of class ServletContextImpl.
     */
    public void testRemoveAttribute() {
        System.out.println("removeAttribute");
        String string = "";
        ServletContextImpl instance = new ServletContextImpl();
        instance.removeAttribute(string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getServletContextName method, of class ServletContextImpl.
     */
    public void testGetServletContextName() {
        System.out.println("getServletContextName");
        ServletContextImpl instance = new ServletContextImpl();
        String expResult = "";
        String result = instance.getServletContextName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
