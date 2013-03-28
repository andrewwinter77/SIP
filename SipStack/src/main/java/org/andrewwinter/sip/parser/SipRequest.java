package org.andrewwinter.sip.parser;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author andrewwinter77
 */
public class SipRequest extends SipMessage {

    private final String method;
    private Uri requestUri;
    private boolean matchesExistingServerTransaction;
    private boolean matchesExistingDialog;
    /**
     * See RFC 17.2.3.
     * @param matches 
     */
    public void setMatchesExistingServerTransaction(final boolean matches) {
        this.matchesExistingServerTransaction = matches;
    }
    
    public boolean matchesExistingServerTransaction() {
        return matchesExistingServerTransaction;
    }
    
    public void setMatchesExistingDialog(final boolean matches) {
        this.matchesExistingDialog = matches;
    }
    
    public boolean matchesExistingDialog() {
        return matchesExistingDialog;
    }
    
    /**
     *
     * @return
     */
    public boolean isCANCEL() {
        return "CANCEL".equals(getMethod());
    }
    
    /**
     *
     * @return
     */
    public boolean isINVITE() {
        return "INVITE".equals(getMethod());
    }

    /**
     *
     * @return
     */
    public boolean isACK() {
        return "ACK".equals(getMethod());
    }
    
    /**
     *
     * @return
     */
    public boolean isBYE() {
        return "BYE".equals(getMethod());
    }
    
    /**
     *
     * @return
     */
    public boolean isOPTIONS() {
        return "OPTIONS".equals(getMethod());
    }
    
    /**
     *
     * @return
     */
    public boolean isREFER() {
        return "REFER".equals(getMethod());
    }
    
    /**
     *
     * @return
     */
    public boolean isREGISTER() {
        return "REGISTER".equals(getMethod());
    }

    /**
     *
     * @return
     */
    public boolean isSUBSCRIBE() {
        return "SUBSCRIBE".equals(getMethod());
    }
    
//    AUTHORIZATION("Authorization", null),
//    PROXY_AUTHORIZATION("Proxy-Authorization", null),

    /**
     *
     * @param route
     */
    public void pushRoute(final Address route) {
        pushHeader(HeaderName.ROUTE, route.toString());
    }
    
    public void pushRoute(final Uri uri) {
        pushRoute(new Address(uri));
    }

    /**
     *
     * @param routes
     */
    public void setRoutes(final List<Address> routes) {
        setHeaders(HeaderName.ROUTE, routes);
    }

    /**
     *
     * @return
     */
    public List<Address> getRoutes() {
        return getAddressHeaders(HeaderName.ROUTE);
    }
    
    /**
     *
     * @return
     */
    public Address getTopmostRoute() {
        final List<Serializable> values = headers.get(HeaderName.ROUTE);
        if (values == null) {
            return null;
        } else if (values.get(0) instanceof Address) {
            return (Address) values.get(0);
        } else {
            final List<String> parts = Util.safeSplit(values.get(0).toString());
            return Address.parse(parts.get(0));
        }
    }

    /**
     *
     * @return
     */
    public Integer getMaxForwards() {
        return getInteger(HeaderName.MAX_FORWARDS);
    }
    
    /**
     *
     * @return
     */
    public List<String> getInReplyTo() {
        return getCommaSeparatedTokens(HeaderName.IN_REPLY_TO);
    }
    
    /**
     *
     * @return
     */
    public List<String> getProxyRequire() {
        return getCommaSeparatedTokens(HeaderName.PROXY_REQUIRE);
    }

    public void setRequestUri(final Uri requestUri) {
        this.requestUri = requestUri;
    }
    
    /**
     *
     * @param method
     * @param requestUri
     */
    public SipRequest(final String method, final Uri requestUri) {
        this.method = method;
        this.requestUri = requestUri;
    }

    /**
     *
     * @return
     */
    public String getPriority() {
        return (String) getFirstOccurrenceOfHeader(HeaderName.PRIORITY);
    }
    
    /**
     *
     * @return
     */
    @Override
    public String getMethod() {
        return method;
    }
    
    /**
     *
     * @return
     */
    public Uri getRequestUri() {
        return requestUri;
    }
    
    @Override
    String getStartLine() {
        return method + " " + requestUri + " SIP/2.0";
    }
}
