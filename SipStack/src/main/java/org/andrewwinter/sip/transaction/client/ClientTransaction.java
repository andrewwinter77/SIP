package org.andrewwinter.sip.transaction.client;

import org.andrewwinter.sip.SipResponseHandler;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.message.InboundSipResponse;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.transaction.Transaction;
import org.andrewwinter.sip.transport.RequestSender;

/**
 *
 * @author andrewwinter77
 */
public abstract class ClientTransaction extends Transaction {

    private final SipRequest request;
    
    private ClientTransactionState state;
    
    private final SipResponseHandler sipListener;
    
    private final RequestSender sender;
    
    /**
     * 
     * @param txnUser 
     * @param request 
     * @param sender  
     */
    protected ClientTransaction(
            final SipResponseHandler txnUser,
            final SipRequest request,
            final RequestSender sender) {
        this.request = request;
        this.sipListener = txnUser;
        this.sender = sender;
        ClientTransactionStore.getInstance().put(this);
    }
    
    /**
     *
     * @return
     */
    public ClientTransactionStateName getStateName() {
        return state.getStateName();
    }

    /**
     * 
     * @param isr 
     */
    public void handleResponseFromTransportLayer(final InboundSipResponse isr) {
        state.handleResponseFromTransportLayer(isr);
    }

    /**
     * 
     */
    @Override
    public void destroy() {
        super.destroy();
        ClientTransactionStore.getInstance().remove(this);
        
        // TODO: Close socket - when we have one to close!
//        isr.getResponseSender().closeSocket();
    }
    
    /**
     * 
     * @param isr 
     * @param dialog  
     */
    public void sendResponseToTU(final InboundSipResponse isr, final Dialog dialog) {
        
        // Valid responses are passed up to the TU from the client transaction.
        
        isr.setDialog(dialog);
        sipListener.doResponse(isr);
    }

    public void inviteClientTxnTimeout() {
        sipListener.inviteClientTxnTimeout();
    }
    
    /**
     * 
     * @return 
     */
    public SipRequest getRequest() {
        return request;
    }
    
    /**
     * 
     * @param state 
     */
    public void changeState(final ClientTransactionState state) {
        this.state = state;
    }
    
    /**
     * 
     * @return 
     */
    protected ClientTransactionState getState() {
        return state;
    }
    
    /**
     * 
     * @param cancel 
     */
    public void cancel(final SipRequest cancel) {
        state.cancel(cancel);
    }
    
    /**
     *
     */
    public void sendRequest() {
        sender.send(request);
    }
    
    /**
     *
     * @return
     */
    public RequestSender getRequestSender() {
        return sender;
    }
    
    /**
     * 
     * @param initialState
     */
    public void initialize(final ClientTransactionState initialState) {
        this.state = initialState;
        
        // The client transaction MUST pass the request to the transport layer
        // for transmission (see Section 18).
        sendRequest();
    }
    
    public void timerAFired() {
        state.timerAFired();
    }
    
    public void timerBFired() {
        state.timerBFired();
    }
}
