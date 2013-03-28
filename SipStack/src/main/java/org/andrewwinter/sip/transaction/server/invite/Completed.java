package org.andrewwinter.sip.transaction.server.invite;

import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.transaction.server.ServerTransactionState;
import org.andrewwinter.sip.transaction.server.ServerTransactionStateName;

/**
 *
 * @author andrewwinter77
 */
class Completed extends ServerTransactionState {
    
    private final InviteServerTransaction txn;

    private final SipResponse finalResponse;
    
    public Completed(final InviteServerTransaction txn, final SipResponse finalResponse) {
        super(ServerTransactionStateName.COMPLETED);
        this.txn = txn;
        this.finalResponse = finalResponse;
    }

    @Override
    public void handleResponseFromTU(final SipResponse response) {
        // TODO: Anything to do here?
    }
    
    @Override
    public void handleRequestFromTransportLayer(final SipRequest request) {

        if (request.isINVITE()) {
            
            // Furthermore, while in the "Completed" state, if a request
            // retransmission is received, the server SHOULD pass the response to
            // the transport for retransmission.

            txn.sendResponseToTransportLayer(finalResponse);
            
        } else if (request.isACK()) {

            // If an ACK is received while the server transaction is in the
            // "Completed" state, the server transaction MUST transition to the
            // "Confirmed" state.
            
            txn.changeState(new Confirmed(txn));
        }
    }
}
