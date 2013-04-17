package org.andrewwinter.jsr289.api;

import java.io.BufferedReader;
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
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.URI;
import javax.servlet.sip.ar.SipApplicationRoutingDirective;
import javax.servlet.sip.ar.SipApplicationRoutingRegion;
import org.andrewwinter.jsr289.auth.SingleAuthInfo;
import org.andrewwinter.sip.parser.SipMessageHelper;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.Uri;

/**
 *
 * @author andrew
 */
public abstract class SipServletRequestImpl extends SipServletMessageImpl implements SipServletRequest {

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
    protected final SipRequest request;
    /**
     * Null if not yet set.
     */
    private transient Boolean initial;

    private SipApplicationRoutingDirective directive;
    
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
    }

    public SipRequest getSipRequest() {
        return request;
    }
    
    @Override
    public ServletInputStream getInputStream() {
        return null;
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
        if (!isInitial()) {
            // Any attempt to do a pushRoute on a subsequent request in a dialog
            // MUST throw an IllegalStateException.
            throw new IllegalStateException("Cannot push Route on subsequent requests.");
        }
        final Uri rfc3261Uri = ((SipURIImpl) uri).getRfc3261Uri();
        request.pushRoute(rfc3261Uri);
    }

    @Override
    public void pushRoute(final Address uri) {
        if (!isInitial()) {
            // Any attempt to do a pushRoute on a subsequent request in a dialog
            // MUST throw an IllegalStateException.
            throw new IllegalStateException("Cannot push Route on subsequent requests.");
        }
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

        if (initial != null) {
            return initial;
        }

        // 1. Request Detection - Upon reception of a SIP message, determine if
        // the message is a SIP request. If it is not a request, stop. The
        // message is not an initial request.

        // 2. Ongoing Transaction Detection - Employ methods of Section 17.2.3
        // in RFC 3261 to see if the request matches an existing transaction. If
        // it does, stop. The request is not an initial request.

        if (request.matchesExistingServerTransaction()) {
            return initial = false;
        }

        // 3. Examine Request Method. If it is CANCEL, BYE, PRACK, ACK, UPDATE
        // or INFO, stop. The request is not an initial request for which
        // application selection occurs.

        final String method = request.getMethod().toUpperCase(Locale.US);
        if (SUBSEQUENT_REQUEST_METHODS.contains(method)) {
            return initial = false;
        }

        // 4. Existing Dialog Detection - If the request has a tag in the To
        // header field, the container computes the dialog identifier (as
        // specified in section 12 of RFC 3261) corresponding to the request and
        // compares it with existing dialogs. If it matches an existing dialog,
        // stop. The request is not an initial request.

        if (request.matchesExistingDialog()) {
            return initial = false;
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

        return initial = true;
    }

    @Override
    public void pushPath(final Address uri) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Address getPoppedRoute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Address getInitialPoppedRoute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected void setRoutingDirective(final SipApplicationRoutingDirective directive) {
        this.directive = directive;
    }
    
    @Override
    public SipApplicationRoutingDirective getRoutingDirective() throws IllegalStateException {
        if (!isInitial()) {
            throw new IllegalStateException("Request is not initial.");
        }
        return directive;
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
    public BufferedReader getReader() {
        return null;
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
