package org.andrewwinter.sip.transaction.client.noninvite;

import org.andrewwinter.sip.message.InboundSipResponse;
import org.andrewwinter.sip.transaction.client.ClientTransactionState;
import org.andrewwinter.sip.transaction.client.ClientTransactionStateName;

/**
 *
 * @author andrewwinter77
 */
class Proceeding extends ClientTransactionState {
    
    private final NonInviteClientTransaction txn;

    public Proceeding(final NonInviteClientTransaction txn) {
        super(ClientTransactionStateName.PROCEEDING);
        this.txn = txn;
    }

    @Override
    public void handleResponseFromTransportLayer(final InboundSipResponse isr) {
        if (isr.getResponse().getStatusCode() >= 200) {
            
            // If a final response (status codes 200-699) is received while in
            // the ???Proceeding??? state, the response MUST be passed to the TU,
            // and the client transaction MUST transition to the ???Completed???
            // state.
            
            txn.changeState(new Completed());
            txn.sendResponseToTU(isr, txn.getDialog());
        }
    }
}
