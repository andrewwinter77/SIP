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
public class TelUriTest {

    @Test
    public void testEquivalence() throws ParseException {
        
        final TelUrl uriA = (TelUrl) Uri.parse("tel:+358-555-1234567;postd=pp22");
        final TelUrl uriB = (TelUrl) Uri.parse("tel:+358-555-1234567;POSTD=PP22");
        
        Assert.assertEquals(uriA.toSipUri("foo.com", false).toString(), "sip:+358-555-1234567;postd=pp22@foo.com;user=phone");
        Assert.assertEquals(uriB.toSipUri("foo.com", false).toString(), "sip:+358-555-1234567;postd=pp22@foo.com;user=phone");
    }

    @Test
    public void testEquivalence2() throws ParseException {
        
        final TelUrl uriA = (TelUrl) Uri.parse("tel:+358-555-1234567;tsp=a.b;phone-context=5");
        final TelUrl uriB = (TelUrl) Uri.parse("tel:+358-555-1234567;phone-context=5;tsp=a.b");
        
        Assert.assertEquals(uriA.toSipUri("foo.com", false).toString(), "sip:+358-555-1234567;phone-context=5;tsp=a.b@foo.com;user=phone");
        Assert.assertEquals(uriB.toSipUri("foo.com", false).toString(), "sip:+358-555-1234567;phone-context=5;tsp=a.b@foo.com;user=phone");
    }
}
