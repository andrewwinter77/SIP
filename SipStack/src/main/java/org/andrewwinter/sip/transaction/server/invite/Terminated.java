package org.andrewwinter.sip.transaction.server.invite;

import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.transaction.server.ServerTransactionState;
import org.andrewwinter.sip.transaction.server.ServerTransactionStateName;

/**
 *
 * @author andrewwinter77
 */
class Terminated extends ServerTransactionState {
    
    public Terminated(final InviteServerTransaction txn, final SipResponse response) {
        super(ServerTransactionStateName.TERMINATED);
        txn.sendResponseToTransportLayer(response);
        txn.destroy();
    }

    @Override
    public void handleResponseFromTU(SipResponse response) {
        // TODO: This state shouldn't even exist
    }

    @Override
    public void handleRequestFromTransportLayer(SipRequest request) {
        // TODO: This state shouldn't even exist
    }
}
