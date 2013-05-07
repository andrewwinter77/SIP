package org.andrewwinter.sip.webapp;

import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.sip.Address;
import javax.servlet.sip.B2buaHelper;
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
public class ClickToCallServlet extends SipServlet {

    @Resource
    private SipFactory sf;

    @Override
    protected void doSuccessResponse(final SipServletResponse response) throws ServletException, IOException {
        
        final boolean responseFromSubscriber = response.getSession().getAttribute(Util.IS_SUBSCRIBER_KEY) != null;
        
        if ("INVITE".equals(response.getMethod()) && responseFromSubscriber) {

            if (response.getRequest().isInitial()) {
            
                response.createAck().send();

                final Address subscriberAddress = (Address) response.getSession().getAttribute(Util.SUBSCRIBER_ADDRESS_KEY);
                final Address calleeAddress = (Address) response.getSession().getAttribute(Util.CALLEE_ADDRESS_KEY);

                final SipServletRequest calleeInvite = sf.createRequest(
                                                            response.getApplicationSession(),
                                                            "INVITE",
                                                            subscriberAddress,
                                                            calleeAddress);

                calleeInvite.getB2buaHelper().linkSipSessions(
                        calleeInvite.getSession(),
                        response.getSession());

                calleeInvite.send();
            } else {
                
                final B2buaHelper helper = response.getRequest().getB2buaHelper();
                final SipSession bobSession = helper.getLinkedSession(response.getSession());
                
                final SipServletResponse okFromCallee = (SipServletResponse) bobSession.getAttribute(Util.OK_KEY);
                final SipServletRequest ackToCallee = okFromCallee.createAck();
                ackToCallee.setContent(response.getRawContent(), response.getContentType());
                ackToCallee.send();
                
                final SipServletRequest ackToSubscriber = response.createAck();
                ackToSubscriber.send();
            }
            
        } else if ("INVITE".equals(response.getMethod()) && !responseFromSubscriber) {
            
            response.getSession().setAttribute(Util.OK_KEY, response);
            
            final B2buaHelper helper = response.getRequest().getB2buaHelper();
            final SipSession subscriberSession = helper.getLinkedSession(response.getSession());
            
            final SipServletRequest reInviteToSubscriber = subscriberSession.createRequest("INVITE");
            reInviteToSubscriber.setContent(response.getRawContent(), response.getContentType());
            reInviteToSubscriber.send();
            
        } else {
            super.doSuccessResponse(response);
        }
    }

    @Override
    protected void doErrorResponse(SipServletResponse ssr) throws ServletException, IOException {
        super.doErrorResponse(ssr); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doBye(final SipServletRequest bye) throws ServletException, IOException {
        final SipSession otherSession = bye.getB2buaHelper().getLinkedSession(bye.getSession());
        bye.createResponse(SipServletResponse.SC_OK).send();
        
        if (otherSession != null) {
            final SipServletRequest byeToOtherParty = otherSession.createRequest("BYE");
            byeToOtherParty.send();

        }
    }

    @Override
    protected void doProvisionalResponse(final SipServletResponse response) throws ServletException, IOException {
//        if ("INVITE".equals(response.getMethod() && response.
    }
}

