package org.andrewwinter.sip.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrewwinter77
 */
public class SipResponse extends SipMessage {

    private int statusCode;
    
    private String reasonPhrase;
    
    /**
     *
     * @param statusCode
     * @param reasonPhrase
     */
    public SipResponse(final int statusCode, final String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }
    
//    AUTHENTICATION_INFO("Authentication-Info", null),
//    ERROR_INFO("Error-Info", null),
//    PROXY_AUTHENTICATE("Proxy-Authenticate", null),
//    RETRY_AFTER("Retry-After", null),
//    WARNING("Warning", null),
//    WWW_AUTHENTICATE("WWW-Authenticate", null);

    /**
     *
     * @return
     * @throws NumberFormatException if the Min-Expires header exists and does not
     * contain a parsable {@code int}.
     */
    public Integer getMinExpires() throws NumberFormatException {
        return getInteger(HeaderName.MIN_EXPIRES);
    }

    private List<AuthenticationChallenge> getAuthenticationChallengeHeaders(final HeaderName hn) {
        final List<Serializable> values = headers.get(hn);
        if (values == null) {
            return null;
        } else {
            final List<AuthenticationChallenge> result = new ArrayList<AuthenticationChallenge>();
            for (int i = 0; i < values.size(); ++i) {
                if (values.get(i) instanceof String) {
                    values.set(i, AuthenticationChallenge.parse((String) values.get(i)));
                }
                result.add((AuthenticationChallenge) values.get(i));
            }
            return result;
        }
    }
    
    /**
     *
     * @return
     */
    public List<AuthenticationChallenge> getWWWAuthenticate() {
        return getAuthenticationChallengeHeaders(HeaderName.WWW_AUTHENTICATE);
    }

    /**
     *
     * @return
     */
    public List<AuthenticationChallenge> getProxyAuthenticate() {
        return getAuthenticationChallengeHeaders(HeaderName.PROXY_AUTHENTICATE);
    }
    
    /**
     *
     * @param statusCode
     * @param reasonPhrase
     */
    public void setStatus(final int statusCode, final String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String getMethod() {
        return getCSeq().getMethod();
    }
    
    /**
     *
     * @return
     */
    public int getStatusCode() {
        return statusCode;
    }
    
    /**
     *
     * @return
     */
    public String getReasonPhrase() {
        return reasonPhrase;
    }
    
    /**
     *
     * @return
     */
    public List<String> getUnsupported() {
        return getCommaSeparatedTokens(HeaderName.UNSUPPORTED);
    }
    
    /**
     *
     * @return
     */
    public String getServer() {
        return (String) getFirstOccurrenceOfHeader(HeaderName.SERVER);
    }
    
    /**
     *
     * @param server
     */
    public void setServer(final String server) {
        setHeader(HeaderName.SERVER, server);
    }
    
    @Override
    String getStartLine() {
        return "SIP/2.0 " + statusCode + " " + reasonPhrase;
    }
}
