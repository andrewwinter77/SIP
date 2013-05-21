package org.andrewwinter.sip.transaction.server.invite;

import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.timer.TimerService;
import org.andrewwinter.sip.transaction.server.ServerTransactionState;
import org.andrewwinter.sip.transaction.server.ServerTransactionStateName;

/**
 * The purpose of the "Confirmed" state is to absorb any additional ACK messages
 * that arrive, triggered from retransmissions of the final response.
 * 
 * @author andrewwinter77
 */
class Confirmed extends ServerTransactionState {
    
    private final InviteServerTransaction txn;

    public Confirmed(final InviteServerTransaction txn) {
        super(ServerTransactionStateName.CONFIRMED);
        this.txn = txn;
        
        // When this state is entered, timer I is set to fire in T4 seconds for
        // unreliable transports, and zero seconds for reliable transports. Once
        // timer I fires, the server MUST transition to the "Terminated" state.
        
        if (true) { // TODO: unreliable transport mechanism
            TimerService.getInstance().startTimerI(txn);
        } else {
            txn.changeState(new Terminated(txn));
        }
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
    
    @Override
    public void timerIFired() {
        txn.changeState(new Terminated(txn));
    }
}
