package org.andrewwinter.jsr289;

import org.andrewwinter.jsr289.api.SipServletRequestImpl;
import org.andrewwinter.jsr289.model.SipDeploymentUnit;
import org.andrewwinter.jsr289.model.SipServletDelegate;

/**
 *
 * @author andrew
 */
public interface InboundSipServletRequestHandler {
    
    /**
     * 
     * @param request
     * @param moduleInfo
     * @param sipServlet 
     */
    void doRequest(SipServletRequestImpl request, SipDeploymentUnit moduleInfo, SipServletDelegate sipServlet);
}
