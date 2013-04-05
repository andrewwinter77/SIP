package org.andrewwinter.sip.message;

import java.net.InetSocketAddress;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.transaction.client.ClientTransaction;

/**
 *
 * @author andrew
 */
public class InboundSipResponse extends InboundSipMessage {

    private final ClientTransaction clientTransaction;

    private Dialog dialog;
    
    /**
     * 
     * @param response
     * @param txn
     * @param dialog  
     */
    public InboundSipResponse(final SipResponse response, final InetSocketAddress initialRemoteAddr, final ClientTransaction txn) {
        super(initialRemoteAddr, response);
        this.clientTransaction = txn;
    }

    public SipResponse getResponse() {
        return (SipResponse) getMessage();
    }
    
    public void setDialog(final Dialog dialog) {
        this.dialog = dialog;
    }

    /**
     *
     * @return
     */
    public Dialog getDialog() {
        return dialog;
    }
    
    /**
     * Returns the request associated with this response.
     * @return 
     */
    public SipRequest getRequest() {
        return clientTransaction.getRequest();
    }
    
    /**
     * 
     * @return 
     */
    public ClientTransaction getClientTransaction() {
        return clientTransaction;
    }
}
