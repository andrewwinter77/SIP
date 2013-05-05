package org.andrewwinter.sip.optionshandler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;

@javax.servlet.sip.annotation.SipServlet
public class OptionsHandlerSipServlet extends SipServlet {

    @Override
    protected void doOptions(final SipServletRequest options) throws ServletException, IOException {

        // TODO: Add call state handling. I.e., return 486 if callee is busy etc.
        final SipServletResponse ok = options.createResponse(SipServletResponse.SC_OK);

        ok.addHeader("Allow", "INVITE, ACK, CANCEL, OPTIONS, BYE, REGISTER");
        ok.addHeader("Accept", "application/sdp");
        ok.addHeader("Accept-Encoding", "gzip");
        ok.addHeader("Accept-Language", "en");
        ok.addHeader("Supported", "");
        
        // TODO: Could include some SDP describing the capabilities of the server.
        
        ok.send();
    }
}
