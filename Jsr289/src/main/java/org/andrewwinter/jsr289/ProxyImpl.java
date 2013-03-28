package org.andrewwinter.jsr289;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.sip.Proxy;
import javax.servlet.sip.ProxyBranch;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.URI;
import org.andrewwinter.sip.message.InboundSipRequest;
import org.andrewwinter.sip.parser.Uri;
import org.andrewwinter.sip.transaction.server.ServerTransactionStateName;

/**
 *
 * @author andrew
 */
public class ProxyImpl implements Proxy {

    private static final Set<String> SUPPORTED_PROXYING_SCHEMES;
    static {
        SUPPORTED_PROXYING_SCHEMES = new HashSet<>();
        SUPPORTED_PROXYING_SCHEMES.add("sip");
        SUPPORTED_PROXYING_SCHEMES.add("sips");
        SUPPORTED_PROXYING_SCHEMES.add("tel");
    }
    
    private final List<ProxyBranch> proxyBranches;
    
    private final InboundSipRequest inboundSipRequest;
    
    private transient SipServletRequest origRequest;
    
    private boolean noCancel;

    private int proxyTimeoutInSeconds;
    
    /**
     * Specifies whether the servlet engine will automatically recurse or not.
     * If recursion is enabled the servlet engine will automatically attempt to
     * proxy to contact addresses received in redirect (3xx) responses. If
     * recursion is disabled and no better response is received, a redirect
     * response will be passed to the application and will be passed upstream
     * towards the client.
     */
    private boolean recurse;
    
    /**
     * Specifies whether this branch should include a Record-Route header for
     * this servlet engine or not.
     * 
     * Record-routing is used to specify that this servlet engine must stay on
     * the signaling path of subsequent requests.
     */
    private boolean recordRoute;
    
    /**
     * Specifies whether to proxy in parallel or sequentially.
     */
    private boolean parallel;
    
    private boolean supervised;
    
    private boolean addToPath;
    
    public ProxyImpl(final InboundSipRequest isr, final SipServletRequest origRequest) {
        this.inboundSipRequest = isr;
        
        // The default proxy behavior, as per RFC 3261 section 16.7 point 10, is
        // to cancel outstanding branches upon receiving the first 2xx response.
        noCancel = false;
        
        // TODO: This could be set in the deployment descriptor. This is just an arbitrary value for now, fix!!!
        proxyTimeoutInSeconds = 30;
        
        // The default value is true.
        recurse = true;
        
        // The default value is false.
        recordRoute = false;
        
        // The default value is true.
        parallel = true;
        
        // The default value is false.
        addToPath = false;
        
        // The default value is true.
        this.supervised = true;

        this.origRequest = origRequest;

        proxyBranches = new ArrayList<>();
    }
    
    @Override
    public SipServletRequest getOriginalRequest() {
        return origRequest;
    }

    @Override
    public void proxyTo(final URI uri) {
        if (uri == null) {
            throw new NullPointerException("URI must not be null.");
        }
        
        final ServerTransactionStateName state = inboundSipRequest.getServerTransaction().getStateName();
        
        // TODO: Check state
        
        if (!SUPPORTED_PROXYING_SCHEMES.contains(uri.getScheme())) {
            throw new IllegalArgumentException("Unsupported proxying scheme.");
        }
        
        proxyBranches.add(new ProxyBranchImpl(this, uri));
        startProxy();
    }

    @Override
    public void proxyTo(final List<? extends URI> uris) {
        // TODO: Check state

        for (final URI uri : uris) {
            if (uri == null) {
                throw new NullPointerException("URIs must be non-null.");
            }
            if (!SUPPORTED_PROXYING_SCHEMES.contains(uri.getScheme())) {
                throw new IllegalArgumentException("Unsupported proxying scheme.");
            }
        }
        
        // TODO: Remove duplicate URIs. See p10-3 of the spec.
        
        for (final URI uri : uris) {
            proxyBranches.add(new ProxyBranchImpl(this, uri));
        }
        startProxy();
    }

    @Override
    public void cancel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cancel(final String[] protocol, final int[] reasonCode, final String[] reaasonText) {
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
    public boolean getParallel() {
        return parallel;
    }

    @Override
    public void setParallel(final boolean parallel) {
        this.parallel = parallel;
    }

    @Override
    public boolean getStateful() {
        // Always returns true.
        return true;
    }

    @Override
    public void setStateful(final boolean stateful) {
        // Deprecated. Do nothing.
    }

    @Override
    public boolean getSupervised() {
        return supervised;
    }

    @Override
    public void setSupervised(final boolean supervised) {
        this.supervised = supervised;
    }

    @Override
    public int getProxyTimeout() {
        return proxyTimeoutInSeconds;
    }

    @Override
    public int getSequentialSearchTimeout() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setProxyTimeout(final int seconds) {
        if (seconds < 1) {
            throw new IllegalArgumentException("Negative proxy timeout not allowed.");
        }
        
        // TODO: Other checks in here.
        
        this.proxyTimeoutInSeconds = seconds;
    }

    @Override
    public void setSequentialSearchTimeout(final int seconds) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SipURI getRecordRouteURI() {
        if (!recordRoute) {
            throw new IllegalStateException("Record-routing is not enabled.");
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // TESTIDEA: Try passing in a null URI
    @Override
    public List<ProxyBranch> createProxyBranches(final List<? extends URI> list) {
        if (list == null) {
            throw new NullPointerException("list must not be null.");
        }
        for (final URI uri : list) {
            if (!SUPPORTED_PROXYING_SCHEMES.contains(uri.getScheme())) {
                throw new IllegalArgumentException("Unsupported proxying scheme.");
            }
            proxyBranches.add(new ProxyBranchImpl(this, uri));
        }
        // TESTIDEA: Try modifying the list.
        return Collections.unmodifiableList(proxyBranches);
    }

    @Override
    public ProxyBranch getProxyBranch(final URI uri) {
        if (uri == null) {
            throw new NullPointerException("uri must not be null.");
        }
        for (final ProxyBranch branch : proxyBranches) {
            if (((ProxyBranchImpl) branch).getUri().equals(uri)) {
                return branch;
            }
        }
        return null;
    }

    @Override
    public List<ProxyBranch> getProxyBranches() {
        return Collections.unmodifiableList(proxyBranches);
    }

    @Override
    public void startProxy() {
        
        // TODO: What checks are required here?
        
        final List<Uri> targets = new ArrayList<>();
        for (final ProxyBranch branch : proxyBranches) {
            targets.add(((ProxyBranchImpl) branch).getRfc3261Uri());
        }
        
        inboundSipRequest.proxy(targets, parallel);
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
    public boolean getAddToPath() {
        return addToPath;
    }

    @Override
    public void setAddToPath(final boolean p) {
        addToPath = p;
    }

    @Override
    public SipURI getPathURI() {
        if (!addToPath) {
            throw new IllegalStateException("addToPath is not enabled.");
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setNoCancel(final boolean noCancel) {
        this.noCancel = noCancel;
    }

    @Override
    public boolean getNoCancel() {
        return noCancel;
    }
}
