package org.andrewwinter.jsr289;

import org.andrewwinter.jsr289.api.SipServletRequestImpl;

/**
 *
 * @author andrew
 */
public interface SipServletRequestHandler {
    
    /**
     *
     */
    public static String ATTRIBUTE_NAME = SipServletRequestHandler.class.getCanonicalName();
    
    /**
     *
     * @param request
     */
    void doRequest(SipServletRequestImpl request);
}
