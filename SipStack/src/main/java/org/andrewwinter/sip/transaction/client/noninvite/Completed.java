package org.andrewwinter.sip.transaction.client.noninvite;

import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.transaction.client.ClientTransactionState;
import org.andrewwinter.sip.transaction.client.ClientTransactionStateName;

/**
 *
 * @author andrewwinter77
 */
class Completed extends ClientTransactionState {
    
    private final NonInviteClientTransaction txn;

    public Completed(final NonInviteClientTransaction txn) {
        super(ClientTransactionStateName.COMPLETED);
        this.txn = txn;
    }

    @Override
    public void cancel(final SipRequest cancel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void handleResponseFromTransportLayer(final SipResponse response) {
        // The "Completed" state exists to buffer any additional response
        // retransmissions that may be received (which is why the client
        // transaction remains there only for unreliable transports).
    }
}
