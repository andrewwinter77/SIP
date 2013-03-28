package org.andrewwinter.jsr289;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import javax.servlet.sip.Proxy;
import javax.servlet.sip.ProxyBranch;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.URI;
import org.andrewwinter.sip.parser.Uri;

/**
 *
 * @author andrew
 */
public class ProxyBranchImpl implements ProxyBranch {

    private final Proxy proxy;
    
    private final URIImpl uri;
    
    private int proxyBranchTimeoutInSeconds;
    
    private boolean recurse;
    
    private boolean recordRoute;
    
    private boolean addToPath;
    
    ProxyBranchImpl(final Proxy proxy, final URI uri) {
        this.proxy = proxy;
        this.uri = (URIImpl) uri;
        proxyBranchTimeoutInSeconds = proxy.getProxyTimeout();
        
        // Inherit values from Proxy.
        // TODO: Pass these values in to this constructor, it feels nicer
        this.recordRoute = proxy.getRecordRoute();
        this.recurse = proxy.getRecurse();
        this.addToPath = proxy.getAddToPath();
    }
    
    URIImpl getUri() {
        return uri;
    }
    
    Uri getRfc3261Uri() {
        return uri.getRfc3261Uri();
    }
    
    @Override
    public SipServletRequest getRequest() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cancel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cancel(final String[] protocol, final int[] reasonCode, final String[] reasonText) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getProxyBranchTimeout() {
        return proxyBranchTimeoutInSeconds;
    }

    @Override
    public void setProxyBranchTimeout(final int seconds) {
        if (seconds < 1) {
            throw new IllegalArgumentException("Negative proxy branch timeout not allowed.");
        }
        
        // TODO: Other checks for illegal values in here
        
        this.proxyBranchTimeoutInSeconds = seconds;
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean getRecurse() {
        return recurse;
    }

    @Override
    public void setRecurse(final boolean recurse) {
        this.recurse = recurse;
    }

    @Override
    public boolean getRecordRoute() {
        return recordRoute;
    }

    @Override
    public void setRecordRoute(final boolean rr) {
        this.recordRoute = rr;
    }

    @Override
    public boolean getAddToPath() {
        return addToPath;
    }

    @Override
    public void setAddToPath(final boolean p) {
        addToPath = p;
    }

    @Override
    public SipURI getRecordRouteURI() {
        if (!recordRoute) {
            throw new IllegalStateException("Record-routing is not enabled.");
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SipURI getPathURI() {
        if (!addToPath) {
            throw new IllegalStateException("addToPath is not enabled.");
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setOutboundInterface(final InetSocketAddress address) {
        if (address == null) {
            throw new NullPointerException("Address must not be null.");
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setOutboundInterface(final InetAddress address) {
        if (address == null) {
            throw new NullPointerException("Address must not be null.");
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SipServletResponse getResponse() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isStarted() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<ProxyBranch> getRecursedProxyBranches() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Proxy getProxy() {
        return proxy;
    }
}
