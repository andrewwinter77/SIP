package org.andrewwinter.jsr289;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.andrewwinter.jsr289.api.InboundSipServletRequestImpl;
import org.andrewwinter.jsr289.model.SipDeploymentUnit;
import org.andrewwinter.jsr289.model.SipServletDelegate;

/**
 *
 * @author andrew
 */
public class RequestDispatcherImpl implements RequestDispatcher {

    private final InboundSipServletRequestHandler handler;
    
    private final SipDeploymentUnit moduleInfo;
    
    private final SipServletDelegate servlet;
    
    /**
     *
     * @param handler
     * @param moduleInfo
     * @param servlet
     */
    public RequestDispatcherImpl(final InboundSipServletRequestHandler handler, final SipDeploymentUnit moduleInfo, final SipServletDelegate servlet) {
        this.handler = handler;
        this.moduleInfo = moduleInfo;
        this.servlet = servlet;
    }

    @Override
    public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        if (request != null) {
            handler.doRequest((InboundSipServletRequestImpl) request, moduleInfo, servlet);
        } else if (response != null) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Override
    public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        // The include method has no meaning for SIP servlets.
    }
}
