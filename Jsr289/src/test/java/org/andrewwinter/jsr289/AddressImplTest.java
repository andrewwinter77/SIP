/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.jsr289;

import javax.servlet.sip.Address;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.URI;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author andrew
 */
public class AddressImplTest extends TestCase {

    private static final String ORIGINAL_DISPLAYNAME = "Andrew";
    private static final String ALTERNATIVE_DISPLAYNAME = "Bob";
    private static final String ORIGINAL_URL = "sip:foo@bar.com";
    private static final String ALTERNATIVE_URL = "tel:bar@foo.com";

    private static final SipFactory SIP_FACTORY = new SipFactoryImpl(null, null, null);
    
    public AddressImplTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(AddressImplTest.class);
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

    private static URI createUri(final String uri) {
        try {
            return SIP_FACTORY.createURI(uri);
        } catch (ServletParseException e) {
            fail("Exception while creating URI.");
            return null;
        }
    }

    private static AddressImpl createAddress() {
        return createAddress(ORIGINAL_DISPLAYNAME + "<" + ORIGINAL_URL + ">");
    }

    private static AddressImpl createAddress(final String address) {
        try {
            return (AddressImpl) SIP_FACTORY.createAddress(address);
        } catch (Exception e) {
            fail("Exception while creating valid address.");
            return null;
        }
    }

    private static AddressImpl createWildcardAddress() {
        try {
            return (AddressImpl) SIP_FACTORY.createAddress("*");
        } catch (Exception e) {
            fail("Exception while creating valid address.");
            return null;
        }
    }

    public void testCreateAddress() {
        createAddress();
    }

    public void testCreateWildcardAddress() {
        createWildcardAddress();
    }

    /**
     * Test of getDisplayName method, of class AddressImpl.
     */
    public void testGetDisplayName() {
        final AddressImpl instance = createAddress();
        assertEquals(ORIGINAL_DISPLAYNAME, instance.getDisplayName());
    }

    /**
     * Test of setDisplayName method, of class AddressImpl.
     */
    public void testSetDisplayName() {
        final AddressImpl instance = createAddress();
        instance.setDisplayName(ALTERNATIVE_DISPLAYNAME);
        assertEquals(ALTERNATIVE_DISPLAYNAME, instance.getDisplayName());
    }

    /**
     * Test of getURI method, of class AddressImpl.
     */
    public void testGetURI() {
        final AddressImpl instance = createAddress();
        assertEquals(ORIGINAL_URL, instance.getURI().toString());
    }

    /**
     * Test of setURI method, of class AddressImpl.
     */
    public void testSetURI() throws Exception {
        AddressImpl instance = createWildcardAddress();
        try {
            instance.setURI(createUri(ORIGINAL_URL));
            fail();
        } catch (IllegalStateException e) {
        }

        instance = createAddress();
        instance.setURI(createUri(ALTERNATIVE_URL));
        assertEquals(ALTERNATIVE_URL, instance.getURI().toString());
    }

    /**
     * Test of isWildcard method, of class AddressImpl.
     */
    public void testIsWildcard() {
        assertTrue(createWildcardAddress().isWildcard());
        assertFalse(createAddress().isWildcard());
    }

    public void testSetQ101() {
        Address addr1 = createAddress("\"Jeo\" <sip:joe@example.com:5060;lr>;q=0.6;expires=3601");
        try {
            addr1.setQ(-1.2F);
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            addr1.setQ(1.1F);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    public void testClone001() {
        final AddressImpl addr1 = createAddress("\"Jeo\" <sip:joe@example.com:5060;lr>;q=0.6;expires=3601");
        final Address addr2 = (Address) addr1.clone();
        assertTrue(addr1.equals(addr2) && (addr1 != addr2));
    }

    private static boolean compareAddress(final String add1, final String add2) {
        final AddressImpl address1 = createAddress(add1);
        final AddressImpl address2 = createAddress(add2);

        return address1.equals(address2);
    }

    public void testEquals001() {

        if (!compareAddress("\"Alice\" <sip:%61lice@bea.com;transport=TCP;lr>;q=0.6;expires=3601", "\"Alice02\" <sip:alice@BeA.CoM;Transport=tcp;lr>;q=0.6;expires=3601")) {
            fail();
        }

        if (!compareAddress("<sip:%61lice@bea.com;transport=TCP;lr>;expires=3601;q=0.6", "<sip:alice@BeA.CoM;Transport=tcp;lr>;q=0.6;expires=3601")) {
            fail();
        }

        if (!compareAddress("<sip:%61lice@bea.com;transport=TCP;lr>;q=0.6", "<sip:alice@BeA.CoM;Transport=tcp;lr>;q=0.6;expires=3601")) {
            fail();
        }

        if (compareAddress("<sip:%61lice@bea.com;transport=TCP;lr>;q=0.5", "<sip:alice@BeA.CoM;Transport=tcp;lr>;q=0.6;expires=3601")) {
            fail();
        }
    }
}
