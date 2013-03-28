/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.sip.parser;

import junit.framework.Assert;
import org.andrewwinter.sip.SipRequestHandler;
import org.andrewwinter.sip.message.InboundSipRequest;
import org.andrewwinter.sip.message.SipMessageFactory;
import org.andrewwinter.sip.properties.UserAgentProperties;
import org.andrewwinter.sip.transport.CoreJavaServerTransport;
import org.andrewwinter.sip.transport.ServerTransport;
import org.junit.Test;

/**
 *
 * @author andrewwinter77
 */
public class SipMessageFactoryTest implements SipRequestHandler {
    
    /**
     *
     */
    @Test
    public void options() {
        final SipUri fromUri = new SipUri("192.168.1.77");
        fromUri.setUser("andrew");
        final Address from = new Address(fromUri);
        from.setDisplayName("Andrew");

        final UserAgentProperties uaProperties = new UserAgentProperties(from, "andrew");
        
        
        final SipRequest options = SipMessageFactory.createOutOfDialogRequest(
                "OPTIONS", "sip:iptel.org", uaProperties.getFrom().toString(), null, uaProperties.getContact());
        System.out.println(options);
        
        final ServerTransport serverTransport = new CoreJavaServerTransport(this);
        serverTransport.listen();
//        Udp.send(options, "iptel.org", 5060);
    }

    @Test
    public void testForParseException() {
        try {
            final SipRequest options = SipMessageFactory.createOutOfDialogRequest(
                 "OPTIONS", "iptel.org", "sip:from@foo.com", null, Address.parse("sip:contact@foo.com"));
            Assert.fail();
        } catch (ParseException e) {
        }
    }
    
    @Override
    public void doRequest(InboundSipRequest request) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
