package org.andrewwinter.sip.transaction.client.invite;

import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.message.InboundSipResponse;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.timer.TimerName;
import org.andrewwinter.sip.timer.TimerService;
import org.andrewwinter.sip.transaction.client.ClientTransactionState;
import org.andrewwinter.sip.transaction.client.ClientTransactionStateName;

/**
 *
 * @author andrewwinter77
 */
class Calling extends ClientTransactionState {
    
    private final InviteClientTransaction txn;

    /**
     * True if the TU has requested that the transaction is canceled.
     */
    private boolean cancelRequested;
    
    private SipRequest cancel;
    
    public Calling(final InviteClientTransaction txn) {
        super(ClientTransactionStateName.CALLING);
        this.txn = txn;
        cancelRequested = false;
        if (true) { // TODO: If unreliable transport
            
            // If an unreliable transport is being used, the client transaction
            // MUST start timer A with a value of T1.
            
            TimerService.getInstance().startTimerA(txn, 0);
        } else {
            // If a reliable transport is being used, the client transaction
            // SHOULD NOT start timer A (Timer A controls request
            // retransmissions).
        }
        
        // For any transport, the client transaction MUST start timer B with a
        // value of 64*T1 seconds (Timer B controls transaction timeouts).
        
        TimerService.getInstance().startTimerB(txn);
    }

    @Override
    public void cancel(final SipRequest cancel) {
        
        // If no provisional response has been received, the CANCEL request MUST
        // NOT be sent; rather, the client MUST wait for the arrival of a
        // provisional response before sending the request.        

        cancelRequested = true;
        this.cancel = cancel;
    }

    @Override
    public void handleResponseFromTransportLayer(final InboundSipResponse isr) {
        
        final Dialog dialog = txn.getOrCreateDialog(isr.getResponse());
        
        final int status = isr.getResponse().getStatusCode();
        if (status < 200) {
            
            // If the client transaction receives a provisional response while
            // in the "Calling" state, it transitions to the "Proceeding" state.
            // Furthermore, the provisional response MUST be passed to the TU.
            
           cancelTimersAB();
           txn.changeState(new Proceeding(txn, cancelRequested, cancel));
            
        } else if (status >= 300 && status < 700) {

            // When in either the "Calling" or "Proceeding" states, reception of
            // a response with status code from 300-699 MUST cause the client
            // transaction to transition to "Completed". The client transaction
            // MUST pass the received response up to the TU, and the client
            // transaction MUST generate an ACK request, even if the transport
            // is reliable (guidelines for constructing the ACK from the
            // response are given in Section 17.1.1) and then pass the ACK to
            // the transport layer for transmission.            

            cancelTimersAB();
            txn.changeState(new Completed(txn));

            // After having received the non-2xx final response the UAC core
            // considers the INVITE transaction completed. The INVITE client
            // transaction handles the generation of ACKs for the response (see
            // Section 17).
            
            txn.ackNon2XX(isr.getResponse(), txn.getRequest());
            
        } else if (status >= 200 && status < 300) {
            
            // When in either the "Calling" or "Proceeding" states, reception of
            // a 2xx response MUST cause the client transaction to enter the
            // "Terminated" state, and the response MUST be passed up to the TU.
            
            cancelTimersAB();
            txn.changeState(new Terminated(txn));
        }
        
        txn.sendResponseToTU(isr, dialog);
    }

    @Override
    public void timerAFired() {
        txn.sendRequest();
    }
    
    @Override
    public void timerBFired() {
        
        // If the client transaction is still in the "Calling" state when timer
        // B fires, the client transaction SHOULD inform the TU that a timeout
        // has occurred. The client transaction MUST NOT generate an ACK.
        
        txn.inviteClientTxnTimeout();
        
        cancelTimersAB();
        txn.changeState(new Terminated(txn));
    }
    
    /**
     * 
     */
    private void cancelTimersAB() {
        System.out.println("Cancelling timers A and B");
        txn.cancelTimer(TimerName.TimerA);
        txn.cancelTimer(TimerName.TimerB);
    }
}
