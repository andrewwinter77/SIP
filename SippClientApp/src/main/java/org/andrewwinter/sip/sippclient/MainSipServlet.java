package org.andrewwinter.sip.sippclient;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;


@javax.servlet.sip.annotation.SipServlet
public class MainSipServlet extends SipServlet {
    
    @Override
    protected void doInvite(final SipServletRequest invite) throws ServletException, IOException {
        invite.createResponse(SipServletResponse.SC_RINGING).send();
        invite.createResponse(SipServletResponse.SC_OK).send();
    }

    @Override
    protected void doBye(final SipServletRequest bye) throws ServletException, IOException {
        bye.createResponse(SipServletResponse.SC_OK).send();
    }
}

