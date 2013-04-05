package org.andrewwinter.sip.transaction.client.invite;

import org.andrewwinter.sip.message.InboundSipResponse;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.transaction.client.ClientTransactionState;
import org.andrewwinter.sip.transaction.client.ClientTransactionStateName;

/**
 *
 * @author andrewwinter77
 */
class Terminated extends ClientTransactionState {
    
    public Terminated(final InviteClientTransaction txn) {
        super(ClientTransactionStateName.TERMINATED);
        
        // The client transaction MUST be destroyed the instant it enters the
        // "Terminated" state.
        
        txn.destroy();
    }

    @Override
    public void cancel(final SipRequest cancel) {
    }

    @Override
    public void handleResponseFromTransportLayer(final InboundSipResponse isr) {
    }
}
