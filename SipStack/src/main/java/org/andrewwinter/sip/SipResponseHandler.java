package org.andrewwinter.sip;

import org.andrewwinter.sip.message.InboundSipResponse;

/**
 *
 * @author andrew
 */
public interface SipResponseHandler {
    
    /**
     * 
     * @param response 
     */
    void doResponse(InboundSipResponse response);
}
