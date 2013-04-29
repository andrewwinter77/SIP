package org.andrewwinter.sip.parser;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @see http://www.iana.org/assignments/sip-parameters
 * @author andrewwinter77
 */
public class HeaderName {

    /**
     *
     */
    public static final HeaderName ACCEPT = createKnownHeader("Accept", null, HeaderType.COMMA_SEPARATED_STRING_WITH_PARAMS);
    /**
     *
     */
    public static final HeaderName ACCEPT_ENCODING = createKnownHeader("Accept-Encoding", null, HeaderType.COMMA_SEPARATED_STRING_WITH_PARAMS);
    /**
     *
     */
    public static final HeaderName ACCEPT_LANGUAGE = createKnownHeader("Accept-Language", null, HeaderType.COMMA_SEPARATED_STRING_WITH_PARAMS);
    /**
     *
     */
    public static final HeaderName ALERT_INFO = createKnownHeader("Alert-Info", null, HeaderType.COMMA_SEPARATED_ABSOLUTE_URI_WITH_PARAMS);
    /**
     *
     */
    public static final HeaderName ALLOW = createKnownHeader("Allow", null, HeaderType.COMMA_SEPARATED_TOKENS);
    /**
     *
     */
    public static final HeaderName AUTHENTICATION_INFO = createKnownHeader("Authentication-Info", null, HeaderType.HEADER_SPECIFIC_FORMAT);
    /**
     *
     */
    public static final HeaderName AUTHORIZATION = createKnownHeader("Authorization", null, HeaderType.HEADER_SPECIFIC_FORMAT);
    /**
     *
     */
    public static final HeaderName CALL_ID = createKnownHeader("Call-ID", "i", HeaderType.OPAQUE_STRING);
    /**
     *
     */
    public static final HeaderName CALL_INFO = createKnownHeader("Call-Info", null, HeaderType.COMMA_SEPARATED_ABSOLUTE_URI_WITH_PARAMS);
    /**
     *
     */
    public static final HeaderName CONTACT = createKnownHeader("Contact", "m", HeaderType.COMMA_SEPARATED_ADDRESSES);
    /**
     *
     */
    public static final HeaderName CONTENT_DISPOSITION = createKnownHeader("Content-Disposition", null, HeaderType.STRING_WITH_PARAMS);
    /**
     *
     */
    public static final HeaderName CONTENT_ENCODING = createKnownHeader("Content-Encoding", "e", HeaderType.COMMA_SEPARATED_TOKENS);
    /**
     *
     */
    public static final HeaderName CONTENT_LANGUAGE = createKnownHeader("Content-Language", null, HeaderType.COMMA_SEPARATED_TOKENS);
    /**
     *
     */
    public static final HeaderName CONTENT_LENGTH = createKnownHeader("Content-Length", "l", HeaderType.INTEGER);
    /**
     *
     */
    public static final HeaderName CONTENT_TYPE = createKnownHeader("Content-Type", "c", HeaderType.STRING_WITH_PARAMS);
    /**
     *
     */
    public static final HeaderName CSEQ = createKnownHeader("CSeq", null, HeaderType.HEADER_SPECIFIC_FORMAT);
    /**
     *
     */
    public static final HeaderName DATE = createKnownHeader("Date", null, HeaderType.RFC1123_DATE);
    
    // Deprecated
    /**
     *
     */
//    public static final HeaderName ENCRYPTION = createKnownHeader("Encryption", null, null);
    
    /**
     *
     */
    public static final HeaderName ERROR_INFO = createKnownHeader("Error-Info", null, HeaderType.COMMA_SEPARATED_ABSOLUTE_URI_WITH_PARAMS);
    /**
     *
     */
    public static final HeaderName EXPIRES = createKnownHeader("Expires", null, HeaderType.INTEGER);
    /**
     *
     */
    public static final HeaderName FROM = createKnownHeader("From", "f", HeaderType.ADDRESS);
    
    // Deprecated
    /**
     *
     */
//    public static final HeaderName HIDE = createKnownHeader("Hide", null, null);
    
    /**
     *
     */
    public static final HeaderName IN_REPLY_TO = createKnownHeader("In-Reply-To", null, HeaderType.COMMA_SEPARATED_TOKENS);
    /**
     *
     */
    public static final HeaderName MAX_FORWARDS = createKnownHeader("Max-Forwards", null, HeaderType.INTEGER);
    /**
     *
     */
    public static final HeaderName MIME_VERSION = createKnownHeader("MIME-Version", null, HeaderType.FLOAT);
    /**
     *
     */
    public static final HeaderName MIN_EXPIRES = createKnownHeader("Min-Expires", null, HeaderType.INTEGER);
    /**
     *
     */
    public static final HeaderName ORGANIZATION = createKnownHeader("Organization", null, HeaderType.OPAQUE_STRING);
    /**
     *
     */
//    public static final HeaderName PATH = createKnownHeader("Path", null, null);
    /**
     *
     */
    public static final HeaderName PRIORITY = createKnownHeader("Priority", null, HeaderType.OPAQUE_STRING);
    /**
     *
     */
    public static final HeaderName PROXY_AUTHENTICATE = createKnownHeader("Proxy-Authenticate", null, HeaderType.AUTHENTICATION_CHALLENGE);
    /**
     *
     */
    public static final HeaderName PROXY_AUTHORIZATION = createKnownHeader("Proxy-Authorization", null, HeaderType.HEADER_SPECIFIC_FORMAT);
    /**
     *
     */
    public static final HeaderName PROXY_REQUIRE = createKnownHeader("Proxy-Require", null, HeaderType.COMMA_SEPARATED_TOKENS);
    /**
     *
     */
//    public static final HeaderName RACK = createKnownHeader("RAck", null, null);
    /**
     *
     */
    public static final HeaderName RECORD_ROUTE = createKnownHeader("Record-Route", null, HeaderType.COMMA_SEPARATED_ADDRESSES);
    /**
     *
     */
    public static final HeaderName REPLY_TO = createKnownHeader("Reply-To", null, HeaderType.ADDRESS);
    /**
     *
     */
    public static final HeaderName REQUIRE = createKnownHeader("Require", null, HeaderType.COMMA_SEPARATED_TOKENS);
    
