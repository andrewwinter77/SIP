package org.andrewwinter.sip.transaction.server;

import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;

/**
 *
 * @author andrew
 */
public abstract class ServerTransactionState {
    
    private final ServerTransactionStateName state;
    
    /**
     *
     * @param state
     */
    public ServerTransactionState(final ServerTransactionStateName state) {
        this.state = state;
    }
    
    /**
     *
     * @param response
     */
    public abstract void handleResponseFromTU(SipResponse response);
    
    /**
     *
     * @param request
     */
    public abstract void handleRequestFromTransportLayer(SipRequest request);
    
    /**
     *
     */
    public void cancel() {
        // If the transaction for the original request still exists, the
        // behavior of the UAS on receiving a CANCEL request depends on whether
        // it has already sent a final response for the original request. If it
        // has, the CANCEL request has no effect on the processing of the
        // original request, no effect on any session state, and no effect on
        // the responses generated for the original request.
    }
    
    /**
     *
     * @return
     */
    public final ServerTransactionStateName getStateName() {
        return state;
    }
}
