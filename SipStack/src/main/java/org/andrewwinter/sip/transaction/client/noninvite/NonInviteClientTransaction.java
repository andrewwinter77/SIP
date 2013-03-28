package org.andrewwinter.sip.transaction.client.noninvite;

import org.andrewwinter.sip.SipResponseHandler;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.element.Destination;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.transaction.client.ClientTransaction;
import org.andrewwinter.sip.transport.RequestSender;

/**
 *
 * @author andrewwinter77
 */
public class NonInviteClientTransaction extends ClientTransaction {
    
    private Dialog dialog;
    
    private NonInviteClientTransaction(
            final SipResponseHandler txnUser,
            final SipRequest request,
            final RequestSender sender,
            final Dialog dialog) {
        super(txnUser, request, sender);
        this.dialog = dialog;
    }
    
    Dialog getDialog() {
        return dialog;
    }
    
    /**
     * 
     * @param sipListener
     * @param request
     * @param dialog 
     * @param dest 
     * @return  
     */
    public static NonInviteClientTransaction create(
            final SipResponseHandler sipListener,
            final SipRequest request,
            final Dialog dialog,
            final Destination dest) {

        // Another type handles client transactions for all requests except
        // INVITE and ACK. This is referred to as a non-INVITE client
        // transaction.
        
        if (request.isINVITE() || request.isACK()) {
            throw new IllegalArgumentException("NonInviteClientTransaction cannot be used to send INVITEs or ACKs.");
        }
        
        final RequestSender sender = new RequestSender(dest);
        final NonInviteClientTransaction txn = new NonInviteClientTransaction(sipListener, request, sender, dialog);
        txn.initialize(new Trying(txn));
        
        return txn;
    }
}
