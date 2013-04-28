package org.andrewwinter.sip.proxy;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;

@javax.servlet.sip.annotation.SipServlet
public class ProxySipServlet extends SipServlet {
    
    @Override
    protected void doInvite(final SipServletRequest invite) throws ServletException, IOException {
        
        // The isInitial method on the SipServletRequest interface returns true
        // if the request is initial as defined above, and can be used as an
        // indication of whether the application should explicitly proxy an
        // incoming request or not.
        
        if (invite.isInitial()) {
            invite.getProxy().proxyTo(invite.getRequestURI());
        } else {
            super.doInvite(invite);
        }
    }
}

