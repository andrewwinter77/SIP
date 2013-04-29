package org.andrewwinter.sip.parser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author andrewwinter77
 */

// TODO: Unescape escaped strings.

public class SipUri extends Uri {

    private String scheme;
    private String user;
    private String password;
    private String host;
    private int port = -1;
    private Map<String, String> headers = new HashMap<String, String>();
    
    private String uriAsString;
    
    /**
     *
     * @return
     */
    public String getUser() {
        return user;
    }
    
    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }
    
    /**
     *
     * @return
     */
    public String getHost() {
        return host;
    }
    
    /**
     * 
     * @return {@code -1} indicates no port set.
     */
    public int getPort() {
        return port;
    }
    
    /**
     *
     * @return An unmodifiable set of header names.
     */
    public Set<String> getHeaderNames() {
        return Collections.unmodifiableSet(headers.keySet());
    }
    
    /**
     *
     * @param name
     * @return
     */
    public String getHeader(final String name) {
        return headers.get(name);
    }
    
    @Override 
    public void setParameter(final String key, final String value) {
        uriAsString = null;
        super.setParameter(key, value);
    }
    
    /**
     *
     * @param name
     * @param value
     */
    public void setHeader(final String name, final String value) {
        uriAsString = null;
        headers.put(name, value);
    }
    
    /**
     *
     * @param name
     */
    public void removeHeader(final String name) {
        headers.remove(name);
    }
    
    private void setUriAsString(final String uriAsString) {
        this.uriAsString = uriAsString;
    }
    
    /**
     *
     * @param scheme
     */
    public void setScheme(final String scheme) {
        uriAsString = null;
        
        // From http://en.wikipedia.org/wiki/URI_scheme: 
        // Although schemes are case-insensitive, the canonical form is
        // lowercase and documents that specify schemes must do so with
        // lowercase letters.
        this.scheme = scheme.toLowerCase(Locale.US);
    }

    /**
     *
     * @param user
     */
    public void setUser(final String user) {
        uriAsString = null;
        this.user = user;
    }
    
    /**
     *
     * @param password
     */
    public void setPassword(final String password) {
        uriAsString = null;
        this.password = password;
    }
    
    /**
     *
     * @param host
     */
    public void setHost(final String host) {
        uriAsString = null;
        this.host = host;
    }
    
    /**
     *
     * @param port
     */
    public void setPort(final int port) {
        uriAsString = null;
        this.port = port;
    }
    
    /**
     * Scheme defaults to {@code sip}.
     * @param host Host without port.
     */
    public SipUri(final String host) {
        this.host = host;
        this.scheme = "sip";
    }
    
    private SipUri() {
    }
    
    /**
     *
     * @param str
     * @return
     * @throws ParseException  
     */
    public static SipUri parse(String str) throws ParseException {
        
        final SipUri uri = new SipUri();
        
        uri.setUriAsString(str);
        
        String parts[] = str.split(":", 2);
        
        // TODO: This undoes the work of setUriAsString() above. Try to optimize this.
        uri.setScheme(parts[0]);
        String restOfString = parts[1];
        
        // We possibly have a userinfo part. Check.
        parts = restOfString.split("@", 2);
        if (parts.length == 2) {
            
            // Yes we do. So process the userinfo.
            
            restOfString = parts[1];
            
            parts = parts[0].split(":");
            uri.setUser(parts[0]);
            if (parts.length == 2) {
                uri.setPassword(parts[1]);
            }
        } else {
            
            // No we don't, so continue.
            restOfString = parts[0];
        }
        
        // Process the hostport part.
        
        int endOfHostPort = restOfString.indexOf(';');
        if (endOfHostPort == -1) {
            endOfHostPort = restOfString.indexOf('?');
            if (endOfHostPort == -1) {
                endOfHostPort = restOfString.length();
            }
        }
        final String hostport = restOfString.substring(0, endOfHostPort);
        restOfString = restOfString.substring(endOfHostPort);
        

        String portPart = null;
        if (hostport.startsWith("[")) {
            // IPv6 address
            int endOfAddress = hostport.indexOf(']');
            uri.setHost(hostport.substring(1, endOfAddress));
            if (endOfAddress+1 < hostport.length()) {
                portPart = hostport.substring(endOfAddress+2);
            }
        } else {
            parts = hostport.split(":");
            uri.setHost(parts[0]);
            if (parts.length == 2) {
                portPart = parts[1];
            }
        }
        
        if (portPart == null) {
            uri.setPort(-1);
        } else {
            uri.setPort(Integer.parseInt(portPart));
        }
        
        while (restOfString.startsWith(";")) {

            int next = restOfString.indexOf(';', 1);
            if (next == -1) {
                next = restOfString.indexOf('?');
                if (next == -1) {
                    next = restOfString.length();
                }
            }
            
            String param = restOfString.substring(1, next);
            
            parts = param.split("=");
            if (parts.length == 2) {
                uri.setParameter(parts[0], parts[1]);
            } else {
                uri.setParameter(parts[0], "");
            }
            
            restOfString = restOfString.substring(next);
            
        }
        
        if (restOfString.startsWith("?")) {
            
            while (restOfString.length() > 0) {

                int next = restOfString.indexOf('&', 1);
                if (next == -1) {
                    next = restOfString.length();
                }

                final String header = restOfString.substring(1, next);

                parts = header.split("=");
                if (parts.length == 2) {
                    uri.setHeader(parts[0], parts[1]);
                } else {
                    throw new IllegalArgumentException("Missing equals in header.");
                }

                restOfString = restOfString.substring(next);
            }
        }
        
        return uri;
    }

    /**
     *
     * @return Scheme in lowercase. E.g., "sip" (without the quotes).
     */
    @Override
    public String getScheme() {
        return scheme;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        if (uriAsString == null) {
            
            final StringBuilder sb = new StringBuilder();
            sb.append(scheme).append(":");
            if (user != null) {
                sb.append(user);
                if (password != null) {
                    sb.append(":");
                    sb.append(password);
                }
                sb.append("@");
            }
            sb.append(host);
            if (port >= 0) {
                sb.append(":");
                sb.append(port);
            }
            
            sb.append(toParamString());
            
            if (!headers.isEmpty()) {
                sb.append("?");
                boolean first = true;
                for (final Entry<String, String> entry : headers.entrySet()) {
                    if (!first) {
                        sb.append("&");
                    }
                    sb.append(entry.getKey());
                    sb.append("=");
                    sb.append(entry.getValue());
                    first = false;
                }
            }
            
            uriAsString = sb.toString();
        }
        
        return uriAsString;
    }
    
    private static String decode(final String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }

    /**
     *
     * @param other
     * @return
     */
    public boolean equalsUsingComparisonRules(final SipUri other) {
        
        if (!scheme.equals(other.getScheme())) {

            // A SIP and SIPS URI are never equivalent.
            
            return false;
        }
        
        // For two URIs to be equal, the user, password, host, and port
        // components must match. A URI omitting the user component will not
        // match a URI that includes one. A URI omitting the password component
        // will not match a URI that includes one.
        
        // Comparison of the userinfo of SIP and SIPS URIs is case-sensitive.
        // This includes userinfo containing passwords or formatted as
        // telephone-subscribers.
        
        if ((user == null) ? (other.user != null) : !decode(user).equals(decode(other.user))) {
            return false;
        }
        
        if ((password == null) ? (other.password != null) : !password.equals(other.password)) {
            return false;
        }

        
        // Comparison of all other components of the URI is case-insensitive
        // unless explicitly defined otherwise.
        
        if ((host == null) ? (other.host != null) : !host.equalsIgnoreCase(other.host)) {
            return false;
        }

        // A URI omitting any component with a default value will not match a
        // URI explicitly containing that component with its default value. For
        // instance, a URI omitting the optional port component will not match a
        // URI explicitly declaring port 5060. 
        
        if (port != other.port) {
            return false;
        }

        // The same is true for the transport-parameter, ttlparameter,
        // user-parameter, and method components.
        
        // A user, ttl, or method uri-parameter appearing in only one URI never
        // matches, even if it contains the default value.
        
        final String[] specialParams = new String[] { "transport", "ttl", "user", "method" };
        for (final String param : specialParams) {
            if ((getParameter(param) == null) ? (other.getParameter(param) != null) : !getParameter(param).equalsIgnoreCase(other.getParameter(param))) {
                return false;
            }
        }
        
        // Any uri-parameter appearing in both URIs must match.
        
        // All other uri-parameters appearing in only one URI are ignored when
        // comparing the URIs.
        
        final Set<String> intersectionOfParams = new HashSet<String>();
        intersectionOfParams.addAll(getParameterNames());
        intersectionOfParams.retainAll(other.getParameterNames());
        for (final String param : intersectionOfParams) {
            if (!getParameter(param).equalsIgnoreCase(other.getParameter(param))) {
                return false;
            }
        }
        
        
        // URI header components are never ignored. Any present header component
        // MUST be present in both URIs and match for the URIs to match. The
        // matching rules are defined for each header field in Section 20.
        
        final Set<String> unionOfHeaders = new HashSet<String>();
        unionOfHeaders.addAll(getHeaderNames());
        unionOfHeaders.addAll(other.getHeaderNames());
        for (final String header : unionOfHeaders) {
            // TODO: Should we use equals or equalsIgnoreCase here?
            if (getHeader(header) == null || other.getHeader(header) == null || !getHeader(header).equals(other.getHeader(header))) {
                return false;
            }
        }
        
        
        // A URI that includes an maddr parameter will not match a URI that
        // contains no maddr parameter.
        
        if ((getParameter("maddr") == null) != (other.getParameter("maddr") == null)) {
            return false;
        }

        // All other uri-parameters appearing in only one URI are ignored when
        // comparing the URIs
        
        // TODO: Finish URI comparison - do the above
        
        
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.scheme != null ? this.scheme.hashCode() : 0);
        hash = 23 * hash + (this.user != null ? this.user.hashCode() : 0);
        hash = 23 * hash + (this.password != null ? this.password.hashCode() : 0);
        hash = 23 * hash + (this.host != null ? this.host.hashCode() : 0);
        hash = 23 * hash + this.port;
        hash = 23 * hash + (this.headers != null ? this.headers.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return equalsUsingComparisonRules((SipUri) obj);
    }
}
