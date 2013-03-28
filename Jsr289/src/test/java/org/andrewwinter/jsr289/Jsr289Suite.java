package org.andrewwinter.jsr289;

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
        suite.addTest(AddressImplTest.suite());
        suite.addTest(SipApplicationSessionImplTest.suite());
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
