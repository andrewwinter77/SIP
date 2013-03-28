package org.andrewwinter.jsr289;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.sip.Address;
import javax.servlet.sip.AuthInfo;
import javax.servlet.sip.B2buaHelper;
import javax.servlet.sip.Proxy;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.TooManyHopsException;
import javax.servlet.sip.URI;
import javax.servlet.sip.ar.SipApplicationRoutingDirective;
import javax.servlet.sip.ar.SipApplicationRoutingRegion;
import org.andrewwinter.sip.element.UserAgentClient;
import org.andrewwinter.sip.message.InboundSipRequest;
import org.andrewwinter.sip.message.InboundSipResponse;
import org.andrewwinter.sip.message.ResponseType;
import org.andrewwinter.sip.message.SipMessageFactory;
import org.andrewwinter.sip.parser.SipMessageHelper;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.parser.Uri;
import org.andrewwinter.sip.transaction.client.ClientTransaction;
import org.andrewwinter.sip.transaction.client.ClientTransactionStateName;
import org.andrewwinter.sip.transaction.server.ServerTransactionStateName;

/**
 *
 * @author andrew
 */
public class SipServletRequestImpl extends SipServletMessageImpl implements SipServletRequest {

    /**
     * See Step 3 of Appendix B of SIP Servlet 1.1 spec.
     */
    private static final Set<String> SUBSEQUENT_REQUEST_METHODS;

    static {
        SUBSEQUENT_REQUEST_METHODS = new HashSet<>();
        SUBSEQUENT_REQUEST_METHODS.add("CANCEL");
        SUBSEQUENT_REQUEST_METHODS.add("BYE");
        SUBSEQUENT_REQUEST_METHODS.add("PRACK");
        SUBSEQUENT_REQUEST_METHODS.add("ACK");
        SUBSEQUENT_REQUEST_METHODS.add("UPDATE");
        SUBSEQUENT_REQUEST_METHODS.add("INFO");
    }
    private final SipRequest request;
    private final InboundSipRequest inboundSipRequest;
    private ProxyImpl proxy;
    private B2bUaHelperImpl b2bUaHelper;
    /**
     * Null if not yet set.
     */
    private transient Boolean requestIsInitial;

    private SipApplicationRoutingDirective routingDirective;
    
    /**
     * This is non-null when we're a UAC and have sent a request.
     */
    private UserAgentClient userAgentClient;
    
    /**
     * Use for requests where we are the UAC AND we've just received a response AND
     * we're reinstantiating the SipServletRequest from the response. This is to support
     * SipServletResponse.getRequest().
     *
     * @param request
     */
    public SipServletRequestImpl(final InboundSipResponse isr) {
        super(isr, isr.getRequest());
        this.request = isr.getRequest();
        this.inboundSipRequest = null;
    }

    private SipServletRequestImpl(final UserAgentClient userAgentClient, final SipRequest cancel) {
        super(cancel);
        this.request = cancel;
        this.inboundSipRequest = null;
        this.userAgentClient = userAgentClient;
    }
    
    /**
     * Use for requests where we are the UAC.
     *
     * @param request
     */
    public SipServletRequestImpl(final SipRequest request) {
        super(request);
        if (request == null) {
            throw new NullPointerException("Request must not be null.");
        }
        this.request = request;
        this.inboundSipRequest = null;
    }

    /**
     * Use for inbound requests where we are the UAS.
     *
     * @param isr
     */
    public SipServletRequestImpl(final InboundSipRequest isr) {
        super(isr);
        if (isr == null || isr.getRequest() == null) {
            throw new NullPointerException("Request must not be null.");
        }
        this.request = isr.getRequest();
        this.inboundSipRequest = isr;
    }

    public SipRequest getSipRequest() {
        return request;
    }
    
    private boolean isOutboundRequest() {
        return inboundSipRequest == null;
    }
    
    public InboundSipRequest getInboundSipRequest() {
        return inboundSipRequest;
    }

    @Override
    public URI getRequestURI() {
        return URIImpl.create(request.getRequestUri());
    }

    @Override
    public void setRequestURI(final URI uri) {
        if (uri == null) {
            throw new NullPointerException("uri must not be null.");
        }
        request.setRequestUri(((URIImpl) uri).getRfc3261Uri());
    }

    @Override
    public void pushRoute(final SipURI uri) {
        final Uri rfc3261Uri = ((SipURIImpl) uri).getRfc3261Uri();
        request.pushRoute(rfc3261Uri);
    }

    @Override
    public void pushRoute(final Address uri) {
        if (uri.isWildcard()) {
            // Note: This is not in the spec but makes sense to me.
            throw new IllegalArgumentException("Wildcard is not a valid route header.");
        }
        request.pushRoute(((AddressImpl) uri).getRfc3261Address());
    }

