package org.andrewwinter.sip.location;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;

@javax.servlet.sip.annotation.SipServlet
public class LocationSipServlet extends SipServlet {
    
    @Override
    protected void doInvite(final SipServletRequest invite) throws ServletException, IOException {
        
        if (true) { // if do not disturb enabled
            final SipServletResponse response = invite.createResponse(SipServletResponse.SC_BUSY_EVERYWHERE);
            response.send();
        } else {
            invite.getProxy().proxyTo(invite.getRequestURI());
        }
    }
}