    // Deprecated
    /**
     *
     */
//    public static final HeaderName RESPONSE_KEY = createKnownHeader("Response-Key", null, null);
    
    /**
     *
     */
    public static final HeaderName RETRY_AFTER = createKnownHeader("Retry-After", null, HeaderType.PARAMETERABLE_HEADER_SPECIFIC_FORMAT);
    /**
     *
     */
    public static final HeaderName ROUTE = createKnownHeader("Route", null, HeaderType.COMMA_SEPARATED_ADDRESSES);
    /**
     *
     */
//    public static final HeaderName RSEQ = createKnownHeader("RSeq", null, null);
    /**
     *
     */
    public static final HeaderName SERVER = createKnownHeader("Server", null, HeaderType.OPAQUE_STRING);
    /**
     *
     */
    public static final HeaderName SUBJECT = createKnownHeader("Subject", "s", HeaderType.OPAQUE_STRING);
    /**
     *
     */
    public static final HeaderName SUPPORTED = createKnownHeader("Supported", "k", HeaderType.COMMA_SEPARATED_TOKENS);
    /**
     *
     */
    public static final HeaderName TIMESTAMP = createKnownHeader("Timestamp", null, HeaderType.HEADER_SPECIFIC_FORMAT);
    /**
     *
     */
    public static final HeaderName TO = createKnownHeader("To", "t", HeaderType.ADDRESS);
    /**
     *
     */
    public static final HeaderName UNSUPPORTED = createKnownHeader("Unsupported", null, HeaderType.COMMA_SEPARATED_TOKENS);
    /**
     *
     */
    public static final HeaderName USER_AGENT = createKnownHeader("User-Agent", null, HeaderType.OPAQUE_STRING);
    /**
     *
     */
    public static final HeaderName VIA = createKnownHeader("Via", "v", HeaderType.COMMA_SEPARATED_STRING_WITH_PARAMS);
    /**
     *
     */
    public static final HeaderName WARNING = createKnownHeader("Warning", null, HeaderType.COMMA_SEPARATED_HEADER_SPECIFIC_FORMAT);
    /**
     *
     */
    public static final HeaderName WWW_AUTHENTICATE = createKnownHeader("WWW-Authenticate", null, HeaderType.AUTHENTICATION_CHALLENGE);
    
    private final String longName;
    private final String shortName;
    private final HeaderType type;

    private static Map<String, HeaderName> MAP;
    
    private static HeaderName createKnownHeader(final String longName, final String shortName, final HeaderType type) {
        if (MAP == null) {
            MAP = new HashMap<String, HeaderName>();
        }
        final HeaderName hn = new HeaderName(longName, shortName, type);
        MAP.put(hn.getLongName().toLowerCase(Locale.US), hn);
        if (hn.getShortName() != null) {
            MAP.put(hn.getShortName().toLowerCase(Locale.US), hn);
        }
        return hn;
    }
    
    private HeaderName(final String longName, final String shortName, final HeaderType type) {
        this.longName = longName;
        this.shortName = shortName;
        this.type = type;
    }
    
    /**
     *
     * @return
     */
    public String getLongName() {
        return longName;
    }
    
    /**
     *
     * @return
     */
    public String getShortName() {
        return shortName;
    }
    
    /**
     *
     * @param headerName
     * @return One of the pre-defined HeaderNames if this is a known header
     * or a new HeaderName instance if this is an extension header.
     */
    public static HeaderName fromString(String headerName) {
        HeaderName hn = null;
        if (headerName != null) {
            if ((hn = MAP.get(headerName.toLowerCase(Locale.US))) == null) {
                // OK maybe the headerName needed trimming...
                headerName = headerName.trim();
                if ((hn = MAP.get(headerName.toLowerCase(Locale.US))) == null) {
                    // Nope. It must be an extension header.
                    hn = new HeaderName(headerName, null, HeaderType.UNKNOWN_EXTENSION_HEADER);
                }
            }
        }
        return hn;
    }
    
    /**
     *
     * @return
     */
    public HeaderType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.longName != null ? this.longName.toLowerCase(Locale.US).hashCode() : 0);
        hash = 89 * hash + (this.shortName != null ? this.shortName.toLowerCase(Locale.US).hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HeaderName other = (HeaderName) obj;
        if ((this.longName == null) ? (other.longName != null) : !this.longName.equalsIgnoreCase(other.longName)) {
            return false;
        }
        if ((this.shortName == null) ? (other.shortName != null) : !this.shortName.equalsIgnoreCase(other.shortName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return longName;
    }
}
