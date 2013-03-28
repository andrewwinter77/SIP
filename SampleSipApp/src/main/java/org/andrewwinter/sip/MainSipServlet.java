package org.andrewwinter.sip;


import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSessionsUtil;
import javax.servlet.sip.TimerService;


@javax.servlet.sip.annotation.SipServlet
public class MainSipServlet extends SipServlet {
    
    @Resource
    private SipFactory sipFactory;
    
    @Resource
    private SipSessionsUtil sipSessionsUtil;

    @Resource
    private TimerService timerService;

    @Override
    protected void doSuccessResponse(SipServletResponse ssr) throws ServletException, IOException {
        super.doSuccessResponse(ssr); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doErrorResponse(SipServletResponse ssr) throws ServletException, IOException {
        super.doErrorResponse(ssr); //To change body of generated methods, choose Tools | Templates.
    }
}

