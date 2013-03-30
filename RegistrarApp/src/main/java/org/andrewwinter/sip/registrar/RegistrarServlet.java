package org.andrewwinter.sip.registrar;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;

@javax.servlet.sip.annotation.SipServlet
public class RegistrarServlet extends SipServlet {

    @Override
    protected void doRegister(SipServletRequest ssr) throws ServletException, IOException {
        super.doRegister(ssr);
    }
}

