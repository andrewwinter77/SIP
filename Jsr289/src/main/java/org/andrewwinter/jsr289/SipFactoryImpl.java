package org.andrewwinter.jsr289;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.sip.Address;
import javax.servlet.sip.AuthInfo;
import javax.servlet.sip.Parameterable;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.URI;
import org.andrewwinter.sip.message.SipMessageFactory;
import org.andrewwinter.sip.parser.GenericParameterable;
import org.andrewwinter.sip.parser.GenericUri;
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

    private final ServletContext servletContext;
    
    public SipFactoryImpl(final String appName, final String mainServletName, final ServletContext servletContext) {
        this.appName = appName;
        this.mainServletName = mainServletName;
        this.servletContext = servletContext;
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
    public SipServletRequest createRequest(SipApplicationSession appSession, String method, Address from, Address to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Invalid to or from.");
        }
        
        try {
            return createRequest(appSession, method, from.toString(), to.toString());
        } catch (ServletParseException e) {
            // TODO: Log error. This should never happen.
            throw new RuntimeException("Error while creating request.", e);
        }
    }
    
    @Override
    public SipServletRequest createRequest(SipApplicationSession appSession, String method, URI from, URI to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Invalid to or from.");
        }
        
        try {
            return createRequest(appSession, method, from.toString(), to.toString());
        } catch (ServletParseException e) {
            // TODO: Log error. This should never happen.
            throw new RuntimeException("Error while creating request.", e);
        }
    }

    /**
     * Some methods we call require these are set. Depending on where we get
     * our thread from, these variables may or may not be set already. If they
     * are not set then set them.
     */
    private void setThreadLocalVariables() {
        if (ServletContextThreadLocal.get() == null) {
            ServletContextThreadLocal.set(servletContext);
        }
        if (AppNameThreadLocal.get() == null) {
            AppNameThreadLocal.set(appName);
        }
        if (MainServletNameThreadLocal.get() == null) {
            MainServletNameThreadLocal.set(mainServletName);
        }
    }
    
    @Override
    public SipServletRequest createRequest(SipApplicationSession appSession, String method, String from, String to) throws ServletParseException {
        
        setThreadLocalVariables();
        
        if (method == null || "ACK".equals(method) || "CANCEL".equals(method)) {
            throw new IllegalArgumentException("Invalid method.");
        }
        
        if (appSession == null || !appSession.isValid()) {
            throw new IllegalArgumentException("Invalid SipApplicationSession.");
        }
        
        final SipRequest request;
        try {
            request = SipMessageFactory.createOutOfDialogRequest(
                    method,
                    to,
                    from,
                    null,
                    org.andrewwinter.sip.parser.Address.parse("sip:dummy@contact.address")); // TODO: Set a sensible contact address
        } catch (final ParseException e) {
            throw new ServletParseException("Attempting to construct a malformed message.");
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
        
        final SipServletRequestImpl result = new SipServletRequestImpl(request);
        result.setSipSession(ss);
        return result;
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
