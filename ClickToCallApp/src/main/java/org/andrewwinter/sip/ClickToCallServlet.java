package org.andrewwinter.sip;


import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSessionsUtil;
import javax.servlet.sip.TimerService;


@javax.servlet.sip.annotation.SipServlet
public class ClickToCallServlet extends SipServlet {
    
    @Resource
    private SipFactory sipFactory;
    
    @Resource
    private SipSessionsUtil sipSessionsUtil;

    @Resource
    private TimerService timerService;

    @Override
    protected void doSuccessResponse(final SipServletResponse response) throws ServletException, IOException {
        if (response.getMethod().equals("INVITE")) {
            final SipServletRequest ack = response.createAck();
            ack.setContent(response.getContent(), response.getContentType());
            ack.send();
        } else {
            super.doSuccessResponse(response);
        }
    }

    @Override
    protected void doErrorResponse(SipServletResponse ssr) throws ServletException, IOException {
        super.doErrorResponse(ssr); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doBye(SipServletRequest ssr) throws ServletException, IOException {
        super.doBye(ssr); //To change body of generated methods, choose Tools | Templates.
    }
}

