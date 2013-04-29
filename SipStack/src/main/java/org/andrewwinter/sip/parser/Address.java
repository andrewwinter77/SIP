package org.andrewwinter.sip.parser;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author andrewwinter77
 */
public class Address extends Parameterable {
    
    private String displayName;
    
    private Uri uri;
    
    /**
     * Equivalent to {@code getParameter("tag").}
     * @return 
     */
    public String getTag() {
        return getParameter("tag");
    }
    
    /**
     *
     * @return
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * @param displayName {@code null} unsets the display name. Use the empty string
     * to force the address to comply with the name-addr form.
     */
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    
    /**
     *
     * @return
     */
    public Uri getUri() {
        return uri;
    }
    
    /**
     *
     * @param uri
     */
    public void setUri(final Uri uri) {
        if (uri == null) {
            throw new IllegalArgumentException("URI cannot be null.");
        } else {
            this.uri = uri;
        }
    }
    
    /**
     *
     * @param uri
     */
    public Address(final Uri uri) {
        this.uri = uri;
    }
    
    private Address() {
    }
    
    /**
     *
     * @param addr
     * @return
     * @throws ParseException  
     */
    public static Address parse(String addr) throws ParseException {
        
        final Address address = new Address();
        
        if (addr == null || addr.isEmpty()) {
            throw new IllegalArgumentException("Empty string or null.");
        }
        
        if (addr.charAt(0) == ' ') {
            throw new IllegalArgumentException("Address string must be pre-trimmed.");
        }
        
        if (addr.charAt(0) == '"') {
            // Gobble up the quoted string.
            int end = Util.findEndOfQuotedString(addr);
            address.setDisplayName(addr.substring(1, end).trim());
            addr = addr.substring(end+1).trim();
        } else {
            
            final int indexOfAngleBracket = addr.indexOf('<');
            if (indexOfAngleBracket > 0) {
            
                // Whatever is between the start of the string and <
                // is the display name.
            
                final String[] parts = addr.split("<");
                address.setDisplayName(parts[0].trim());
                addr = addr.substring(indexOfAngleBracket);
            }
        }
            
        
        if (addr.charAt(0) == '<') {
            
            final int indexOfAngleBracket = addr.indexOf('>');
            final String uriAsString = addr.substring(1, indexOfAngleBracket);
            
            address.setUri(Uri.parse(uriAsString));
            
            addr = addr.substring(indexOfAngleBracket+1).trim();
        } else {
            
            // RFC 3261 says:
            // If no "<" and ">" are present, all parameters after the
            // URI are header parameters, not URI parameters.
            
            int endOfUri = addr.indexOf(';', addr.indexOf('@'));
            if (endOfUri == -1) {
                endOfUri = addr.length();
            }
            
            address.setUri(Uri.parse(addr.substring(0, endOfUri)));
            
            addr = addr.substring(endOfUri);
        }
        
        address.parseParams(addr);
        return address;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return getValue() + toParamString();
    }

    /**
     *
     * @return
     */
    @Override
    public String getValue() {
        if (displayName != null && !displayName.isEmpty()) {
            final StringBuilder sb = new StringBuilder();
            
            // The display name can be tokens, or a quoted string, if a larger
            // character set is desired.
            if (displayName.matches("[\\w-.!%*\\ +`'~]+")) {
                sb.append(displayName);
            } else {
                sb.append("\"").append(displayName).append("\" ");
            }
            
            sb.append(" <").append(uri).append(">");
            return sb.toString();
        } else {
            
            // Even if the "display-name" is empty, the "name-addr" form MUST be
            // used if the "addr-spec" contains a comma, question mark, or
            // semicolon.
            
            final String uriString = uri.toString();
            if (displayName != null || uriString.indexOf(',') != -1 || uriString.indexOf('?') != -1 || uriString.indexOf(';') != -1) {
                final StringBuilder sb = new StringBuilder();
                sb.append("<").append(uriString).append(">");
                return sb.toString();
            } else {
                // No display name and no special characters in the URI.
                return uriString;
            }
            
        }
    }

    @Override
    public void setValue(String addr) throws IllegalStateException, ParseException {
        final Address address = Address.parse(addr);
        setDisplayName(address.getDisplayName());
        this.uri = address.getUri();
    }
    
    @Override
    public Address clone() {
        return Address.parse(toString());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Address other = (Address) obj;
        if (this.uri != other.uri && (this.uri == null || !this.uri.equals(other.uri))) {
            return false;
        }
        
        
        
        //  Any uri-parameter appearing in both URIs must match.
        
        final Set<String> intersectionOfParams = new HashSet<String>();
        intersectionOfParams.addAll(getParameterNames());
        intersectionOfParams.retainAll(other.getParameterNames());
        for (final String param : intersectionOfParams) {
            if (!getParameter(param).equalsIgnoreCase(other.getParameter(param))) {
                return false;
            }
        }

        
        
        // All other uri-parameters appearing in only one URI are
        // ignored when comparing the URIs.        
        
        // TODO: Finish Address comparison - do the above
        
        
        return true;
    }
}
