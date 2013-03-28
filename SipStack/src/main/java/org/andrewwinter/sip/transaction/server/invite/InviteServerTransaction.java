package org.andrewwinter.sip.transaction.server.invite;

import org.andrewwinter.sip.SipRequestHandler;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.message.InboundSipRequest;
import org.andrewwinter.sip.transaction.server.ServerTransaction;

/**
 *
 * @author andrewwinter77
 */
public class InviteServerTransaction extends ServerTransaction {
    
    private InviteServerTransaction(final Dialog dialog, final InboundSipRequest isr) {
        super(dialog, isr);
    }
    
    /**
     *
     * @param dialog
     * @param isr
     * @param sipListener  
     */
    public static void create(final Dialog dialog, final InboundSipRequest isr, final SipRequestHandler sipListener) {
        final InviteServerTransaction txn = new InviteServerTransaction(dialog, isr);
        txn.initialize(new Proceeding(txn), null, sipListener);
    }
}
