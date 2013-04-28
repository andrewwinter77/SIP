package org.andrewwinter.sip.transaction.server.ack;

import org.andrewwinter.sip.SipRequestHandler;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.message.InboundSipRequest;
import org.andrewwinter.sip.transaction.server.ServerTransaction;

/**
 *
 * @author andrewwinter77
 */
public class AckServerTransaction extends ServerTransaction {
    
    private AckServerTransaction(final Dialog dialog, final InboundSipRequest isr) {
        super(dialog, isr);
    }
    
    /**
     * 
     * @param dialog Null if there is no dialog.
     * @param isr
     * @param sipListener 
     */
    public static void create(
            final Dialog dialog,
            final InboundSipRequest isr,
            final SipRequestHandler sipListener) {
        final AckServerTransaction txn = new AckServerTransaction(dialog, isr);
        
        if (!isr.getRequest().isACK()) {
            throw new IllegalArgumentException(isr.getRequest().getMethod() + " not allowed in ACK transaction.");
        }
        
        txn.initialize(null, null, sipListener);
        txn.destroy();
    }
}
