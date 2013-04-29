package org.andrewwinter.sip.transaction.server.noninvite;

import org.andrewwinter.sip.SipRequestHandler;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.message.InboundSipRequest;
import org.andrewwinter.sip.transaction.server.ServerTransaction;

/**
 *
 * @author andrewwinter77
 */
public class NonInviteServerTransaction extends ServerTransaction {
    
    private final ServerTransaction associatedTxn;
    
    /**
     * 
     * @param dialog
     * @param isr
     * @param associatedTxn Null if there is no associated transaction.
     */
    private NonInviteServerTransaction(final Dialog dialog, final InboundSipRequest isr, final ServerTransaction associatedTxn) {
        super(dialog, isr);
        this.associatedTxn = associatedTxn;
    }
    
    /**
     *
     * @return
     */
    public ServerTransaction getAssociatedTxn() {
        return associatedTxn;
    }
    
    /**
     * 
     * @param dialog Null if there is no dialog.
     * @param isr
     * @param associatedTxn
     * @param sipListener  
     */
    public static void create(
            final Dialog dialog,
            final InboundSipRequest isr,
            final ServerTransaction associatedTxn,
            final SipRequestHandler sipListener) {
        final NonInviteServerTransaction txn = new NonInviteServerTransaction(dialog, isr, associatedTxn);
        
        // The state machine is initialized in the 'Trying' state and is passed
        // a request other than INVITE or ACK when initialized.
        
        if (isr.getRequest().isINVITE() || isr.getRequest().isACK()) {
            throw new IllegalArgumentException(isr.getRequest().getMethod() + " not allowed in non-INVITE server transaction.");
        }
        
        txn.initialize(new Trying(txn), associatedTxn, sipListener);
    }
}