    @Override
    public int getMaxForwards() {
        try {
            Integer maxForwards = request.getMaxForwards();
            if (maxForwards == null) {
                return -1;
            } else {
                return maxForwards;
            }
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public void setMaxForwards(final int n) {
        if (n < 0 || n > 255) {
            throw new IllegalArgumentException("Max forwards not in range 0..255.");
        }
        SipMessageHelper.setMaxForwards(n, request);
    }

    /**
     * @see Appendix B: Definition of Initial Request
     * @return
     */
    @Override
    public boolean isInitial() {

        if (requestIsInitial != null) {
            return requestIsInitial;
        }

        // 1. Request Detection - Upon reception of a SIP message, determine if
        // the message is a SIP request. If it is not a request, stop. The
        // message is not an initial request.

        // 2. Ongoing Transaction Detection - Employ methods of Section 17.2.3
        // in RFC 3261 to see if the request matches an existing transaction. If
        // it does, stop. The request is not an initial request.

        if (request.matchesExistingServerTransaction()) {
            return requestIsInitial = false;
        }

        // 3. Examine Request Method. If it is CANCEL, BYE, PRACK, ACK, UPDATE
        // or INFO, stop. The request is not an initial request for which
        // application selection occurs.

        final String method = request.getMethod().toUpperCase(Locale.US);
        if (SUBSEQUENT_REQUEST_METHODS.contains(method)) {
            return requestIsInitial = false;
        }

        // 4. Existing Dialog Detection - If the request has a tag in the To
        // header field, the container computes the dialog identifier (as
        // specified in section 12 of RFC 3261) corresponding to the request and
        // compares it with existing dialogs. If it matches an existing dialog,
        // stop. The request is not an initial request.

        if (request.matchesExistingDialog()) {
            return requestIsInitial = false;
        }

        // 5. Detection of Requests Sent to Encoded URIs - Requests may be sent
        // to a container instance addressed to a URI obtained by calling the
        // encodeURI() method of a SipApplicationSession managed by this
        // container instance. When a container receives such a request, stop.
        // This request is not an initial request.


        // =====================================================================
        // =====================================================================
        // =====================================================================
        // TODO: Implement 5 above. Detect if request is sent to encoded URI.
        // =====================================================================
        // =====================================================================
        // =====================================================================



        // 6. Initial Request - The request is an initial request and the
        // application invocation process commences. In this case and in this
        // case only SipServletRequest.isInitial() MUST return true.

        return requestIsInitial = true;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        // Always returns null.
        return null;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        // Always returns null.
        return null;
    }

    @Override
    public Proxy getProxy() throws TooManyHopsException {
        return getProxy(true);
    }

    @Override
    public Proxy getProxy(final boolean create) throws TooManyHopsException {

        if (isOutboundRequest()) {
            throw new IllegalStateException("Cannot proxy requests where we are the UAC.");
        }
        
        if (b2bUaHelper == null) {

            if (proxy == null) {

                final Integer maxForwards = request.getMaxForwards();
                if (maxForwards != null && maxForwards == 0) {

                    // TODO: Generate 483

                    throw new TooManyHopsException();
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

        if (inboundSipRequest == null) {
            throw new IllegalStateException("Cannot create a response to an outbound request.");
        }

        if (reasonPhrase == null) {
            throw new NullPointerException("Reason phrase must not be null.");
        }

        synchronized(inboundSipRequest.getServerTransaction()) {
            final ServerTransactionStateName state = inboundSipRequest.getServerTransaction().getStateName();
            if (state == ServerTransactionStateName.TRYING || state == ServerTransactionStateName.PROCEEDING) {
                final SipResponse response = inboundSipRequest.createResponse(statusCode, reasonPhrase);
                return new SipServletResponseImpl(this, response);
            } else {
                throw new IllegalStateException("Request already responded to with final status code.");
            }
        }
    }

    @Override
    public SipServletRequest createCancel() {
        
        if (!isOutboundRequest() || !request.isINVITE()) {
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
        final SipServletRequestImpl servletRequest = new SipServletRequestImpl(userAgentClient, cancel);
        servletRequest.setSipSession((SipSessionImpl) getSession());
        return servletRequest;
    }

    @Override
    public void pushPath(final Address uri) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public Address getPoppedRoute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Address getInitialPoppedRoute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRoutingDirective(
            final SipApplicationRoutingDirective directive,
            final SipServletRequest origRequest) throws IllegalStateException {

        if (isOutboundRequest()) {
            
            if (isInitial()) {
                throw new UnsupportedOperationException("Not supported yet.");
            } else {
                throw new IllegalStateException("Request must be initial to set routing directive.");
            }
            
        } else {
            throw new IllegalStateException("Cannot set routing directive on inbound requests.");
        }
    }

    @Override
    public SipApplicationRoutingDirective getRoutingDirective() throws IllegalStateException {
        if (!isInitial()) {
            throw new IllegalStateException("Request is not initial.");
        }
        return routingDirective;
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
    public void addAuthHeader(final SipServletResponse challengeResponse, final AuthInfo authInfo) {
        final AuthInfoImpl impl = (AuthInfoImpl) authInfo;
        for (final SingleAuthInfo sai : impl.getAuthInfos()) {
        }
        
        // TODO: Investigate MessageDigest
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addAuthHeader(final SipServletResponse challengeResponse, final String username, final String password) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getParameter(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Enumeration getParameterNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] getParameterValues(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map getParameterMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getScheme() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getServerName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getServerPort() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRemoteHost() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Locale getLocale() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Enumeration getLocales() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRealPath(String string) {
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

        // TODO: Use something like if (!hasBeenSent()) { }
        
        if (userAgentClient == null) {
            synchronized (super.sendLock) {
                flagMessageAsSent();
                userAgentClient = UserAgentClient.create((SipSessionImpl) getSession(), request, null);
            }
        } else if (request.isCANCEL()) {
            userAgentClient.cancel(request);
        } else {
            throw new IllegalStateException("Request has already been sent.");
        }
    }

    @Override
    public ServletContext getServletContext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AsyncContext startAsync(ServletRequest arg0, ServletResponse arg1) throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isAsyncStarted() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isAsyncSupported() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AsyncContext getAsyncContext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DispatcherType getDispatcherType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
