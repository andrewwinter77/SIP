/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.jsr289;

import org.andrewwinter.servlet.ServletContextImplTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author andrew
 */
public class Jsr289Suite extends TestCase {
    
    public Jsr289Suite(String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite("Jsr289Suite");
        suite.addTest(SipURIImplTest.suite());
        suite.addTest(ApplicationSessionStoreTest.suite());
        suite.addTest(AbstractParameterableTest.suite());
        suite.addTest(SipServletResponseImplTest.suite());
        suite.addTest(URIImplTest.suite());
        suite.addTest(SipSessionsUtilImplTest.suite());
        suite.addTest(SipFactoryImplTest.suite());
        suite.addTest(AddressImplTest.suite());
        suite.addTest(SipSessionStoreTest.suite());
        suite.addTest(SipSessionImplTest.suite());
        suite.addTest(SipServletRequestImplTest.suite());
        suite.addTest(SipServletMessageImplTest.suite());
        suite.addTest(SipApplicationSessionImplTest.suite());
        suite.addTest(TelURLImplTest.suite());
        suite.addTest(ServletContextImplTest.suite());
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
}
