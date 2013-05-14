package org.andrewwinter.jsr289.api;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.sip.Address;
import javax.servlet.sip.Parameterable;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipServletMessage;
import javax.servlet.sip.SipSession;
import org.andrewwinter.jsr289.threadlocal.AppNameThreadLocal;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.parser.HeaderName;
import org.andrewwinter.sip.parser.ParseException;
import org.andrewwinter.sip.parser.SipMessage;
import org.andrewwinter.sip.parser.SipMessageHelper;
import org.andrewwinter.sip.parser.Util;

/**
 *
 * @author andrew
 */
public abstract class SipServletMessageImpl implements SipServletMessage {

    private HeaderForm headerForm = HeaderForm.DEFAULT;
    protected final SipMessage message;
    private final Map<String, Object> attributes = new HashMap<>();
    private SipSessionImpl sipSession;
    protected final Object sendLock = new Object();
    private boolean sent;
    private ServletContext servletContext;

    protected SipServletMessageImpl(final SipMessage message) {
        this.message = message;
    }
    
    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    
    public ServletContext getServletContext() {
        return servletContext;
    }
    
    protected boolean isSent() {
        return sent;
    }
    
    protected void flagMessageAsSent() {
        sent = true;
    }

    public void setSipSession(final SipSession sipSession) {
        this.sipSession = (SipSessionImpl) sipSession;
    }

    @Override
    public Address getFrom() {
        return new AddressImpl(SipMessageHelper.getFrom(message), HeaderName.FROM);
    }

    @Override
    public Address getTo() {
        return new AddressImpl(SipMessageHelper.getTo(message), HeaderName.TO);
    }

    @Override
    public String getMethod() {
        return message.getMethod();
    }

    @Override
    public String getProtocol() {
        // For this version of the SIP Servlet API this is always "SIP/2.0".
        return "SIP/2.0";
    }

    @Override
    public String getHeader(final String name) {
        if (name == null) {
            throw new NullPointerException("Name is null.");
        }
        final Serializable value = message.getFirstOccurrenceOfHeader(HeaderName.fromString(name));
        if (value == null) {
            return null;
        } else {
            return value.toString();
        }
    }

    @Override
    public ListIterator<String> getHeaders(final String name) {
        if (name == null) {
            throw new NullPointerException("Name is null.");
        }
        final HeaderName hn = HeaderName.fromString(name);
        return Collections.unmodifiableList(message.getHeadersAsStrings(hn)).listIterator();
    }

    @Override
    public Iterator<String> getHeaderNames() {
        final List<String> headerNames = new ArrayList<>();
        for (final HeaderName hn : message.getHeaderNames()) {
            headerNames.add(hn.getLongName());
        }
        return headerNames.iterator();
    }

    @Override
    public void setHeader(final String name, final String value) {
        if (name == null || value == null) {
            throw new NullPointerException("Name and value must both be non-null.");
        }

        final HeaderName hn = HeaderName.fromString(name);
        if (Util.isSystemHeader(hn, message, false)) {
            throw new IllegalArgumentException(name + " is a system header.");
        }

        message.setHeader(hn, value);
    }

    @Override
    public void addHeader(final String name, final String value) {
        if (name == null || value == null) {
            throw new NullPointerException("Name and value must both be non-null.");
        }

        final HeaderName hn = HeaderName.fromString(name);
        if (Util.isSystemHeader(hn, message, false)) {
            throw new IllegalArgumentException(name + " is a system header.");
        }

        message.addHeader(hn, value);
    }

    @Override
    public void removeHeader(final String name) {
        if (name == null) {
            throw new NullPointerException("Name must be non-null.");
        }

        final HeaderName hn = HeaderName.fromString(name);
        if (Util.isSystemHeader(hn, message, false)) {
            throw new IllegalArgumentException(name + " is a system header.");
        }

        message.removeHeader(hn);
    }

