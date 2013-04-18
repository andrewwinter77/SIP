package org.andrewwinter.jsr289.api;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.sip.B2buaHelper;
import javax.servlet.sip.Proxy;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.TooManyHopsException;
import javax.servlet.sip.URI;
import javax.servlet.sip.ar.SipApplicationRoutingDirective;
import javax.servlet.sip.ar.SipApplicationRoutingRegion;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.message.InboundSipRequest;
import org.andrewwinter.sip.message.ResponseType;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.transaction.server.ServerTransactionStateName;

/**
 *
 * @author andrew
 */
public class InboundSipServletRequestImpl extends SipServletRequestImpl implements SipServletRequest {

    private final InboundSipRequest inboundSipRequest;
    private ProxyImpl proxy;
    private B2bUaHelperImpl b2bUaHelper;

    private String remoteAddr;
    private int remotePort;
    
    /**
     * Use for inbound requests where we are the UAS.
     *
     * @param isr
     */
    public InboundSipServletRequestImpl(final InboundSipRequest isr) {
        super(isr.getRequest());
        this.inboundSipRequest = isr;
        
        // remoteAddr and remotePort will be updated as the message is proxied
        // between applications.
        remoteAddr = inboundSipRequest.getInitialRemoteAddr();
        remotePort = inboundSipRequest.getInitialRemotePort();
    }

    public InboundSipRequest getInboundSipRequest() {
        return inboundSipRequest;
    }

    @Override
    public Proxy getProxy() throws TooManyHopsException {
        return getProxy(true);
    }

    @Override
    public Proxy getProxy(final boolean create) throws TooManyHopsException {

        if (b2bUaHelper == null) {

            if (proxy == null) {

                final Integer maxForwards = request.getMaxForwards();
                if (maxForwards != null && maxForwards == 0) {

                    throw new TooManyHopsException();
                    // If unhandled by the application, this will be caught by
                    // the container which must then generate a 483 (Too many
                    // hops) error response.
                    
                } else {
                    proxy = new ProxyImpl(inboundSipRequest, this);
                }
            }
            return proxy;
        } else {
            throw new IllegalStateException("Application is already a B2B.");
        }
    }

    @Override
    public SipServletResponse createResponse(final int statusCode) {
        final ResponseType rt = ResponseType.get(statusCode);
        final String reasonPhrase;
        if (rt == null) {
            reasonPhrase = "Custom response";
        } else {
            reasonPhrase = rt.getReasonPhrase();
        }
        return this.createResponse(statusCode, reasonPhrase);
    }

    @Override
    public SipServletResponse createResponse(final int statusCode, String reasonPhrase) {
        if (statusCode < 100 || statusCode > 699) {
            throw new IllegalArgumentException("Invalid status code.");
        }

        if (reasonPhrase == null) {
            throw new NullPointerException("Reason phrase must not be null.");
        }

        synchronized(inboundSipRequest.getServerTransaction()) {
            final ServerTransactionStateName state = inboundSipRequest.getServerTransaction().getStateName();
            if (state == ServerTransactionStateName.TRYING || state == ServerTransactionStateName.PROCEEDING) {
                final SipResponse response = inboundSipRequest.createResponse(statusCode, reasonPhrase);
                return new OutboundSipServletResponseImpl(this, response);
            } else {
                throw new IllegalStateException("Request already responded to with final status code.");
            }
        }
    }

    @Override
    public SipServletRequest createCancel() {
        throw new IllegalStateException("Only outbound INVITEs can be canceled.");
    }

    @Override
    public B2buaHelper getB2buaHelper() {
        if (proxy == null) {
            return b2bUaHelper = new B2bUaHelperImpl();
        } else {
            throw new IllegalStateException("Application is already a proxy.");
        }
    }

    @Override
    public void setRoutingDirective(
            final SipApplicationRoutingDirective directive,
            final SipServletRequest origRequest) throws IllegalStateException {

        throw new IllegalStateException("Cannot set routing directive on inbound requests.");
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
        throw new IllegalStateException("Cannot send inbound requests.");
    }

    @Override
    public void setContentLength(final int len) {
        throw new IllegalStateException("Cannot set content on incoming messages.");
    }

    @Override
    public String getInitialRemoteAddr() {
        return inboundSipRequest.getInitialRemoteAddr();
    }

    @Override
    public int getInitialRemotePort() {
        return inboundSipRequest.getInitialRemotePort();
    }
    
    @Override
    protected Dialog getDialog() {
        return inboundSipRequest.getServerTransaction().getDialog();
    }

    @Override
    public String getLocalAddr() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getLocalPort() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTransport() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRemoteAddr() {
        return remoteAddr;
    }

    @Override
    public int getRemotePort() {
        return remotePort;
    }

    @Override
    public String getInitialTransport() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}