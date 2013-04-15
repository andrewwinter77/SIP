package org.andrewwinter.jsr289.api;

import javax.servlet.ServletException;
import javax.servlet.sip.Parameterable;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletRequest;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.andrewwinter.sip.parser.SipMessage;
import org.andrewwinter.sip.parser.SipRequest;

/**
 *
 * @author andrew
 */
public class SipFactoryImplTest extends TestCase {

    private final static String MESSAGE = "MESSAGE sip:JSR289_TCK@localhost:5060 SIP/2.0\r\n"
            + "Servlet-Name: SipFactory\r\n"
            + "To: \"JSR289_TCK\" <sip:JSR289_TCK@localhost:5060>\r\n"
            + "Method-Name: testCreateParameterable001\r\n"
            + "From: \"Alice\" <sip:alice@192.168.1.65:5071>;tag=1874096070\r\n"
            + "Via: SIP/2.0/UDP 192.168.1.65:5071;branch=z9hG4bK3c99483f577417a2b5a7f24d0f4e2c7f\r\n"
            + "Call-ID: 2aaef3d0ee487092310d10c00062ff56@192.168.1.65\r\n"
            + "Content-Length: 0\r\n"
            + "Route: \"JSR289_TCK\" <sip:JSR289_TCK@localhost:5060;lr>,<sip:localhost:5060;transport=udp;lr>\r\n"
            + "Max-Forwards: 5\r\n"
            + "Application-Name: com.bea.sipservlet.tck.apps.apitestapp\r\n"
            + "CSeq: 1 MESSAGE\r\n"
            + "\r\n";

    public static Test suite() {
        TestSuite suite = new TestSuite(SipFactoryImplTest.class);
        return suite;
    }

    public void testCreateParameterable001() throws ServletException {
        final SipFactory sf = new SipFactoryImpl(null, null, null);
        final SipRequest request = (SipRequest) SipMessage.parse(MESSAGE);
        final SipServletRequest servletRequest = new OutboundSipServletRequestImpl(request);
        final Parameterable hdr = servletRequest.getParameterableHeader("From");
        final Parameterable param = sf.createParameterable(hdr.toString());
        Assert.assertNotNull(param);
        Assert.assertTrue(param.equals(hdr));
    }
}
