package org.andrewwinter.jsr289.api;

import java.util.Iterator;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.URI;
import org.andrewwinter.sip.parser.SipUri;
import org.andrewwinter.sip.parser.Uri;

/**
 *
 * @author andrewwinter77
 */
public class SipURIImpl extends URIImpl implements SipURI {

    SipURIImpl(final SipUri uri) {
        super(uri);
    }
    
    private SipUri getSipUri() {
        return (SipUri) uri;
    }
    
    @Override
    public String getUser() {
        return getSipUri().getUser();
    }

    @Override
    public void setUser(final String string) {
        getSipUri().setUser(string);
    }

    @Override
    public String getUserPassword() {
        return getSipUri().getPassword();
    }

    @Override
    public void setUserPassword(final String password) {
        getSipUri().setPassword(password);
    }

    @Override
    public String getHost() {
        return getSipUri().getHost();
    }

    @Override
    public void setHost(final String host) {
        getSipUri().setHost(host);
    }

    @Override
    public int getPort() {
        return getSipUri().getPort();
    }

    @Override
    public void setPort(final int port) {
        getSipUri().setPort(port);
    }

    @Override
    public boolean isSecure() {
        return "sips".equals(getSipUri().getScheme());
    }

    @Override
    public void setSecure(final boolean b) {
        String scheme = "sip";
        if (b) {
            scheme = "sips";
        }
        getSipUri().setScheme(scheme);
    }

    @Override
    public String getTransportParam() {
        return uri.getParameter("transport");
    }

    @Override
    public void setTransportParam(final String transport) {
        uri.setParameter("transport", transport);
    }

    @Override
    public String getMAddrParam() {
        return uri.getParameter("maddr");
    }

    @Override
    public void setMAddrParam(final String maddr) {
        uri.setParameter("maddr", maddr);
    }

    @Override
    public String getMethodParam() {
        return uri.getParameter("method");
    }

    @Override
    public void setMethodParam(final String method) {
        uri.setParameter("method", method);
    }

    @Override
    public int getTTLParam() {
        String ttlAsString = uri.getParameter("ttl");
        if (ttlAsString == null) {
            return -1;
        } else {
            try {
                return Integer.valueOf(ttlAsString);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
    }

    @Override
    public void setTTLParam(int i) {
        uri.setParameter("ttl", String.valueOf(i));
    }

    @Override
    public String getUserParam() {
        return uri.getParameter("user");
    }

    @Override
    public void setUserParam(final String user) {
        uri.setParameter("user", user);
    }

    @Override
    public boolean getLrParam() {
        return uri.getParameter("lr") != null;
    }

    @Override
    public void setLrParam(final boolean flag) {
        String value = null;
        if (flag) {
            value = "";
        }
        uri.setParameter("lr", value);
    }

    @Override
    public String getHeader(final String name) {
        if (name == null) {
            throw new NullPointerException("Name is null.");
        }
        return getSipUri().getHeader(name);
    }

    @Override
    public void setHeader(final String name, final String value) {
        if (name == null) {
            throw new NullPointerException("Name is null.");
        }
        getSipUri().setHeader(name, value);
    }

    @Override
    public void removeHeader(final String name) {
        if (name == null) {
            throw new NullPointerException("Name is null.");
        }
        getSipUri().removeHeader(name);
    }

    @Override
    public Iterator<String> getHeaderNames() {
        return getSipUri().getHeaderNames().iterator();
    }

    @Override
    public URI clone() {
        return new SipURIImpl((SipUri) Uri.parse(uri.toString()));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.uri != null ? this.uri.hashCode() : 0);
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
        final SipURIImpl other = (SipURIImpl) obj;
        if (this.uri != other.uri && (this.uri == null || !this.uri.equals(other.uri))) {
            return false;
        }
        return true;
    }
}
