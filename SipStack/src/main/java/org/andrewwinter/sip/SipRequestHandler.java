package org.andrewwinter.sip;

import org.andrewwinter.sip.message.InboundSipRequest;

/**
 *
 * @author andrew
 */
public interface SipRequestHandler {
    
    /**
     * 
     * @param request 
     */
    void doRequest(InboundSipRequest request);
}
