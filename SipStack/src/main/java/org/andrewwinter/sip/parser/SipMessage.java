package org.andrewwinter.sip.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author andrewwinter77
 */
public abstract class SipMessage implements Serializable {
    
    private boolean useLongFormHeaders;
    
    protected final Map<HeaderName, List<Serializable>> headers;
    
    private String body;
    
    /**
     *
     */
    protected SipMessage() {
        headers = new HashMap<HeaderName, List<Serializable>>();
        useLongFormHeaders = true;
    }
    
    public void setUseLongFormHeaders(final boolean useLongFormHeaders) {
        this.useLongFormHeaders = useLongFormHeaders;
    }

    /**
     * Sets a header with the given name and values. If the header had already
     * been set, the new values overwrites the previous ones. If there are
     * multiple headers with the same name, they all are replaced by this header
     * name, value pair.
     * @param name
     * @param values
     */
    public <T extends Serializable> void setHeaders(final HeaderName name, final List<T> objs) {
        if (objs == null) {
            throw new IllegalArgumentException("Attempting to set null value for header.");
        }
        headers.put(name, new ArrayList<Serializable>(objs));
    }
    

    public void setHeader(final HeaderName name, final Serializable value) {
        if (value == null) {
            throw new IllegalArgumentException("Attempting to set null value for header.");
        }
        headers.put(name, Arrays.asList(value));
    }

    /**
     * If the header type supports multiple values, the new value is inserted
     * into the message before any existing values.
     * @param name
     * @param value 
     */
    public void pushHeader(final HeaderName name, final Serializable value) {
        if (value == null) {
            throw new IllegalArgumentException("Attempting to set null value for header.");
        }
        List<Serializable> values = headers.get(name);
        if (values == null) {
            values = new ArrayList<Serializable>();
            headers.put(name, values);
        }
        values.add(0, value);
    }

    /**
     * If the header type supports multiple values, the new value is inserted
     * into the message after any existing values.
     * @param name
     * @param value 
     */
    public void addHeader(final HeaderName name, final Serializable value) {
        if (value == null) {
            throw new IllegalArgumentException("Attempting to set null value for header.");
        }
        List<Serializable> values = headers.get(name);
        if (values == null) {
            values = new ArrayList<Serializable>();
            headers.put(name, values);
        }
        values.add(value);
    }
    
    /**
     *
     * @param nameAndValue
     */
    public void addHeader(final String nameAndValue) {
        final String[] headerFieldParts = nameAndValue.split(":", 2);
        String value = "";
        if (headerFieldParts.length == 2) {
            value = headerFieldParts[1].trim();
        }
        addHeader(HeaderName.fromString(headerFieldParts[0]), value);
    }
    
    /**
     *
     * @return
     */
    public abstract String getMethod();
    
    public void setHeader(final HeaderName name, final int value) {
        setHeader(name, String.valueOf(value));
    }

    public void setHeader(final HeaderName name, final float value) {
        setHeader(name, String.valueOf(value));
    }
    
    /**
     * 
     * @param name
     * @return
     * @throws NumberFormatException if the header exists and does not
     * contain a parsable {@code int}.
     */
    Integer getInteger(final HeaderName name) throws NumberFormatException {
        final Serializable cl = getFirstOccurrenceOfHeader(name);
        if (cl == null) {
            return null;
        } else if (cl instanceof Integer) {
            return (Integer) cl;
        } else {
            return Integer.parseInt(cl.toString());
        }
    }
    
    /**
     * 
     * @param name
     * @return
     * @throws NumberFormatException if the header exists and does not
     * contain a parsable {@code float}.
     */
    Float getFloat(final HeaderName name) throws NumberFormatException {
        final Serializable cl = getFirstOccurrenceOfHeader(name);
        if (cl == null) {
            return null;
        } else if (cl instanceof Float) {
            return (Float) cl;
        } else {
            return Float.parseFloat(cl.toString());
        }
    }

