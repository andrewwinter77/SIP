package org.andrewwinter.sip.message;

import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.transaction.client.ClientTransaction;

/**
 *
 * @author andrew
 */
public class InboundSipResponse {

    private final SipResponse response;
    
    private final ClientTransaction clientTransaction;

    private final Dialog dialog;
    
    /**
     * 
     * @param response
     * @param txn
     * @param dialog  
     */
    public InboundSipResponse(final SipResponse response, final ClientTransaction txn, final Dialog dialog) {
        this.response = response;
        this.clientTransaction = txn;
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

    /**
     * 
     * @return 
     */
    public SipResponse getResponse() {
        return response;
    }
}
