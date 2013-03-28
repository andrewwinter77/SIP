package org.andrewwinter.sip.parser;

import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author andrewwinter77
 */
public class UtilTest {

    /**
     *
     */
    @Test
    public void testFindEndOfQuotedString001() {
        Assert.assertEquals(7, Util.findEndOfQuotedString("\"123456\""));
    }

    /**
     *
     */
    @Test
    public void testFindEndOfQuotedString002() {
        try {
            Util.findEndOfQuotedString("\"123456");
            Assert.fail("Exception expected because there was no end to the quoted string.");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     *
     */
    @Test
    public void testSafeSplit001() {
        final List<String> parts = Util.safeSplit("xyz;foo=\"a,b;c\"");
        Assert.assertEquals(1, parts.size());
        Assert.assertEquals("xyz;foo=\"a,b;c\"", parts.get(0));
    }

    /**
     *
     */
    @Test
    public void testSafeSplit002() {
        final List<String> parts = Util.safeSplit("xyz;foo=\"a,b;c\";bar,  abc;ubw=\";\"");
        Assert.assertEquals(2, parts.size());
        Assert.assertEquals("xyz;foo=\"a,b;c\";bar", parts.get(0));
        Assert.assertEquals("abc;ubw=\";\"", parts.get(1));
    }
}
