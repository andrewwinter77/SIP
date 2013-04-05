package org.andrewwinter.sip.transaction.client;

import org.andrewwinter.sip.message.InboundSipResponse;
import org.andrewwinter.sip.parser.SipRequest;

/**
 *
 * @author andrew
 */
public abstract class ClientTransactionState {
    
    
    private final ClientTransactionStateName state;
    
    public ClientTransactionState(final ClientTransactionStateName state) {
        this.state = state;
    }
    
    public ClientTransactionStateName getStateName() {
        return state;
    }
    
    /**
     *
     * @param response
     */
    public abstract void handleResponseFromTransportLayer(InboundSipResponse response);
    
    /**
     *
     */
    public void cancel(final SipRequest cancel) {
    }
}
