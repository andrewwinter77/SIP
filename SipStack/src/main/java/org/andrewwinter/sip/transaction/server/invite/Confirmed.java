package org.andrewwinter.sip.transaction.server.invite;

import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.transaction.server.ServerTransactionState;
import org.andrewwinter.sip.transaction.server.ServerTransactionStateName;

/**
 * The purpose of the "Confirmed" state is to absorb any additional ACK messages
 * that arrive, triggered from retransmissions of the final response.
 * 
 * @author andrewwinter77
 */
class Confirmed extends ServerTransactionState {
    
    public Confirmed() {
        super(ServerTransactionStateName.CONFIRMED);
    }

    @Override
    public void handleResponseFromTU(final SipResponse response) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void handleRequestFromTransportLayer(final SipRequest request) {

        // The purpose of the "Confirmed" state is to absorb any additional ACK
        // messages that arrive, triggered from retransmissions of the final
        // response.
    }
}
