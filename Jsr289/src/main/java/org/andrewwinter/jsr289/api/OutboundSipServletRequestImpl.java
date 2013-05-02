package org.andrewwinter.jsr289.api;

import java.io.IOException;
import javax.servlet.sip.B2buaHelper;
import javax.servlet.sip.Proxy;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.TooManyHopsException;
import javax.servlet.sip.URI;
import javax.servlet.sip.ar.SipApplicationRoutingDirective;
import javax.servlet.sip.ar.SipApplicationRoutingRegion;
import org.andrewwinter.sip.SipResponseHandler;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.element.UserAgentClient;
import org.andrewwinter.sip.message.InboundSipResponse;
import org.andrewwinter.sip.message.SipMessageFactory;
import org.andrewwinter.sip.parser.HeaderName;
import org.andrewwinter.sip.parser.SipMessageHelper;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.transaction.client.ClientTransaction;
import org.andrewwinter.sip.transaction.client.ClientTransactionStateName;

/**
 *
 * @author andrew
 */
public class OutboundSipServletRequestImpl extends SipServletRequestImpl implements SipResponseHandler {

    /**
     * This is non-null when we're a UAC and have sent a request.
     */
    private UserAgentClient userAgentClient;
    
    private final Dialog dialog;
    
    /**
     * 
     * @param userAgentClient
     * @param cancel 
     */
    private OutboundSipServletRequestImpl(final UserAgentClient userAgentClient, final SipRequest cancel) {
        super(cancel);
        this.userAgentClient = userAgentClient;
        this.dialog = null;
    }
    
    /**
     * Use for requests where we are the UAC.
     *
     * @param request
     */
    public OutboundSipServletRequestImpl(final SipRequest request, final SipApplicationRoutingDirective directive) {
        super(request);
        super.setRoutingDirective(directive);
        this.dialog = null;
    }

    
    public OutboundSipServletRequestImpl(final SipRequest request, final Dialog dialog) {
        super(request);
        this.dialog = dialog;
    }

    
    @Override
    public Proxy getProxy() throws TooManyHopsException {
        throw new IllegalStateException("Cannot proxy requests where we are the UAC.");
    }

    @Override
    public Proxy getProxy(final boolean create) throws TooManyHopsException {
        throw new IllegalStateException("Cannot proxy requests where we are the UAC.");
    }

    @Override
    public SipServletResponse createResponse(final int statusCode) {
        throw new IllegalStateException("Cannot create a response to an outbound request.");
    }

    @Override
    public SipServletResponse createResponse(final int statusCode, String reasonPhrase) {
        throw new IllegalStateException("Cannot create a response to an outbound request.");
    }

    @Override
    public SipServletRequest createCancel() {
        
        if (!request.isINVITE()) {
            throw new IllegalStateException("Only outbound INVITEs can be canceled.");
        }
        
        if (userAgentClient == null) {
            throw new IllegalStateException("Cannot cancel an INVITE that has not yet been sent.");
        }
        
        final ClientTransaction txn = userAgentClient.getClientTransaction();
        final ClientTransactionStateName state = txn.getStateName();
        if (state != ClientTransactionStateName.PROCEEDING && state != ClientTransactionStateName.CALLING) {
            throw new IllegalStateException("Cannot cancel since not in the CALLING or PROCEEDING states.");
        }
        
        final SipRequest cancel = SipMessageFactory.createCancel(request);
        final OutboundSipServletRequestImpl servletRequest = new OutboundSipServletRequestImpl(userAgentClient, cancel);
        servletRequest.setServletContext(getServletContext());
        servletRequest.setSipSession(getSession());
        return servletRequest;
    }

    @Override
    public B2buaHelper getB2buaHelper() {
        return new B2bUaHelperImpl();
    }

    @Override
    public void setRoutingDirective(
            final SipApplicationRoutingDirective directive,
            final SipServletRequest origRequest) throws IllegalStateException {

        if (isSent()) {
            throw new IllegalStateException("Request has already been sent.");
        }
        
        if (directive == SipApplicationRoutingDirective.NEW) {
            super.setRoutingDirective(directive);
        } else {
            if (!origRequest.isInitial()) {
                
                // If directive is CONTINUE or REVERSE, the parameter origRequest must
                // be an initial request dispatched by the container to this
                // application, i.e. origRequest.isInitial() must be true.
                
                throw new IllegalStateException("origRequest must be initial for CONTINUE or REVERSE but is not.");
            }
            
            // If request is received from an application, and directive
            // is CONTINUE or REVERSE, stateInfo is set to that of the
            // original request that this request is associated with.
            
            setStateInfo(((SipServletRequestImpl) origRequest).getStateInfo());
        }
        
        // TODO: Add check for: This request must be a request created in a new SipSession or from an initial request
        
        super.setRoutingDirective(directive);
    }

    @Override
    public SipApplicationRoutingRegion getRegion() {
        if (!isInitial()) {
            throw new IllegalStateException("Request is not initial.");
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public URI getSubscriberURI() {
        if (!isInitial()) {
            throw new IllegalStateException("Request is not initial.");
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getLocalName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return request.toString();
    }

    /**
     * Send request back into the container so we can continue the application
     * sequencing.
     */
    private void pushRoute() {
        getSipRequest().pushHeader(HeaderName.ROUTE, "<sip:127.0.0.1;lr>");
    }
    
    /**
     * Handles responses directly from the SIP stack. Delegate to the SipSession
     * passing in this object, the original request corresponding to the
     * response.
     */
    @Override
    public void doResponse(final InboundSipResponse isr) {
        ((SipSessionImpl) getSession()).doResponse(isr, this);
    }

    @Override
    public void send() throws IOException {

        if (isSent()) {
            throw new IllegalStateException("Request has already been sent.");
        }
        
        if (userAgentClient == null) {
            synchronized (super.sendLock) {
                flagMessageAsSent();
                
                if (isInitial() && getRoutingDirective() == SipApplicationRoutingDirective.NEW) {
                    pushRoute();
                }
                
                userAgentClient = UserAgentClient.createUacAndSendRequest(this, request, dialog);
            }
        } else if (request.isCANCEL()) {
            userAgentClient.cancel(request);
        }
    }

    @Override
    public void setContentLength(final int len) {
        if (isSent()) {
            throw new IllegalStateException("Message has already been sent.");
        }
        SipMessageHelper.setContentLength(len, request);
    }

    @Override
    public Dialog getDialog() {
        return dialog;
    }

    @Override
    public String getInitialRemoteAddr() {
        return null;
    }

    @Override
    public int getInitialRemotePort() {
        return -1;
    }
    
    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return -1;
    }

    @Override
    public String getTransport() {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public int getRemotePort() {
        return -1;
    }

    @Override
    public String getInitialTransport() {
        return null;
    }
}
