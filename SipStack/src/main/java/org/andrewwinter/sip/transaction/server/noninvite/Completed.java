package org.andrewwinter.sip.transaction.server.noninvite;

import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.transaction.server.ServerTransactionState;
import org.andrewwinter.sip.transaction.server.ServerTransactionStateName;

/**
 *
 * @author andrewwinter77
 */
class Completed extends ServerTransactionState {
    
    private final NonInviteServerTransaction txn;

    private final SipResponse finalResponse;
    
    public Completed(final NonInviteServerTransaction txn, final SipResponse finalResponse) {
        super(ServerTransactionStateName.COMPLETED);
        this.txn = txn;
        this.finalResponse = finalResponse;
    }

    @Override
    public void handleResponseFromTU(final SipResponse response) {
        // Any other final responses passed by the TU to the server transaction
        // MUST be discarded while in the "Completed" state.
    }
    
    @Override
    public void handleRequestFromTransportLayer(SipRequest request) {
        // While in the "Completed" state, the server transaction MUST pass the
        // final response to the transport layer for retransmission whenever a
        // retransmission of the request is received.
        
        txn.sendResponseToTransportLayer(finalResponse);
    }
}
