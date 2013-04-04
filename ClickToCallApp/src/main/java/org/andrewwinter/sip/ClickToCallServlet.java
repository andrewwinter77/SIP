package org.andrewwinter.sip;


import java.io.IOException;
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
    
    @Override
    protected void doSuccessResponse(final SipServletResponse response) throws ServletException, IOException {
        
        final boolean responseFromAlice = response.getSession().getAttribute(Util.IS_ALICE_KEY) != null;
        
        if ("INVITE".equals(response.getMethod()) && responseFromAlice) {

            if (response.getRequest().isInitial()) {
            
                response.createAck().send();

                // TODO: Inject SipFactory when we're capable
                final SipFactory sf = (SipFactory) getServletContext().getAttribute(SipServlet.SIP_FACTORY);


                final Address aliceAddress = (Address) response.getSession().getAttribute(Util.ALICE_ADDRESS_KEY);
                final Address bobAddress = (Address) response.getSession().getAttribute(Util.BOB_ADDRESS_KEY);

                final SipServletRequest bobInvite = sf.createRequest(
                        response.getApplicationSession(),
                        "INVITE",
                        aliceAddress,
                        bobAddress);

                bobInvite.getB2buaHelper().linkSipSessions(
                        bobInvite.getSession(),
                        response.getSession());

                bobInvite.send();
            } else {
                
                final B2buaHelper helper = response.getRequest().getB2buaHelper();
                final SipSession bobSession = helper.getLinkedSession(response.getSession());
                
                final SipServletResponse okFromBob = (SipServletResponse) bobSession.getAttribute(Util.OK_KEY);
                final SipServletRequest ackToBob = okFromBob.createAck();
                ackToBob.setContent(response.getRawContent(), response.getContentType());
                ackToBob.send();
                
                final SipServletRequest ackToAlice = response.createAck();
                ackToAlice.send();
            }
            
        } else if ("INVITE".equals(response.getMethod()) && !responseFromAlice) {
            
            response.getSession().setAttribute(Util.OK_KEY, response);
            
            final B2buaHelper helper = response.getRequest().getB2buaHelper();
            final SipSession aliceSession = helper.getLinkedSession(response.getSession());
            
            final SipServletRequest reInviteToAlice = aliceSession.createRequest("INVITE");
            reInviteToAlice.setContent(response.getRawContent(), response.getContentType());
            reInviteToAlice.send();
            
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
        
        final SipServletRequest byeToOtherParty = otherSession.createRequest("BYE");
        byeToOtherParty.send();
        
        bye.createResponse(SipServletResponse.SC_OK).send();
    }

    @Override
    protected void doProvisionalResponse(final SipServletResponse response) throws ServletException, IOException {
//        if ("INVITE".equals(response.getMethod() && response.
    }
}

