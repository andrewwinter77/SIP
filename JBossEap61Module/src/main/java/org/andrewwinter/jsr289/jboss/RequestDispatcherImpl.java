package org.andrewwinter.jsr289.jboss;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.sip.SipServlet;
import org.andrewwinter.jsr289.api.SipServletRequestImpl;
import org.andrewwinter.jsr289.jboss.metadata.SipModuleInfo;

/**
 *
 * @author andrew
 */
public class RequestDispatcherImpl implements RequestDispatcher {

    private final SipServletService service;
    
    private final SipModuleInfo moduleInfo;
    
    private final SipServlet servlet;
    
    public RequestDispatcherImpl(final SipServletService service, final SipModuleInfo moduleInfo, final SipServlet servlet) {
        this.service = service;
        this.moduleInfo = moduleInfo;
        this.servlet = servlet;
    }

    @Override
    public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        if (request != null) {
            service.doRequest((SipServletRequestImpl) request, moduleInfo, servlet);
        } else if (response != null) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Override
    public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        // The include method has no meaning for SIP servlets.
    }
}
