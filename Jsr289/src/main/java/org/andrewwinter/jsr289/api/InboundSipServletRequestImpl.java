package org.andrewwinter.jsr289.api;

import java.io.IOException;
import java.util.List;
import javax.servlet.sip.Address;
import javax.servlet.sip.B2buaHelper;
import javax.servlet.sip.Proxy;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.TooManyHopsException;
import javax.servlet.sip.URI;
import javax.servlet.sip.ar.SipApplicationRoutingDirective;
import javax.servlet.sip.ar.SipApplicationRoutingRegion;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.message.InboundSipRequest;
import org.andrewwinter.sip.message.ResponseType;
import org.andrewwinter.sip.parser.HeaderName;
import org.andrewwinter.sip.parser.SipMessage;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.parser.Uri;
import org.andrewwinter.sip.transaction.server.ServerTransactionStateName;

/**
 *
 * @author andrew
 */
public class InboundSipServletRequestImpl extends SipServletRequestImpl {

    private final InboundSipRequest inboundSipRequest;
    private ProxyImpl proxy;
    private B2bUaHelperImpl b2bUaHelper;

    private String remoteAddr;
    private int remotePort;
    
    private Address poppedRoute;
    
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

    @Override
    public Address getPoppedRoute() {
        return poppedRoute;
    }

    public void setPoppedRoute(final Address poppedRoute) {
        this.poppedRoute = poppedRoute;
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

                final Integer maxForwards = getSipRequest().getMaxForwards();
                if (maxForwards != null && maxForwards == 0) {

                    throw new TooManyHopsException();
                    // If unhandled by the application, this will be caught by
                    // the container which must then generate a 483 (Too many
                    // hops) error response.
                    
                } else {
                    proxy = new ProxyImpl(inboundSipRequest, this);
                    setRoutingDirective(SipApplicationRoutingDirective.CONTINUE);
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

    private static void copyRecordRouteHeaders(final SipMessage src, final SipMessage dst) {
        final List<String> headers = src.getHeadersAsStrings(HeaderName.RECORD_ROUTE);
        if (headers != null && !headers.isEmpty()) {
            dst.setHeaders(HeaderName.RECORD_ROUTE, headers);
        }
    }
    
    private void pushRecordRoute(final SipResponse response, final Uri uri) {
        response.pushHeader(HeaderName.RECORD_ROUTE, "<" + uri + ";lr;ssid=" + getSession().getId() + ">");
    }
    
    private Uri getOurOwnIp() {
        final List<SipURI> ifaces = (List<SipURI>) getServletContext().getAttribute(SipServlet.OUTBOUND_INTERFACES);
        final SipURIImpl uri = (SipURIImpl) ifaces.get(0);
        return uri.getRfc3261Uri();
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

                if (statusCode >= 200 && statusCode < 300 && getSipRequest().isINVITE()) {
                    
                    // When a UAS responds to a request with a response that
                    // establishes a dialog (such as a 2xx to INVITE), the UAS
                    // MUST copy all Record-Route header field values from the
                    // request into the response (including the URIs, URI
                    // parameters, and any Record-Route header field parameters,
                    // whether they are known or unknown to the UAS) and MUST
                    // maintain the order of those values.                    
                    
                    copyRecordRouteHeaders(message, response);

                    final Uri uri = getOurOwnIp();
                    pushRecordRoute(response, uri);
                    
                    // Servlets must not set the Contact header in these cases.
                    // Containers know which network interfaces they listen on
                    // and are responsible for choosing and adding the Contact
                    // header in these cases.
                    
                    // The UAS MUST add a Contact header field to the response.
                    
                    final org.andrewwinter.sip.parser.Address contactHeader =
                            new org.andrewwinter.sip.parser.Address(uri);
                    response.addHeader(HeaderName.CONTACT, contactHeader);
                }
            
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
    public Dialog getDialog() {
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