    /**
     * Returns list of individual tokens that may appear comma-separated in
     * a single header field, and/or across multiple header fields of the same
     * name. For example,
     *   Require: abc,def
     *   Require: ghi
     * This will return <"abc", "def", "ghi">.
     * @param name
     * @return 
     */
    List<String> getCommaSeparatedTokens(final HeaderName name) {
        final List<Serializable> values = headers.get(name);
        if (values == null) {
            return null;
        } else {
            
            // TODO: Optimize this. Joining then splitting is a bit silly.
            
            final StringBuilder sb = new StringBuilder();
            for (final Serializable value : values) {
                sb.append(value).append(",");
            }
            
            final List<String> result = new ArrayList<String>();
            final String[] parts = sb.toString().split(",");
            for (final String part : parts) {
                result.add(part.trim());
            }
            return result;
        }
    }
    
    public List<Parameterable> getParameterables(final HeaderName name) throws ParseException {
        final List<Serializable> values = headers.get(name);
        if (values == null) {
            return null;
        } else {
            
            // TODO: Optimize this. Joining then splitting is a bit silly.
            // TODO: Also, some of these may already be Parameterable objects.

            final StringBuilder sb = new StringBuilder();
            for (final Serializable value : values) {
                sb.append(value).append(",");
            }
            
            final List<Parameterable> result = new ArrayList<Parameterable>();

            final List<String> parts = Util.safeSplit(sb.toString());
            for (final String part : parts) {
                result.add(GenericParameterable.parse(part));
            }
                    
            return result;
        }
    }
    
    /**
     * This method can be used with headers which are defined to contain one or
     * more entries matching (name-addr | addr-spec) *(SEMI generic-param) as
     * defined in RFC 3261. This includes, for example, Contact and Route.
     * @param name
     * @return A list of Address headers or the empty list if no address
     * headers matching the given name were found.
     */
    public List<Address> getAddressHeaders(final HeaderName name) {
        final List<Serializable> values = headers.get(name);
        
        if (values == null) {
            return new ArrayList<Address>();
        } else {
            
            // TODO: Some (or perhaps all) of the values elements may already be Address objects. Cater for this.
            
            // TODO: Optimize this. Joining then splitting is a bit silly.

            final StringBuilder sb = new StringBuilder();
            for (final Serializable value : values) {
                sb.append(value).append(",");
            }
            
            final List<Address> result = new ArrayList<Address>();

            final List<String> parts = Util.safeSplit(sb.toString());
            for (final String part : parts) {
                result.add(Address.parse(part));
            }
            
            setHeaders(name, result);
            
            return result;
        }
    }
    
    public List<Parameterable> getAccept() {
        return getParameterables(HeaderName.ACCEPT);
    }
    
    public List<Parameterable> getAcceptEncoding() {
        return getParameterables(HeaderName.ACCEPT_ENCODING);
    }
    
    public List<Parameterable> getAcceptLanguage() {
        return getParameterables(HeaderName.ACCEPT_LANGUAGE);
    }

    /**
     *
     * @return
     */
    public Address popRoute() {
        final List<Address> routes = getAddressHeaders(HeaderName.ROUTE);
        Address route = null;
        if (!routes.isEmpty()) {
            route = routes.remove(0);
            setHeaders(HeaderName.ROUTE, routes);
        }
        return route;
    }

    /**
     *
     * @return
     */
    public Via popVia() {
        final List<Via> vias = getVias();
        final Via result = vias.remove(0);
        if (vias.isEmpty()) {
            removeHeader(HeaderName.VIA);
        } else {
            SipMessageHelper.setVias(vias, this);
        }
        return result;
    }
    
    /**
     *
     * @return
     */
    public Via getTopmostVia() {
        final List<Serializable> values = headers.get(HeaderName.VIA);
        if (values == null) {
            return null;
        } else if (values.get(0) instanceof Via) {
            return (Via) values.get(0);
        } else {
            final List<String> parts = Util.safeSplit(values.get(0).toString());
            return Via.parse(parts.get(0));
        }
    }
    
