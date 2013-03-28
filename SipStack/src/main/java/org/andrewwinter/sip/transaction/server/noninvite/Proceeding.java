package org.andrewwinter.sip.transaction.server.noninvite;

import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.transaction.server.ServerTransactionState;
import org.andrewwinter.sip.transaction.server.ServerTransactionStateName;

/**
 *
 * @author andrewwinter77
 */
class Proceeding extends ServerTransactionState {
    
    private final NonInviteServerTransaction txn;

    private SipResponse provisionalResponse;
    
    /**
     * 
     * @param txn
     * @param provisionalResponse The provisional response that caused us to enter this state.
     */
    public Proceeding(final NonInviteServerTransaction txn, final SipResponse provisionalResponse) {
        super(ServerTransactionStateName.PROCEEDING);
        this.txn = txn;
        this.provisionalResponse = provisionalResponse;
    }

    @Override
    public void handleResponseFromTU(final SipResponse response) {
        if (response.getStatusCode() < 200) {
            
            this.provisionalResponse = response;
            
            // Any further provisional responses that are received from the TU
            // while in the "Proceeding" state MUST be passed to the transport
            // layer for transmission.
        } else {

            // If the TU passes a final response (status codes 200-699) to the
            // server while in the "Proceeding" state, the transaction MUST
            // enter the "Completed" state, and the response MUST be passed to
            // the transport layer for transmission.
            
            txn.changeState(new Completed(txn, response));
        }
        
        txn.sendResponseToTransportLayer(response);
    }

    @Override
    public void handleRequestFromTransportLayer(SipRequest request) {
        // If a retransmission of the request is received while in the
        // "Proceeding" state, the most recently sent provisional response MUST
        // be passed to the transport layer for retransmission.
        
        txn.sendResponseToTransportLayer(provisionalResponse);
    }
}
