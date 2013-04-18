package org.andrewwinter.jsr289;

import javax.servlet.sip.SipServletRequest;

/**
 *
 * @author andrew
 */
public interface SipServletRequestHandler {
    
    public static String ATTRIBUTE_NAME = SipServletRequestHandler.class.getCanonicalName();
    
    void doRequest(SipServletRequest request);
}