    @Override
    public Address getAddressHeader(final String name) throws ServletParseException {
        if (name == null) {
            throw new NullPointerException("Name must be non-null.");
        }
        final HeaderName hn = HeaderName.fromString(name);
        try {
            final org.andrewwinter.sip.parser.Address address = message.getAddress(hn);
            if (address == null) {
                return null;
            } else {
                return new AddressImpl(address, hn);
            }
        } catch (ParseException e) {
            throw new ServletParseException("Value of header '" + name + "' could not be parsed as an address.");
        }
    }

    @Override
    public ListIterator<Address> getAddressHeaders(final String name) throws ServletParseException {
        if (name == null) {
            throw new NullPointerException("Name must not be null.");
        }
        final HeaderName hn = HeaderName.fromString(name);
        final List<Address> result = new ArrayList<>();
        try {
            final List<org.andrewwinter.sip.parser.Address> addresses = message.getAddressHeaders(hn);
            if (addresses != null) {
                for (final org.andrewwinter.sip.parser.Address address : addresses) {
                    result.add(new AddressImpl(address, hn));
                }
            }
        } catch (ParseException e) {
            throw new ServletParseException("Value of header '" + name + "' could not be parsed as an address.");
        }
        return result.listIterator();
    }

    @Override
    public void setAddressHeader(final String name, final Address addr) {
        final HeaderName hn = HeaderName.fromString(name);
        if (Util.isSystemHeader(hn, message, false)) {
            throw new IllegalArgumentException("Header must not be a system header.");
        }
        if (!Util.headerCanBeAddress(hn)) {
            throw new IllegalArgumentException("Header is not an address header.");
        }
        message.setHeader(hn, ((AddressImpl) addr).getRfc3261Address());
    }

    @Override
    public void addAddressHeader(final String name, final Address addr, final boolean first) {
        final HeaderName hn = HeaderName.fromString(name);
        if (Util.isSystemHeader(hn, message, false)) {
            throw new IllegalArgumentException("Header must not be a system header.");
        }
        if (!Util.headerCanBeAddress(hn)) {
            throw new IllegalArgumentException("Header is not an address header.");
        }
        if (first) {
            message.pushHeader(hn, ((AddressImpl) addr).getRfc3261Address());
        } else {
            message.addHeader(hn, ((AddressImpl) addr).getRfc3261Address());
        }
    }

    private static Parameterable rfc3261ParameterableToServletParameterable(
            final org.andrewwinter.sip.parser.Parameterable p,
            final HeaderName hn) {
        if (p instanceof org.andrewwinter.sip.parser.Address) {
            return new AddressImpl((org.andrewwinter.sip.parser.Address) p, hn);
        } else {
            return new ParameterableImpl(p, hn);
        }
    }

    @Override
    public Parameterable getParameterableHeader(final String name) throws ServletParseException {
        if (name == null) {
            throw new NullPointerException("Name cannot be null.");
        }

        final HeaderName hn = HeaderName.fromString(name);
        if (!hn.getType().isParameterable()) {
            throw new ServletParseException("Header is not defined to hold Parameterable.");
        }

        try {
            final org.andrewwinter.sip.parser.Parameterable p = message.getParameterable(hn);
            if (p == null) {
                return null;
            } else {
                return rfc3261ParameterableToServletParameterable(p, hn);
            }
        } catch (ParseException e) {
            throw new ServletParseException("Value could not be parsed as Parameterable.");
        }
    }

    @Override
    public ListIterator<? extends Parameterable> getParameterableHeaders(final String name) throws ServletParseException {
        if (name == null) {
            throw new NullPointerException("Name cannot be null.");
        }

        final HeaderName hn = HeaderName.fromString(name);
        if (!hn.getType().isParameterable()) {
            throw new ServletParseException("Header is not defined to hold Parameterable.");
        }

        final List<Parameterable> result = new ArrayList<>();
        try {
            final List<org.andrewwinter.sip.parser.Parameterable> params = message.getParameterables(hn);
            if (params != null) {
                for (final org.andrewwinter.sip.parser.Parameterable param : params) {
                    result.add(rfc3261ParameterableToServletParameterable(param, hn));
                }
            }
        } catch (ParseException e) {
            throw new ServletParseException("Value could not be parsed as Parameterable.");
        }

        return result.listIterator();
    }

