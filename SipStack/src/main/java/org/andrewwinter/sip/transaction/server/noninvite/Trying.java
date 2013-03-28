package org.andrewwinter.sip.transaction.server.noninvite;

import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.transaction.server.ServerTransactionState;
import org.andrewwinter.sip.transaction.server.ServerTransactionStateName;

/**
 *
 * @author andrewwinter77
 */
class Trying extends ServerTransactionState {
    
    private final NonInviteServerTransaction txn;

    public Trying(final NonInviteServerTransaction txn) {
        super(ServerTransactionStateName.TRYING);
        this.txn = txn;
    }

    @Override
    public void handleResponseFromTU(final SipResponse response) {
        
        final int statusCode = response.getStatusCode();
        if (statusCode < 200) {

            // While in the "Trying" state, if the TU passes a provisional
            // response to the server transaction, the server transaction MUST
            // enter the "Proceeding" state. The response MUST be passed to the
            // transport layer for transmission.           
            
            txn.changeState(new Proceeding(txn, response));
        } else {
            txn.changeState(new Completed(txn, response));
        }
        
        txn.sendResponseToTransportLayer(response);
    }

    @Override
    public void handleRequestFromTransportLayer(SipRequest request) {
        
        // Once in the "Trying" state, any further request retransmissions are
        // discarded.
    }
}
