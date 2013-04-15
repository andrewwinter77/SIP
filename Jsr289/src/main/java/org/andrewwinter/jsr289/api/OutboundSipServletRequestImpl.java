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
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.element.UserAgentClient;
import org.andrewwinter.sip.message.InboundSipResponse;
import org.andrewwinter.sip.message.SipMessageFactory;
import org.andrewwinter.sip.parser.SipMessageHelper;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.transaction.client.ClientTransaction;
import org.andrewwinter.sip.transaction.client.ClientTransactionStateName;

/**
 *
 * @author andrew
 */
public class OutboundSipServletRequestImpl extends SipServletRequestImpl implements SipServletRequest {

    private final SipRequest request;

    /**
     * This is non-null when we're a UAC and have sent a request.
     */
    private UserAgentClient userAgentClient;
    
    private final InboundSipResponse inboundSipResponse;
    
    /**
     * Use for requests where we are the UAC AND we've just received a response AND
     * we're reinstantiating the SipServletRequest from the response. This is to support
     * SipServletResponse.getRequest().
     *
     * @param request
     */
    public OutboundSipServletRequestImpl(final InboundSipResponse isr) {
        super(isr.getRequest());
        this.request = isr.getRequest();
        inboundSipResponse = isr;
    }

    private OutboundSipServletRequestImpl(final UserAgentClient userAgentClient, final SipRequest cancel) {
        super(cancel);
        this.request = cancel;
        this.userAgentClient = userAgentClient;
        inboundSipResponse = null;
    }
    
    /**
     * Use for requests where we are the UAC.
     *
     * @param request
     */
    public OutboundSipServletRequestImpl(final SipRequest request) {
        super(request);
        if (request == null) {
            throw new NullPointerException("Request must not be null.");
        }
        this.request = request;
        inboundSipResponse = null;
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
        servletRequest.setSipSession((SipSessionImpl) getSession());
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
        
        // If directive is CONTINUE or REVERSE, the parameter origRequest must
        // be an initial request dispatched by the container to this
        // application, i.e. origRequest.isInitial() must be true.
        
        if ((directive == SipApplicationRoutingDirective.CONTINUE || directive == SipApplicationRoutingDirective.REVERSE)
                && !origRequest.isInitial()) {
            throw new IllegalStateException("origRequest must be initial for CONTINUE or REVERSE but is not.");
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

    @Override
    public void send() throws IOException {

        if (isSent()) {
            throw new IllegalStateException("Request has already been sent.");
        }
        
        if (userAgentClient == null) {
            synchronized (super.sendLock) {
                flagMessageAsSent();
                userAgentClient = UserAgentClient.create((SipSessionImpl) getSession(), request, null);
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
    protected Dialog getDialog() {
        if (inboundSipResponse == null) {
            return null;
        } else {
            return inboundSipResponse.getDialog();
        }
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
