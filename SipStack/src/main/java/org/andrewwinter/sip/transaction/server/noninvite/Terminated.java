package org.andrewwinter.sip.transaction.server.noninvite;

import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.transaction.server.ServerTransactionState;
import org.andrewwinter.sip.transaction.server.ServerTransactionStateName;

/**
 *
 * @author andrewwinter77
 */
class Terminated extends ServerTransactionState {
    
    public Terminated(final NonInviteServerTransaction txn) {
        super(ServerTransactionStateName.TERMINATED);
        
        // The server transaction MUST be destroyed the instant it enters the
        // "Terminated" state.
        
        txn.destroy();
    }

    @Override
    public void handleResponseFromTU(SipResponse response) {
        // Do nothing. This state should not exist.
    }

    @Override
    public void handleRequestFromTransportLayer(SipRequest request) {
        // Do nothing. This state should not exist.
    }
}
