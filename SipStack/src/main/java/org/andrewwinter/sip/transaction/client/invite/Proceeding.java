package org.andrewwinter.sip.transaction.client.invite;

import org.andrewwinter.sip.SipResponseHandler;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.message.InboundSipResponse;
import org.andrewwinter.sip.message.SipMessageFactory;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.transaction.client.ClientTransactionState;
import org.andrewwinter.sip.transaction.client.ClientTransactionStateName;
import org.andrewwinter.sip.transaction.client.noninvite.NonInviteClientTransaction;

/**
 *
 * @author andrewwinter77
 */
class Proceeding extends ClientTransactionState {
    
    private final InviteClientTransaction txn;

    public Proceeding(final InviteClientTransaction txn, final boolean cancelRequested, final SipRequest cancel) {
        super(ClientTransactionStateName.PROCEEDING);
        this.txn = txn;
        
        if (cancelRequested) {
            cancel(cancel);
        }
    }

    @Override
    public final void cancel(SipRequest cancel) {
        if (cancel == null) {
            cancel = SipMessageFactory.createCancel(txn.getRequest());
        }
        
        final SipResponseHandler listener = new SipResponseHandler() {
            @Override
            public void doResponse(InboundSipResponse response) {
            }
        };
        
        // When the client decides to send the CANCEL, it creates a client
        // transaction for the CANCEL and passes it the CANCEL request along
        // with the destination address, port, and transport. The destination
        // address, port, and transport for the CANCEL MUST be identical to
        // those used to send the original request.

        NonInviteClientTransaction.create(
                listener,
                cancel,
                txn.getDialog(),
                txn.getRequestSender().getDestination());
    }

    @Override
    public void handleResponseFromTransportLayer(final SipResponse response) {

        final Dialog dialog = txn.getOrCreateDialog(response);
        
        if (response.getStatusCode() < 200) {
            
            // Any further provisional responses MUST be passed up to the TU
            // while in the "Proceeding" state.

        } else if (response.getStatusCode() >= 300 && response.getStatusCode() < 700) {

            // When in either the "Calling" or "Proceeding" states, reception of
            // a response with status code from 300-699 MUST cause the client
            // transaction to transition to "Completed". The client transaction
            // MUST pass the received response up to the TU, and the client
            // transaction MUST generate an ACK request, even if the transport
            // is reliable (guidelines for constructing the ACK from the
            // response are given in Section 17.1.1) and then pass the ACK to
            // the transport layer for transmission.            
            
            txn.changeState(new Completed(txn));
            
            // After having received the non-2xx final response the UAC core
            // considers the INVITE transaction completed. The INVITE client
            // transaction handles the generation of ACKs for the response (see
            // Section 17).
            
            txn.ackNon2XX(response, txn.getRequest());
            
        } else if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
            
            // When in either the "Calling" or "Proceeding" states, reception of
            // a 2xx response MUST cause the client transaction to enter the
            // "Terminated" state, and the response MUST be passed up to the TU.
            
            txn.changeState(new Terminated(txn));
        }
        
        txn.sendResponseToTU(response, dialog);
    }
}
