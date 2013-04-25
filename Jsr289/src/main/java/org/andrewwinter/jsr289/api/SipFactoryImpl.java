package org.andrewwinter.jsr289.api;

import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.sip.Address;
import javax.servlet.sip.AuthInfo;
import javax.servlet.sip.Parameterable;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.URI;
import javax.servlet.sip.ar.SipApplicationRoutingDirective;
import org.andrewwinter.jsr289.ApplicationPath;
import org.andrewwinter.jsr289.store.ApplicationPathStore;
import org.andrewwinter.jsr289.threadlocal.AppNameThreadLocal;
import org.andrewwinter.jsr289.store.ApplicationSessionStore;
import org.andrewwinter.jsr289.threadlocal.MainServletNameThreadLocal;
import org.andrewwinter.jsr289.threadlocal.ServletContextThreadLocal;
import org.andrewwinter.jsr289.threadlocal.ServletNameThreadLocal;
import org.andrewwinter.sip.message.SipMessageFactory;
import org.andrewwinter.sip.parser.GenericParameterable;
import org.andrewwinter.sip.parser.GenericUri;
import org.andrewwinter.sip.parser.HeaderName;
import org.andrewwinter.sip.parser.ParseException;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipUri;
import org.andrewwinter.sip.parser.TelUrl;
import org.andrewwinter.sip.parser.Uri;

/**
 *
 * @author andrewwinter77
 */
public class SipFactoryImpl implements SipFactory {

    private final String appName;
    
    private final String mainServletName;

    private final ServletContextProvider servletContextProvider;
    
    public SipFactoryImpl(final String appName, final String mainServletName, final ServletContextProvider servletContext) {
        this.appName = appName;
        this.mainServletName = mainServletName;
        this.servletContextProvider = servletContext;
    }
    
    @Override
    public URI createURI(String uri) throws ServletParseException {
        
        final Uri parsedUri;
        try {
            parsedUri = Uri.parse(uri);
        } catch (ParseException e) {
            throw new ServletParseException("Malformed URI");
        }
        
        if (parsedUri instanceof SipUri) {
            return new SipURIImpl((SipUri) parsedUri);
        } else if (parsedUri instanceof TelUrl) {
            return new TelURLImpl((TelUrl) parsedUri);
        } else {
            return new GenericURIImpl((GenericUri) parsedUri);
        }
    }

    @Override
    public SipURI createSipURI(final String user, final String host) {
        final SipUri uri = new SipUri(host);
        uri.setUser(user);
        return new SipURIImpl(uri);
    }

    @Override
    public Address createAddress(String addr) throws ServletParseException {
        
        if ("*".equals(addr)) {
            // The no-arg constructor generates the wildcard address
            return new AddressImpl();
        } else {
            final org.andrewwinter.sip.parser.Address parsedAddress;
            try {
                parsedAddress = org.andrewwinter.sip.parser.Address.parse(addr);
            } catch (ParseException e) {
                throw new ServletParseException("Malformed address");
            }

            return new AddressImpl(parsedAddress, null);
        }
    }

    @Override
    public Address createAddress(final URI uri) {
        return new AddressImpl(((URIImpl) uri).getRfc3261Uri(), null);
    }

    @Override
    public Address createAddress(final URI uri, final String displayName) {
        final Address address = createAddress(uri);
        address.setDisplayName(displayName);
        return address;
    }

    @Override
    public Parameterable createParameterable(final String s) throws ServletParseException {
        try {
            final GenericParameterable parameterable = GenericParameterable.parse(s);
            return new ParameterableImpl(parameterable, null);
        } catch (ParseException e) {
            throw new ServletParseException("Error parsing parameterable.");
        }
    }

    @Override
    public SipServletRequest createRequest(final SipApplicationSession appSession, final String method, final Address from, final Address to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Invalid to or from.");
        }

        setThreadLocalVariables();
        
        if (method == null || "ACK".equals(method) || "CANCEL".equals(method)) {
            throw new IllegalArgumentException("Invalid method.");
        }
        
        if (appSession == null || !appSession.isValid()) {
            throw new IllegalArgumentException("Invalid SipApplicationSession.");
        }
        
        final SipRequest request;
        try {
            final List<SipURI> ifaces = (List<SipURI>) servletContextProvider.getServletContext().getAttribute(SipServlet.OUTBOUND_INTERFACES);
            final SipURIImpl contactAsSipURI = (SipURIImpl) ifaces.get(0);
            final org.andrewwinter.sip.parser.Address contactHeader =
                    new org.andrewwinter.sip.parser.Address(contactAsSipURI.getRfc3261Uri());
            
            request = SipMessageFactory.createOutOfDialogRequest(
                    method,
                    to.toString(),
                    from.toString(),
                    null,
                    contactHeader);
        } catch (final ParseException e) {
            // This should never happen.
            throw new IllegalArgumentException("Attempting to construct a malformed message.");
        }
        
        final SipSessionImpl ss = SipSessionImpl.createForInitialOutboundRequests(
                request.getCallId(), appSession);
        
        if (ServletNameThreadLocal.get() != null) {
            try {
                ss.setHandler(ServletNameThreadLocal.get());
            } catch (final ServletException e) {
                e.printStackTrace();
            }
        }
        
        final OutboundSipServletRequestImpl result = new OutboundSipServletRequestImpl(
                request,
        
                // Table 15-1 in Sip Servlet 1.1 shows we must use NEW here
                SipApplicationRoutingDirective.NEW);
        
        final ApplicationPath path = new ApplicationPath();
        ApplicationPathStore.getInstance().put(path);
        result.addHeader(HeaderName.P_APPLICATION_PATH.toString(), path.getId());
        path.add(result);
        
        result.setServletContext(servletContextProvider.getServletContext());
        result.setSipSession(ss);
        return result;
    }
    
    @Override
    public SipServletRequest createRequest(final SipApplicationSession appSession, final String method, final URI from, final URI to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Invalid to or from.");
        }
        
        return createRequest(
                appSession,
                method,
                createAddress(from),
                createAddress(to));
    }

    @Override
    public SipServletRequest createRequest(final SipApplicationSession appSession, final String method, final String from, final String to) throws ServletParseException {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Invalid to or from.");
        }

        return createRequest(
                appSession,
                method,
                createAddress(from),
                createAddress(to));
    }
    
    /**
     * Some methods we call require these are set. Depending on where we get
     * our thread from, these variables may or may not be set already. If they
     * are not set then set them.
     */
    private void setThreadLocalVariables() {
        if (ServletContextThreadLocal.get() == null) {
            ServletContextThreadLocal.set(servletContextProvider.getServletContext());
        }
        if (AppNameThreadLocal.get() == null) {
            AppNameThreadLocal.set(appName);
        }
        if (MainServletNameThreadLocal.get() == null) {
            MainServletNameThreadLocal.set(mainServletName);
        }
    }
    
    @Override
    public SipServletRequest createRequest(final SipServletRequest origRequest, final boolean sameCallId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SipApplicationSession createApplicationSession() {
        return SipApplicationSessionImpl.create(AppNameThreadLocal.get());
    }

    @Override
    // TESTIDEA: Pass in null sipApplicationKey
    public SipApplicationSession createApplicationSessionByKey(final String sipApplicationKey) {
        final SipApplicationSession session = createApplicationSession();
        ApplicationSessionStore.getInstance().putUsingKey(sipApplicationKey, session);
        return session;
    }

    @Override
    public AuthInfo createAuthInfo() {
        return new AuthInfoImpl();
    }
    
}
