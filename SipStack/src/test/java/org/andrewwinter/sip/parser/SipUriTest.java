/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.sip.parser;

import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author andrewwinter77
 */
public class SipUriTest {
   
    // The URIs within each of the following sets are equivalent:
    private static final String[][] EQUAL = new String[][] {
        new String[] {
            "sip:%61lice@atlanta.com;transport=TCP",
            "sip:alice@AtLanTa.CoM;Transport=tcp"
        },
        new String[] {
            "sip:carol@chicago.com",
            "sip:carol@chicago.com;newparam=5"
        },
        new String[] {
            "sip:carol@chicago.com",
            "sip:carol@chicago.com;security=on"
        },
        new String[] {
            "sip:carol@chicago.com;newparam=5",
            "sip:carol@chicago.com;security=on"
        },
        new String[] {
            "sip:biloxi.com;transport=tcp;method=REGISTER?to=sip:bob%40biloxi.com",
            "sip:biloxi.com;method=REGISTER;transport=tcp?to=sip:bob%40biloxi.com"
        },
        new String[] {
            "sip:alice@atlanta.com?subject=project%20x&priority=urgent",
            "sip:alice@atlanta.com?priority=urgent&subject=project%20x"
        },
        new String[] {
            "sip:carol@chicago.com",
            "sip:carol@chicago.com;security=on"
        },
        new String[] {
            "sip:carol@chicago.com",
            "sip:carol@chicago.com;security=off"
        },
    };
    
    // The URIs within each of the following sets are not equivalent:
    private static final String[][] NOT_EQUAL = new String[][] {
        new String[] {
            "SIP:ALICE@AtLanTa.CoM;Transport=udp",
            "sip:alice@AtLanTa.CoM;Transport=UDP"
        },
        new String[] {
            "sip:bob@biloxi.com",
            "sip:bob@biloxi.com:5060"
        },
        new String[] {
            "sip:bob@biloxi.com",
            "sip:bob@biloxi.com;transport=udp"
        },
        new String[] {
            "sip:bob@biloxi.com",
            "sip:bob@biloxi.com:6000;transport=tcp"
        },
        new String[] {
            "sip:carol@chicago.com",
            "sip:carol@chicago.com?Subject=next%20meeting"
        },
        new String[] {
            "sip:bob@phone21.boxesbybob.com",
            "sip:bob@192.0.2.4"
        },
        new String[] {
            "sip:carol@chicago.com;security=on",
            "sip:carol@chicago.com;security=off"
        },
        new String[] {
            "sip:+358-555-1234567;postd=pp22@foo.com;user=phone",
            "sip:+358-555-1234567;POSTD=PP22@foo.com;user=phone"
        },
        new String[] {
            "sip:+358-555-1234567;postd=pp22;isub=1411@foo.com;user=phone",
            "sip:+358-555-1234567;isub=1411;postd=pp22@foo.com;user=phone"
        }
    };
    
    
    @Test
    public void testEqual() throws ParseException {
        
        for (final String[] pair : EQUAL) {
            SipUri uriA = (SipUri) Uri.parse(pair[0]);
            SipUri uriB = (SipUri) Uri.parse(pair[1]);
            System.out.println(uriA);
            System.out.println(uriB);
            Assert.assertTrue(uriA.toString() + " and " + uriB.toString() + " are supposed to be equal.", uriA.equalsUsingComparisonRules(uriB));
            Assert.assertTrue(uriA.toString() + " and " + uriB.toString() + " are supposed to be equal.", uriB.equalsUsingComparisonRules(uriA));
        }
    }

    @Test
    public void testNotEqual() throws ParseException {
        
        for (final String[] pair : NOT_EQUAL) {
            SipUri uriA = (SipUri) Uri.parse(pair[0]);
            SipUri uriB = (SipUri) Uri.parse(pair[1]);
            System.out.println(uriA);
            System.out.println(uriB);
            Assert.assertFalse(uriA.toString() + " and " + uriB.toString() + " are not supposed to be equal.", uriA.equalsUsingComparisonRules(uriB));
            Assert.assertFalse(uriA.toString() + " and " + uriB.toString() + " are not supposed to be equal.", uriB.equalsUsingComparisonRules(uriA));
        }
    }
}
