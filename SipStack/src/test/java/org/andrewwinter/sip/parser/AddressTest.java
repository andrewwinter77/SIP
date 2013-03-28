package org.andrewwinter.sip.parser;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author andrewwinter77
 */
public class AddressTest {

//    "Mr. Watson" <sip:watson@worcester.bell-telephone.com>;q=0.7; expires=3600
//            
//            <sip:alice@atlanta.com>
//
//<sip:alice@atlanta.com>;expires=3600
//        
//        Carol <sip:carol@chicago.com>
//        
//        "Bob" <sips:bob@biloxi.com> ;tag=a48s
//                
//                sip:+12125551212@phone2net.com;tag=887s
//                        
//                        Anonymous <sip:c8oqz84zk7z@privacy.org>;tag=hyh8
//                                
//                                
//                                <sip:carol@chicago.com>
//                                
//                                sip:callee@domain.com
//                                
//                                <sip:p1.example.com;lr>
//                                
//                                <sip:services.example.com;lr;unknownwith=value;unknown-no-value>
//                                
//                                "Quoted string \"\"" <sip:jdrosen@example.com> ; newparam =" + CRLF +
//      "      newvalue ;" + CRLF +
//      "  secondparam ; q = 0.33
//
// sip:alice@atlanta.com?priority=urgent&subject=project%20x
//                                
    
    /**
     *
     */
    @Test
    public void testAddress() throws ParseException {
        final Address address = Address.parse("\"Mr. \\\"Watson\\\"      \" <sip:watson@worcester.bell-telephone.com>;q=0.7; expires=3600    ;flagparam");
        Assert.assertEquals("0.7", address.getParameter("q"));
        Assert.assertEquals("3600", address.getParameter("expires"));
        Assert.assertEquals("", address.getParameter("flagparam"));
        Assert.assertEquals("Mr. \\\"Watson\\\"", address.getDisplayName());
    }

    /**
     *
     */
    @Test
    public void testAddress2() throws ParseException {
        final Address address = Address.parse("Anonymous <sip:c8oqz84zk7z@privacy.org>;flagparam;tag=hyh8");
        Assert.assertEquals("hyh8", address.getTag());
        Assert.assertNull(address.getParameter("nonexistentparam"));
        Assert.assertEquals("Anonymous", address.getDisplayName());
    }

    /**
     *
     */
    @Test
    public void testAddress3() throws ParseException {
        final Address address = Address.parse("sip:vivekg@chair-dnrc.example.com ;   tag    = 1918181833n");
        Assert.assertEquals("1918181833n", address.getTag());
        Assert.assertNull(address.getDisplayName());
    }
    
    /**
     *
     */
    @Test
    public void testAddress4() throws ParseException {
        final Address address = Address.parse("sip:vivekg@chair-dnrc.example.com");
        Assert.assertNull(address.getDisplayName());
    }
    
    private static Address createAddress(final String address) {
        return Address.parse(address);
    }
    
    @Test
    public void testClone001() {
        final Address addr1 = createAddress("\"Jeo\" <sip:joe@example.com:5060;lr>;q=0.6;expires=3601");
        final Address addr2 = (Address) addr1.clone();
        Assert.assertTrue(addr1.equals(addr2) && (addr1 != addr2));
    }

    private static boolean compareAddress(final String add1, final String add2) {
        final Address address1 = createAddress(add1);
        final Address address2 = createAddress(add2);
        return address1.equals(address2);
    }

    @Test
    public void testEquals001() {
        if (!compareAddress("\"Alice\" <sip:%61lice@bea.com;transport=TCP;lr>;q=0.6;expires=3601", "\"Alice02\" <sip:alice@BeA.CoM;Transport=tcp;lr>;q=0.6;expires=3601")) {
            Assert.fail();
        }

        if (!compareAddress("<sip:%61lice@bea.com;transport=TCP;lr>;expires=3601;q=0.6", "<sip:alice@BeA.CoM;Transport=tcp;lr>;q=0.6;expires=3601")) {
            Assert.fail();
        }

        if (!compareAddress("<sip:%61lice@bea.com;transport=TCP;lr>;q=0.6", "<sip:alice@BeA.CoM;Transport=tcp;lr>;q=0.6;expires=3601")) {
            Assert.fail();
        }

        if (compareAddress("<sip:%61lice@bea.com;transport=TCP;lr>;q=0.5", "<sip:alice@BeA.CoM;Transport=tcp;lr>;q=0.6;expires=3601")) {
            Assert.fail();
        }
    }
}