    @Override
    public void setParameterableHeader(final String name, final Parameterable param) {
        final HeaderName hn = HeaderName.fromString(name);
        if (Util.isSystemHeader(hn, message, false)) {
            throw new IllegalArgumentException("Header must not be a system header.");
        }
        if (!hn.getType().isParameterable()) {
            throw new IllegalArgumentException("Header is not defined to hold Parameterable.");
        }

        message.setHeader(hn, ((AbstractParameterable) param).getRfc3261Parameterable());
    }

    @Override
    public void addParameterableHeader(final String name, final Parameterable param, final boolean first) {
        final HeaderName hn = HeaderName.fromString(name);
        if (Util.isSystemHeader(hn, message, false)) {
            throw new IllegalArgumentException("Header must not be a system header.");
        }
        if (!hn.getType().isParameterable()) {
            throw new IllegalArgumentException("Header is not defined to hold Parameterable.");
        }

        if (first) {
            message.pushHeader(hn, ((AbstractParameterable) param).getRfc3261Parameterable());
        } else {
            message.addHeader(hn, ((AbstractParameterable) param).getRfc3261Parameterable());
        }
    }

    @Override
    public String getCallId() {
        return message.getCallId();
    }

    @Override
    public int getExpires() {
        try {
            Integer expires = SipMessageHelper.getExpires(message);
            if (expires == null) {
                return -1;
            } else {
                return expires;
            }
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public void setExpires(final int seconds) {
        SipMessageHelper.setExpires(seconds, message);
    }

    @Override
    public String getCharacterEncoding() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCharacterEncoding(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getContentLength() {
        try {
            return SipMessageHelper.getContentLength(message);
        } catch (final NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public String getContentType() {
        // TODO: Use message.getHeaderAsString() to avoid unnecessarily instantiating a Parameterable then doing a toString on it
        final org.andrewwinter.sip.parser.Parameterable contentType = SipMessageHelper.getContentType(message);
        if (contentType == null) {
            return null;
        } else {
            return contentType.toString();
        }
    }

    @Override
    public byte[] getRawContent() throws IOException {
        // TODO: This whole method body is temporary. Replace with something sensible when we stop representing content objects as Strings.
        if (message.getBody() == null || message.getBody().isEmpty()) {
            return null;
        } else {
            return message.getBody().getBytes(); 
        }
    }

    @Override
    public Object getContent() throws IOException, UnsupportedEncodingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setContent(final Object content, final String contentType) throws UnsupportedEncodingException {
        if (sent) {
            throw new IllegalStateException("Message has already been sent.");
        }
        
        // TODO: TEMPORARY
        final String body = new String((byte[]) content);
        message.setBody(body);
        // END TEMPORARY
        
        SipMessageHelper.setContentType(contentType, message);
        
        // Applications are discouraged from setting the Content-Length directly
        // using this method; they should instead use the setContent methods
        // which guarantees that the Content-Length is computed and set
        // correctly.
        
        SipMessageHelper.setContentLength(body.length(), message); // body.length() is temporary
    }

    @Override
    public void setContentType(final String type) {
        SipMessageHelper.setContentType(type, message);
    }

    @Override
    public Object getAttribute(final String name) {
        if (name == null) {
            throw new NullPointerException("Name is null.");
        }
        return attributes.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(attributes.keySet());
    }

    @Override
    public void setAttribute(final String name, final Object o) {
        if (name == null || o == null) {
            throw new NullPointerException("Name or o is null.");
        }
        attributes.put(name, o);
    }

    @Override
    public void removeAttribute(final String name) {
        if (name == null) {
            throw new NullPointerException("Name is null.");
        }
        attributes.remove(name);
    }

    @Override
    public SipSession getSession() {
        return getSession(true);
    }

    public abstract Dialog getDialog();
    
    @Override
    public SipSession getSession(final boolean create) {
        if (create && sipSession == null) {

            final Dialog dialog = getDialog();
            if (dialog == null) {
                sipSession = SipSessionImpl.createForInitialOutboundRequests(getCallId(), getApplicationSession());
            } else { 
//                sipSession = SipSessionStore.getInstance().getUsingDialogId(dialog.getId());

                if (sipSession == null) {
                    sipSession = SipSessionImpl.createForInboundInitialRequests(
                            getCallId(),
                            dialog);
//                    sipSession.setDialog(dialog);
                }
            }
        }

        return sipSession;
    }

    @Override
    public SipApplicationSession getApplicationSession() {
        return getApplicationSession(true);
    }

    @Override
    public SipApplicationSession getApplicationSession(final boolean create) {
        if (sipSession == null) {
            // TODO: Rethink this bit. Surely we should look the app session up. Where do we get its ID from?
            if (create) {
                return SipApplicationSessionImpl.create(AppNameThreadLocal.get());
            } else {
                return null;
            }
        } else {
            return sipSession.getApplicationSession(create);
        }
    }

    @Override
    public Locale getAcceptLanguage() {
        final List<org.andrewwinter.sip.parser.Parameterable> languages = message.getAcceptLanguage();
        if (languages == null) {
            return null;
        } else {
            // TODO: Sort on q value
            final String language = languages.get(0).getValue();
            return Locale.forLanguageTag(language);
        }
    }

    @Override
    public Iterator<Locale> getAcceptLanguages() {
        final List<Locale> result = new ArrayList<>();
        final List<org.andrewwinter.sip.parser.Parameterable> languages = message.getAcceptLanguage();
        if (languages != null) {

            // TODO: sort based on q param
            for (final org.andrewwinter.sip.parser.Parameterable language : languages) {
                final Locale locale = Locale.forLanguageTag(language.getValue());
                if (locale != null) {
                    result.add(locale);
                }
            }
        }
        return result.iterator();
    }

    @Override
    public void setAcceptLanguage(final Locale locale) {
        if (locale == null) {
            message.removeHeader(HeaderName.ACCEPT_LANGUAGE);
        } else {
            if (!locale.getLanguage().isEmpty()) {
                message.setHeader(HeaderName.ACCEPT_LANGUAGE, locale.getLanguage());
            }
        }
    }

    @Override
    public void addAcceptLanguage(final Locale locale) {
        if (locale == null) {
            throw new NullPointerException("Locale must not be null.");
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setContentLanguage(final Locale locale) {
        if (locale == null) {
            throw new NullPointerException("Locale must not be null.");
        }
        if (!locale.getLanguage().isEmpty()) {
            message.setHeader(HeaderName.CONTENT_LANGUAGE, locale.getLanguage());
        }
    }

    @Override
    public Locale getContentLanguage() {
        final List<String> languages = message.getContentLanguage();
        if (languages == null || languages.isEmpty()) {
            return null;
        } else {
            return Locale.forLanguageTag(languages.get(0));
        }
    }

    @Override
    public boolean isSecure() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isCommitted() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRemoteUser() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isUserInRole(final String role) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Principal getUserPrincipal() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setHeaderForm(final HeaderForm hf) {
        if (hf == null) {
            throw new NullPointerException("Header form must not be null.");
        }
        if (hf == HeaderForm.COMPACT) {
            message.setUseLongFormHeaders(false);
        } else if (hf == HeaderForm.LONG) {
            message.setUseLongFormHeaders(true);
        } else {
            // TODO: Consider supporting default, but this will require significant change to SipStack.
        }
        this.headerForm = hf;
    }

    @Override
    public HeaderForm getHeaderForm() {
        return headerForm;
    }
}
