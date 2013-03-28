package org.andrewwinter.sip.transaction.client.noninvite;

import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.transaction.client.ClientTransactionState;
import org.andrewwinter.sip.transaction.client.ClientTransactionStateName;

/**
 *
 * @author andrewwinter77
 */
class Terminated extends ClientTransactionState {
    
    public Terminated(final NonInviteClientTransaction txn) {
        super(ClientTransactionStateName.TERMINATED);
        
        // Once the transaction is in the terminated state, it MUST be destroyed
        // immediately.
        
        txn.destroy();
    }

    @Override
    public void handleResponseFromTransportLayer(final SipResponse response) {
    }
}
