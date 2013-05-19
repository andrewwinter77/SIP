package org.andrewwinter.sip.transaction.server.invite;

import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.dialog.DialogState;
import org.andrewwinter.sip.message.InboundSipRequest;
import org.andrewwinter.sip.message.ResponseType;
import org.andrewwinter.sip.parser.SipMessageHelper;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.parser.Timestamp;
import org.andrewwinter.sip.transaction.server.ServerTransactionState;
import org.andrewwinter.sip.transaction.server.ServerTransactionStateName;

/**
 *
 * @author andrewwinter77
 */
class Proceeding extends ServerTransactionState {
    
    private final InviteServerTransaction txn;

    private SipResponse provisionalResponse;

    public Proceeding(final InviteServerTransaction txn) {
        super(ServerTransactionStateName.PROCEEDING);
        this.txn = txn;
        
        final InboundSipRequest isr = txn.getInboundSipRequest();
        final SipRequest request = isr.getRequest();
        
        // The server transaction MUST generate a 100 (Trying) response unless
        // it knows that the TU will generate a provisional or final response
        // within 200 ms, in which case it MAY generate a 100 (Trying) response.
        final SipResponse trying = isr.createResponse(ResponseType.TRYING);
        
        // When a 100 (Trying) response is generated, any Timestamp header field
        // present in the request MUST be copied into this 100 (Trying)
        // response.
        final Timestamp timestamp = request.getTimestamp();
        if (timestamp != null) {
            SipMessageHelper.setTimestamp(timestamp, trying);
        }
        
        handleResponseFromTU(trying);
    }

    @Override
    public final void handleResponseFromTU(final SipResponse response) {
        
        final int statusCode = response.getStatusCode();

        if (statusCode < 300 && SipMessageHelper.getTo(response).getTag() != null) {
            
            // Dialogs are created through the generation of non-failure responses
            // to requests with specific methods. Within this specification, only
            // 2xx and 101-199 responses with a To tag, where the request was
            // INVITE, will establish a dialog.        
            
            // A dialog established by a non-final response to a request is in
            // the "early" state and it is called an early dialog.
            
            // A dialog can also be in the "early" state, which occurs when it
            // is created with a provisional response, and then transition to
            // the "confirmed" state when a 2xx final response arrives.
            
            final Dialog dialog = txn.getDialog();
            if (dialog == null) {
                if (statusCode < 200) {
                    txn.createDialog(response, DialogState.EARLY);
                } else {
                    txn.createDialog(response, DialogState.CONFIRMED);
                }
            } else {
                if (statusCode >= 200 && dialog.getState() == DialogState.EARLY) {
                    txn.createDialog(response, DialogState.CONFIRMED);
                }
            }
        }
        
        if (statusCode < 200) {

            provisionalResponse = response;
            
            // The TU passes any number of provisional responses to the server
            // transaction. So long as the server transaction is in the
            // "Proceeding" state, each of these MUST be passed to the transport
            // layer for transmission. They are not sent reliably by the
            // transaction layer (they are not retransmitted by it) and do not
            // cause a change in the state of the server transaction.
            
            txn.sendResponseToTransportLayer(response);

        } else if (statusCode >= 200 && statusCode < 300) {

            // If, while in the "Proceeding" state, the TU passes a 2xx response
            // to the server transaction, the server transaction MUST pass this
            // response to the transport layer for transmission. It is not
            // retransmitted by the server transaction; retransmissions of 2xx
            // responses are handled by the TU. The server transaction MUST then
            // transition to the "Terminated" state.
            
            txn.sendResponseToTransportLayer(response);
            
            txn.changeState(new Terminated(txn));
            
        } else { // 300-699
            
            // While in the "Proceeding" state, if the TU passes a response with
            // status code from 300 to 699 to the server transaction, the
            // response MUST be passed to the transport layer for transmission,
            // and the state machine MUST enter the "Completed" state.
            
            // TODO: For
            // unreliable transports, timer G is set to fire in T1 seconds, and
            // is not set to fire for reliable transports.

            txn.sendResponseToTransportLayer(response);

            txn.changeState(new Completed(txn, response));
        }

        
    }

    @Override
    public void handleRequestFromTransportLayer(SipRequest request) {
        if (provisionalResponse != null) {
            
            // If a request retransmission is received while in the "Proceeding"
            // state, the most recent provisional response that was received
            // from the TU MUST be passed to the transport layer for
            // retransmission.
            
            txn.sendResponseToTransportLayer(provisionalResponse);
        }
    }
    
    @Override
    public void cancel() {
        final InboundSipRequest isr = txn.getInboundSipRequest();
        
        // If the original request was an INVITE, the UAS SHOULD immediately
        // respond to the INVITE with a 487 (Request Terminated).
        
        final SipResponse response = isr.createResponse(ResponseType.REQUEST_TERMINATED);
        
        // While in the "Proceeding" state, if the TU passes a response with
        // status code from 300 to 699 to the server transaction, the response
        // MUST be passed to the transport layer for transmission, and the state
        // machine MUST enter the "Completed" state.

        txn.changeState(new Completed(txn, response));

        txn.sendResponseToTransportLayer(response);
    }
}
