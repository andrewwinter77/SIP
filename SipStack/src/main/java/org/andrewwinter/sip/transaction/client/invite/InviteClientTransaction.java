package org.andrewwinter.sip.transaction.client.invite;

import java.util.List;
import org.andrewwinter.sip.SipResponseHandler;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.dialog.DialogState;
import org.andrewwinter.sip.element.Destination;
import org.andrewwinter.sip.parser.Address;
import org.andrewwinter.sip.parser.CSeq;
import org.andrewwinter.sip.parser.SipMessageHelper;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.transaction.client.ClientTransaction;
import org.andrewwinter.sip.transport.RequestSender;

/**
 *
 * @author andrewwinter77
 */
public class InviteClientTransaction extends ClientTransaction {
    
    private Dialog dialog;
    
    private InviteClientTransaction(
            final SipResponseHandler txnUser,
            final SipRequest request,
            final RequestSender sender) {
        super(txnUser, request, sender);
    }
    
    void ackNon2XX(final SipResponse response, final SipRequest invite) {

        setTerminated();
        
        // The ACK request constructed by the client transaction MUST contain
        // values for the Call-ID, From, and Request-URI that are equal to the
        // values of those header fields in the request passed to the transport
        // by the client transaction (call this the "original request").
        
        final SipRequest ack = new SipRequest("ACK", invite.getRequestUri());
        ack.setCallId(invite.getCallId());
        SipMessageHelper.setFrom(SipMessageHelper.getFrom(invite), ack);

        
        // The To header field in the ACK MUST equal the To header field in the
        // response being acknowledged, and therefore will usually differ from
        // the To header field in the original request by the addition of the
        // tag parameter.
        
        SipMessageHelper.setTo(SipMessageHelper.getTo(response), ack);

        // The ACK MUST contain a single Via header field, and this MUST be
        // equal to the top Via header field of the original request.
        
        SipMessageHelper.setVia(invite.getTopmostVia(), ack);
        
        // The CSeq header field in the ACK MUST contain the same value for the
        // sequence number as was present in the original request, but the
        // method parameter MUST be equal to "ACK".
        
        final CSeq cseq = new CSeq("ACK", invite.getCSeq().getSequence());
        ack.setCSeq(cseq);        
        
        // If the INVITE request whose response is being acknowledged had Route
        // header fields, those header fields MUST appear in the ACK. This is to
        // ensure that the ACK can be routed properly through any downstream
        // stateless proxies.
        
        // TODO: Copy over Route headers
        
        // The ACK MUST be sent to the same address, port, and transport to
        // which the original request was sent.
        
        getRequestSender().send(ack);
    }
    
    Dialog getDialog() {
        return dialog;
    }
    
    /**
     * 
     * @param response
     * @return 
     */
    public Dialog getOrCreateDialog(final SipResponse response) {
        
        final int statusCode = response.getStatusCode();
        
        // Dialogs are created through the generation of non-failure responses
        // to requests with specific methods. Within this specification, only
        // 2xx and 101-199 responses with a To tag, where the request was
        // INVITE, will establish a dialog.        
        
        // A dialog established by a non-final response to a request is in
        // the "early" state and it is called an early dialog.
            
        // A dialog can also be in the "early" state, which occurs when it
        // is created with a provisional response, and then transition to
        // the "confirmed" state when a 2xx final response arrives.

        final Address to = SipMessageHelper.getTo(response);
        final List<Address> contact = SipMessageHelper.getContact(response);
        
        // Note: there's a discussion here:
        // http://www.mail-archive.com/sip@ietf.org/msg03828.html
        // on whether 1xx without a Contact
        // header should form a dialog or not. We'll add a check for Contact
        // header being present because there are situations where this code
        // will blow up otherwise.
        
        if (statusCode < 300 && to.getTag() != null && contact != null && !contact.isEmpty()) {

            if (statusCode < 200) {
                if (dialog == null) {

                    // If a provisional response has a tag in the To field, and if the
                    // dialog ID of the response does not match an existing dialog, one is
                    // constructed using the procedures defined in Section 12.1.2.
                    
                    dialog = Dialog.createForUAC(getRequest(), response, DialogState.EARLY);
                }
            } else if (statusCode >= 200 && statusCode < 300) {
                if (dialog == null) {
                
                    // Otherwise, a new dialog in the "confirmed" state MUST be
                    // constructed using the procedures of Section 12.1.2.
                
                    dialog = Dialog.createForUAC(getRequest(), response, DialogState.CONFIRMED);
                
                } else {
                    // If the dialog identifier in the 2xx response matches the
                    // dialog identifier of an existing dialog, the dialog MUST be
                    // transitioned to the "confirmed" state
                
                    dialog.setState(DialogState.CONFIRMED);
                }
            }
        }
        
        return dialog;
    }

    
    /**
     * 
     * @param sipListener
     * @param request
     * @param dest 
     * @return  
     */
    public static InviteClientTransaction create(
            final SipResponseHandler sipListener,
            final SipRequest request,
            final Destination dest) {

        // There are two types of client transaction state machines, depending
        // on the method of the request passed by the TU. One handles client
        // transactions for INVITE requests. This type of machine is referred to
        // as an INVITE client transaction.
        
        if (!request.isINVITE()) {
            throw new IllegalArgumentException("InviteClientTransaction cannot be used to send non-INVITE requests.");
        }
        
        final RequestSender sender = new RequestSender(dest);
        final InviteClientTransaction txn = new InviteClientTransaction(sipListener, request, sender);
        
        // The initial state, "calling", MUST be entered when the TU initiates a
        // new client transaction with an INVITE request.        
        txn.initialize(new Calling(txn));
        
        return txn;
    }
}
