package org.andrewwinter.jsr289.api;

import java.net.MalformedURLException;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Assert;

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
            final URL encoded = sas.encodeURL(url);
            Assert.assertNotNull("URL was unexpectedly null.", encoded);
        }
    }
}
