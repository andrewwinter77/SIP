package org.andrewwinter.jsr289.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.sip.Proxy;
import javax.servlet.sip.ProxyBranch;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import org.andrewwinter.sip.parser.AuthenticationChallenge;
import org.andrewwinter.sip.parser.SipResponse;

/**
 *
 * @author andrew
 */
public abstract class SipServletResponseImpl extends SipServletMessageImpl implements SipServletResponse {

    protected final SipServletRequestImpl servletRequest;
    
    protected SipServletResponseImpl(SipResponse response, SipServletRequestImpl servletRequest) {
        super(response);
        this.servletRequest = servletRequest;
    }
    
    @Override
    public SipServletRequest getRequest() {
        return servletRequest;
    }

    public final SipResponse getSipResponse() {
        return (SipResponse) message;
    }
    
    @Override
    public int getStatus() {
        return getSipResponse().getStatusCode();
    }

    @Override
    public void setStatus(final int statusCode) {
        setStatus(statusCode, getSipResponse().getReasonPhrase());
    }

    @Override
    public void setStatus(final int statusCode, final String reasonPhrase) {
        // TODO: Do we want to allow the app to set the response code 100?
        if (statusCode < 100 || statusCode > 699) {
            throw new IllegalArgumentException("Status code not allowed.");
        }
        
        getSipResponse().setStatus(statusCode, reasonPhrase);
    }

    @Override
    public String getReasonPhrase() {
        return getSipResponse().getReasonPhrase();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        // Always returns null.
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        // Always returns null.
        return null;
    }

    @Override
    public abstract Proxy getProxy();

    @Override
    public abstract ProxyBranch getProxyBranch();

    @Override
    public Iterator<String> getChallengeRealms() {
        final List<String> result = new ArrayList<>();
        List<AuthenticationChallenge> challenges = getSipResponse().getWWWAuthenticate();
        if (challenges != null) {
            for (final AuthenticationChallenge challenge : challenges) {
                final String realm = challenge.getParam("realm");
                if (realm != null) {
                    result.add(realm);
                }
            }
        }
        challenges = getSipResponse().getProxyAuthenticate();
        if (challenges != null) {
            for (final AuthenticationChallenge challenge : challenges) {
                final String realm = challenge.getParam("realm");
                if (realm != null) {
                    result.add(realm);
                }
            }
        }
        return result.iterator();
    }

    @Override
    public abstract boolean isBranchResponse();

    @Override
    public void setBufferSize(final int size) {
        // Therefore, it is not expected that these methods will yield any
        // useful result and implementations may simply do nothing.
    }

    @Override
    public int getBufferSize() {
        // It is recommended that getBufferSize return 0.
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {
        // Therefore, it is not expected that these methods will yield any
        // useful result and implementations may simply do nothing.
    }

    @Override
    public void resetBuffer() {
        // Therefore, it is not expected that these methods will yield any
        // useful result and implementations may simply do nothing.
    }

    @Override
    public void reset() {
        // Therefore, it is not expected that these methods will yield any
        // useful result and implementations may simply do nothing.
    }

    @Override
    public void setLocale(Locale locale) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Locale getLocale() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String toString() {
        return getSipResponse().toString();
    }
}