    /**
     *
     * @return
     */
    public List<Via> getVias() {
        final List<Serializable> values = headers.get(HeaderName.VIA);
        if (values == null) {
            return null;
        } else {
            
            // TODO: Optimize this. Joining then splitting is a bit silly.

            final StringBuilder sb = new StringBuilder();
            for (final Serializable value : values) {
                sb.append(value).append(",");
            }
            
            final List<Via> result = new ArrayList<Via>();

            final List<String> parts = Util.safeSplit(sb.toString());
            for (final String part : parts) {
                result.add(Via.parse(part));
            }
                    
            return result;
        }
    }

    /**
     * 
     * @param name 
     */
    public void removeHeader(final HeaderName hn) {
        headers.remove(hn);
    }

    /**
     *
     * @return
     */
    public Timestamp getTimestamp() {
        final Serializable value = getFirstOccurrenceOfHeader(HeaderName.TIMESTAMP);
        if (value == null) {
            return null;
        } if (value instanceof Timestamp) {
            return (Timestamp) value;
        } else {
            return new Timestamp(value.toString());
        }
    }
    
//    TODO: ALERT_INFO("Alert-Info", null),
//    TODO: CALL_INFO("Call-Info", null),
//    TODO: DATE("Date", null),
    
    /**
     *
     * @return
     */
    public CSeq getCSeq() {
        final Serializable value = getFirstOccurrenceOfHeader(HeaderName.CSEQ);
        if (value == null) {
            return null;
        } if (value instanceof CSeq) {
            return (CSeq) value;
        } else {
            return new CSeq(value.toString());
        }
    }
    
    /**
     *
     * @param cseq
     */
    public void setCSeq(final CSeq cseq) {
        setHeader(HeaderName.CSEQ, cseq);
    }
    
    /**
     *
     * @return
     */
    public List<String> getRequire() {
        return getCommaSeparatedTokens(HeaderName.REQUIRE);
    }

    /**
     *
     * @return
     */
    public List<String> getAllow() {
        return getCommaSeparatedTokens(HeaderName.ALLOW);
    }

    /**
     *
     * @return
     */
    public List<String> getContentEncoding() {
        return getCommaSeparatedTokens(HeaderName.CONTENT_ENCODING);
    }

    /**
     *
     * @return
     */
    public List<String> getContentLanguage() {
        return getCommaSeparatedTokens(HeaderName.CONTENT_LANGUAGE);
    }

    /**
     *
     * @return
     */
    public List<String> getSupported() {
        return getCommaSeparatedTokens(HeaderName.SUPPORTED);
    }
    
    /**
     *
     * @return
     */
    public String getCallId() {
        return (String) getFirstOccurrenceOfHeader(HeaderName.CALL_ID);
    }
    
    /**
     *
     * @param callId
     */
    public void setCallId(final String callId) {
        setHeader(HeaderName.CALL_ID, callId);
    }
    
    /**
     *
     * @return
     */
    public String getUserAgent() {
        return (String) getFirstOccurrenceOfHeader(HeaderName.USER_AGENT);
    }

    public Address getAddress(final HeaderName hn) throws ParseException {
        final Serializable value = getFirstOccurrenceOfHeader(hn);
        if (value == null) {
            return null;
        } else if (value instanceof Address) {
            return (Address) value;
        } else {
            final List<String> parts = Util.safeSplit(value.toString());
            final Address address = Address.parse(parts.get(0));
            parts.remove(0);
            replaceFirstOccurrenceOfHeader(hn, address, parts);
            return address;
        }
    }
    
    /**
     *
     * @param body
     */
    public void setBody(final String body) {
        this.body = body;
    }
    
    /**
     *
     * @return
     */
    public String getBody() {
        return body;
    }
    
    abstract String getStartLine();
    
    public Parameterable getParameterable(final HeaderName hn) throws ParseException {
        final Serializable header = getFirstOccurrenceOfHeader(hn);
        if (header == null) {
            return null;
        } else if (header instanceof Parameterable) {
            return (Parameterable) header;
        } else {
            final List<String> parts = Util.safeSplit(header.toString());
            final Parameterable param = GenericParameterable.parse(parts.get(0));
            parts.remove(0);
            replaceFirstOccurrenceOfHeader(hn, param, parts);
            return param;
        }
    }
    
