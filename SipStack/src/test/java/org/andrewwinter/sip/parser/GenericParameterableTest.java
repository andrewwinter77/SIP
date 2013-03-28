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
public class GenericParameterableTest {

    /**
     *
     */
    @Test
    public void testGenericParameterable() throws ParseException {
        GenericParameterable gp = GenericParameterable.parse("\"foo;bar\";param");
        Assert.assertEquals("\"foo;bar\"", gp.getValue());
        Assert.assertEquals("", gp.getParameter("param"));
    }

}
