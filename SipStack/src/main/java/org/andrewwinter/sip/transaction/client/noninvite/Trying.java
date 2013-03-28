package org.andrewwinter.sip.transaction.client.noninvite;

import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.transaction.client.ClientTransactionState;
import org.andrewwinter.sip.transaction.client.ClientTransactionStateName;

/**
 *
 * @author andrewwinter77
 */
class Trying extends ClientTransactionState {
    
    private final NonInviteClientTransaction txn;

    public Trying(final NonInviteClientTransaction txn) {
        super(ClientTransactionStateName.TRYING);
        this.txn = txn;
    }

    @Override
    public void cancel(final SipRequest cancel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void handleResponseFromTransportLayer(final SipResponse response) {
        
        // If a provisional response is received while in the ???Trying??? state,
        // the response MUST be passed to the TU, and then the client
        // transaction SHOULD move to the ???Proceeding??? state.
        
        // If a final response (status codes 200-699) is received while in the
        // ???Trying??? state, the response MUST be passed to the TU, and the client
        // transaction MUST transition to the ???Completed??? state.
        
        if (response.getStatusCode() < 200) {
            txn.changeState(new Proceeding(txn));
        } else {
            txn.changeState(new Completed(txn));
        }
        
        txn.sendResponseToTU(response, txn.getDialog());
    }
}