    /**
     *
     * @param name
     * @return
     */
    public Serializable getFirstOccurrenceOfHeader(final HeaderName name) {
        final List<Serializable> values = headers.get(name);
        if (values == null) {
            return null;
        } else {
            return values.get(0);
        }
    }
    
    private <T extends Serializable> void replaceFirstOccurrenceOfHeader(final HeaderName name, final Serializable value, final List<T> subsequent) {
        final List<Serializable> values = headers.get(name);
        if (values != null) {
            values.set(0, value);
            int i=1;
            for (final Serializable s : subsequent) {
                values.add(i++, s);
            }
        }
    }

    /**
     * Returns null if there is no such header with the given name.
     * @param name
     * @return 
     */
    public List<String> getHeadersAsStrings(final HeaderName name) {
        final List<Serializable> headersAsSerializable = headers.get(name);
        final List<String> headersAsStrings = new ArrayList<String>();
        if (headersAsSerializable != null) {
            for (final Serializable s : headersAsSerializable) {
                headersAsStrings.add(s.toString());
            }
        }
        return headersAsStrings;
    }

    /**
     *
     * @param name
     * @return
     */
    public List<Serializable> getHeaders(final HeaderName hn) {
        List<Serializable> result = headers.get(hn);
        if (result == null) {
            return new ArrayList<Serializable>();
        } else {
            return Collections.unmodifiableList(result);
        }
    }

    /**
     *
     * @return
     */
    public Set<HeaderName> getHeaderNames() {
        return Collections.unmodifiableSet(headers.keySet());
    }
    
    /**
     * 
     * @param str
     * @return 
     */
    public static SipMessage parse(String str) throws ParseException {
        // Separate the SIP header section from the body.
        final String parts[] = str.split(Util.CRLF + Util.CRLF);
        
        // Separate out each header field.
        final String headerSection = Util.unfoldHeaders(parts[0]);
        final String headerFields[] = headerSection.split(Util.CRLF);

        if (headerFields.length < 2) {
            throw new ParseException("Insufficient number of headers.");
        }
        
        // The first line in the header section is
        // the start-line.
        final String startLine = headerFields[0];
        final String[] startLineParts = startLine.split("\\s+",  3);

        SipMessage msg;
        if (str.startsWith("SIP/2.0 ")) {
            msg = new SipResponse(
                    Integer.parseInt(startLineParts[1]),
                    startLineParts[2]);
        } else {
            msg = new SipRequest(startLineParts[0], Uri.parse(startLineParts[1]));
        }

        for (int i=1; i<headerFields.length; ++i) {
            msg.addHeader(headerFields[i]);
        }
        
        // Set the body, only if there's a body to set.
        if (parts.length > 1) {
            // TODO: For now we'll treat this as a string
            // but this probably isn't the right thing to do.
            msg.setBody(parts[1]);
        }

        return msg;
    }
    
    /**
     * @return
     */
    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder();
        
        sb.append(getStartLine()).append(Util.CRLF);
        
        
        
        // TODO: Remove this debug when we've tracked down and fixed NPE seen in testSetHeader001
        if (headers.entrySet() == null) {
            throw new NullPointerException("entrySet is null!");
        }
        
        
        
        
        for (Map.Entry<HeaderName, List<Serializable>> entry : headers.entrySet()) {
            
            
            // TODO: Remove this debug when we've tracked down and fixed NPE seen in testSetHeader001
            if (entry.getKey() == null) {
                throw new NullPointerException("key is null for entry " + entry);
            }
            if (entry.getValue() == null) {
                throw new NullPointerException("value is null for entry " + entry);
            }
            
            
            String name;
            if (useLongFormHeaders) {
                name = entry.getKey().getLongName();
            } else {
                name = entry.getKey().getShortName();
                if (name == null) {
                    name = entry.getKey().getLongName();
                }
            }
            for (final Serializable value : entry.getValue()) {
                sb.append(name).append(": ").append(value).append(Util.CRLF);
            }
        }
        
        // Terminate header with empty line.
        sb.append(Util.CRLF);
        
        if (body != null) {
            sb.append(body);
        }
        
        return sb.toString();
    }
}
