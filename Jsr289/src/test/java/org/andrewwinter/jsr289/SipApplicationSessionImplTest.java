/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.jsr289;

import java.net.MalformedURLException;
import org.andrewwinter.jsr289.SipApplicationSessionImpl;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpSession;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipApplicationSession.Protocol;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.URI;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author andrew
 */
public class SipApplicationSessionImplTest extends TestCase {
    
    public SipApplicationSessionImplTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SipApplicationSessionImplTest.class);
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
     * Test of encodeURI method, of class SipApplicationSessionImpl.
     */
//    public void testEncodeURI() {
//        System.out.println("encodeURI");
//        URI uri = null;
//        SipApplicationSessionImpl instance = null;
//        instance.encodeURI(uri);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of encodeURL method, of class SipApplicationSessionImpl.
     */
    public void testEncodeURL() throws MalformedURLException {
        final URL[] urls = new URL[] {
            new URL("http://www.google.com"),
            new URL("http://www.google.com/"),
            new URL("http://www.google.com/index.html"),
            new URL("http://www.google.com?foo"),
            new URL("http://www.google.com?foo&bar")
        };
        final SipApplicationSessionImpl sas = SipApplicationSessionImpl.create(null);
        for (final URL url : urls) {
            System.out.println(sas.encodeURL(url));
        }
    }
}
