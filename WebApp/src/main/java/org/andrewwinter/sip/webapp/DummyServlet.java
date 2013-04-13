package org.andrewwinter.sip.webapp;

import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;


/**
 * Implements Call Flow IV from RFC 3725. That is,
 * <pre>
 * {@code 
 *    A                 Controller                  B
 *    |(1) INVITE offer1     |                      |
 *    |no media              |                      |
 *    |<---------------------|                      |
 *    |(2) 200 answer1       |                      |
 *    |no media              |                      |
 *    |--------------------->|                      |
 *    |(3) ACK               |                      |
 *    |<---------------------|                      |
 *    |                      |(4) INVITE no SDP     |
 *    |                      |--------------------->|
 *    |                      |(5) 200 OK offer2     |
 *    |                      |<---------------------|
 *    |(6) INVITE offer2'    |                      |
 *    |<---------------------|                      |
 *    |(7) 200 answer2'      |                      |
 *    |--------------------->|                      |
 *    |                      |(8) ACK answer2       |
 *    |                      |--------------------->|
 *    |(9) ACK               |                      |
 *    |<---------------------|                      |
 *    |(10) RTP              |                      |
 *    |.............................................|
 * }
 * </pre>
 * @author andrew
 */
@javax.servlet.sip.annotation.SipServlet
public class DummyServlet extends SipServlet {

    @Resource
    private SipFactory sf;

    @Override
    protected void doSuccessResponse(final SipServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doErrorResponse(SipServletResponse ssr) throws ServletException, IOException {
        super.doErrorResponse(ssr); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doBye(final SipServletRequest bye) throws ServletException, IOException {
        final SipSession otherSession = bye.getB2buaHelper().getLinkedSession(bye.getSession());
        
        final SipServletRequest byeToOtherParty = otherSession.createRequest("BYE");
        byeToOtherParty.send();
        
        bye.createResponse(SipServletResponse.SC_OK).send();
    }

    @Override
    protected void doProvisionalResponse(final SipServletResponse response) throws ServletException, IOException {
//        if ("INVITE".equals(response.getMethod() && response.
    }
}

