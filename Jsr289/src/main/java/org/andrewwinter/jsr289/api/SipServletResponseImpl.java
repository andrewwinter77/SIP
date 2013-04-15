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

    private final SipResponse response;
    
    private final SipServletRequestImpl servletRequest;
    
    protected SipServletResponseImpl(SipResponse response, SipServletRequestImpl servletRequest) {
        super(response);
        this.response = response;
        this.servletRequest = servletRequest;
    }
    
    @Override
    public SipServletRequest getRequest() {
        return servletRequest;
    }

    @Override
    public int getStatus() {
        return response.getStatusCode();
    }

    @Override
    public void setStatus(final int statusCode) {
        setStatus(statusCode, response.getReasonPhrase());
    }

    @Override
    public void setStatus(final int statusCode, final String reasonPhrase) {
        // TODO: Do we want to allow the app to set the response code 100?
        if (statusCode < 100 || statusCode > 699) {
            throw new IllegalArgumentException("Status code not allowed.");
        }
        
        response.setStatus(statusCode, reasonPhrase);
    }

    @Override
    public String getReasonPhrase() {
        return response.getReasonPhrase();
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
        List<AuthenticationChallenge> challenges = response.getWWWAuthenticate();
        if (challenges != null) {
            for (final AuthenticationChallenge challenge : challenges) {
                final String realm = challenge.getParam("realm");
                if (realm != null) {
                    result.add(realm);
                }
            }
        }
        challenges = response.getProxyAuthenticate();
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
        return response.toString();
    }
}
