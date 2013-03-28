package org.andrewwinter.sip.transaction.server;

import org.andrewwinter.sip.SipRequestHandler;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.dialog.DialogState;
import org.andrewwinter.sip.element.StatefulProxy;
import org.andrewwinter.sip.message.InboundSipRequest;
import org.andrewwinter.sip.message.ResponseType;
import org.andrewwinter.sip.parser.Address;
import org.andrewwinter.sip.parser.SipMessageHelper;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;

/**
 *
 * @author andrewwinter77
 */
public abstract class ServerTransaction {

    private final InboundSipRequest isr;
    
    private ServerTransactionState state;
    
    private Dialog dialog;
    
    private StatefulProxy proxy;

    public ServerTransactionStateName getStateName() {
        return state.getStateName();
    }
    
    /**
     * 
     * @return 
     */
    public boolean hasStatefulProxy() {
        return proxy != null;
    }
    
    /**
     * 
     * @param proxy 
     */
    public void setProxy(final StatefulProxy proxy) {
        this.proxy = proxy;
    }
    
    /**
     * 
     * @param dialog Null if there is no dialog.
     * @param isr 
     */
    protected ServerTransaction(final Dialog dialog, final InboundSipRequest isr) {
        this.dialog = dialog;
        this.isr = isr;
        ServerTransactionStore.getInstance().put(this);
    }
    
    /**
     * 
     * @param response 
     */
    public void handleResponseFromTU(final SipResponse response) {
        state.handleResponseFromTU(response);
    }

    /**
     * Either retransmissions of the original request or ACK after a non-2XX
     * response to an INVITE.
     * @param request 
     */
    public void handleRequestFromTransportLayer(final SipRequest request) {
        state.handleRequestFromTransportLayer(request);
    }
    
    /**
     * 
     * @param response
     * @param state  
     */
    public void createDialog(final SipResponse response, final DialogState state) {
        dialog = Dialog.createForUAS(isr.getRequest(), response, state);
    }
    
    /**
     * 
     */
    public void destroy() {
        ServerTransactionStore.getInstance().remove(this);
        isr.getResponseSender().closeSocket();
    }
    
    /**
     * 
     * @param response 
     */
    public void sendResponseToTransportLayer(final SipResponse response) {
        isr.getResponseSender().send(response);
    }

    /**
     * 
     * @return 
     */
    public InboundSipRequest getInboundSipRequest() {
        return isr;
    }
    
    /**
     * 
     * @param state 
     */
    public void changeState(final ServerTransactionState state) {
        this.state = state;
    }
    
    /**
     * 
     * @return 
     */
    protected ServerTransactionState getState() {
        return state;
    }
    
    /**
     * 
     */
    public void cancel() {
        state.cancel();
    }
    
    /**
     * 
     * @return 
     */
    public Dialog getDialog() {
        return dialog;
    }
    
    /**
     * 
     * @param initialState
     * @param associatedTxn Any transaction that is associated with this one.
     *   For example, if this is a CANCEL, the associated transaction is the
     *   INVITE transaction that this cancels. May be null.
     * @param sipListener  
     */
    public void initialize(final ServerTransactionState initialState, final ServerTransaction associatedTxn, final SipRequestHandler sipListener) {
        this.state = initialState;

        isr.setServerTransaction(this);
        
        if (dialog == null && SipMessageHelper.getTo(isr.getRequest()).getTag() != null) {

            // If the request has a tag in the To header field, but the dialog
            // identifier does not match any existing dialogs, the UAS may have
            // crashed and restarted

            // If the UAS wishes to reject the request because it does not wish
            // to recreate the dialog, it MUST respond to the request with a 481
            // (Call/Transaction Does Not Exist) status code and pass that to
            // the server transaction.
            
            if (!isr.getRequest().isACK()) {
                final SipResponse response = isr.createResponse(ResponseType.CALL_TRANSACTION_DOES_NOT_EXIST);
                handleResponseFromTU(response);
            }
        } else if (isr.getRequest().isCANCEL()) {

            final SipResponse response;
            if (associatedTxn == null) {
                
                // If the UAS did not find a matching transaction for the CANCEL
                // according to the procedure above, it SHOULD respond to the
                // CANCEL with a 481 (Call Leg/Transaction Does Not Exist).
                
                response = isr.createResponse(ResponseType.CALL_TRANSACTION_DOES_NOT_EXIST);
            } else {

                // This request is passed up to the TU.
                
                sipListener.doRequest(isr);
            
                // A proxy MUST cancel any pending client transactions
                // associated with a response context when it receives a
                // matching CANCEL request.
                
                if (associatedTxn.proxy != null) {
                    associatedTxn.proxy.cancel();
                }
                
                // Regardless of the method of the original request, as long as
                // the CANCEL matched an existing transaction, the UAS answers
                // the CANCEL request itself with a 200 (OK) response.
                
                response = isr.createResponse(ResponseType.OK);

                // This response is constructed following the procedures
                // described in Section 8.2.6 noting that the To tag of the
                // response to the CANCEL and the To tag in the response to the
                // original request SHOULD be the same.
                
                final String tag = associatedTxn.getInboundSipRequest().getToTag();

                final Address to = SipMessageHelper.getTo(response);
                to.setParameter("tag", tag);
                SipMessageHelper.setTo(to, response);
            }

            handleResponseFromTU(response);
        } else {
            
            // This request is passed up to the TU.
            
            sipListener.doRequest(isr);
        }
    }
}
