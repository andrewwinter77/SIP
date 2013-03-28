package org.andrewwinter.sip.transaction.client.invite;

import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.transaction.client.ClientTransactionState;
import org.andrewwinter.sip.transaction.client.ClientTransactionStateName;

/**
 *
 * @author andrewwinter77
 */
class Completed extends ClientTransactionState {
    
    private final InviteClientTransaction txn;

    public Completed(final InviteClientTransaction txn) {
        super(ClientTransactionStateName.COMPLETED);
        this.txn = txn;
    }

    @Override
    public void cancel(final SipRequest cancel) {
    }

    @Override
    public void handleResponseFromTransportLayer(final SipResponse response) {
        
        if (response.getStatusCode() >= 200) {
        
            // Any retransmissions of the final response that are received while
            // in the "Completed" state MUST cause the ACK to be re-passed to
            // the transport layer for retransmission, but the newly received
            // response MUST NOT be passed up to the TU.
            
            txn.ackNon2XX(response, txn.getRequest());
        }
    }
}
