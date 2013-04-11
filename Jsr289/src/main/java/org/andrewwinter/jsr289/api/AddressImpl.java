package org.andrewwinter.jsr289.api;

import javax.servlet.sip.Address;
import javax.servlet.sip.URI;
import org.andrewwinter.sip.parser.HeaderName;
import org.andrewwinter.sip.parser.Uri;

/**
 *
 * @author andrewwinter77
 */
public class AddressImpl extends AbstractParameterable implements Address {

    private final org.andrewwinter.sip.parser.Address address;
    
    private final boolean wildcard;
    
    AddressImpl(final org.andrewwinter.sip.parser.Address address, final HeaderName hn) {
        super(address, hn);
        this.address = address;
        wildcard = false;
    }

    AddressImpl(final Uri uri, final HeaderName hn) {
        this(new org.andrewwinter.sip.parser.Address(uri), hn);
    }
    
    /**
     * Creates a wildcard address.
     */
    AddressImpl() {
        super(null, null);
        address = null;
        wildcard = true;
    }
    
    @Override
    public Object clone() {
        if (wildcard) {
            return new AddressImpl();
        } else {
            // Use null for the HeaderName because the application is creating
            // a new address.
            return new AddressImpl(address.clone(), null);
        }
    }

    @Override
    public String getDisplayName() {
        return address.getDisplayName();
    }

    @Override
    public void setDisplayName(String name) {
        if (HeaderName.FROM.equals(getHeaderName()) || HeaderName.TO.equals(getHeaderName())) {
            throw new IllegalStateException("Display name cannot be modified here.");
        }
        address.setDisplayName(name);
    }

    @Override
    public URI getURI() {
        if (isWildcard()) {
            return null;
        } else {
            return URIImpl.create(address.getUri());
        }
    }

    @Override
    public void setURI(final URI uri) {
        if (uri == null) {
            throw new NullPointerException("URI cannot be null.");
        } else if (isWildcard()) {
            throw new IllegalStateException("Cannot set URI on this address.");
            // TODO: Are there other scenarios where this exception should be set?
        } else if (HeaderName.FROM.equals(getHeaderName()) || HeaderName.TO.equals(getHeaderName())) {
            throw new IllegalStateException("URI cannot be modified here.");
        } else {
            address.setUri(((URIImpl) uri).getRfc3261Uri());
        }
    }

    org.andrewwinter.sip.parser.Address getRfc3261Address() {
        return address;
    }
    
    @Override
    public boolean isWildcard() {
        return wildcard;
    }

    @Override
    public float getQ() {
        final String qAsString = address.getParameter("q");
        if (qAsString == null) {
            return -1;
        } else {
            try {
                return Float.valueOf(qAsString);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
    }

    @Override
    public void setQ(float q) {
        if (q == -1) {
            address.setParameter("q", null);
        } else {
            if (q >= 0 && q <= 1) {
                address.setParameter("q", String.valueOf(q));
            } else {
                throw new IllegalArgumentException("Illegal Q value.");
            }
        }
    }

    @Override
    public int getExpires() {
        final String expiresAsString = address.getParameter("expires");
        if (expiresAsString == null) {
            return -1;
        } else {
            try {
                return Integer.parseInt(expiresAsString);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
    }

    @Override
    public void setExpires(final int seconds) {
        String secondsAsString = null;
        if (seconds >= 0) {
            secondsAsString = String.valueOf(seconds);
        }
        address.setParameter("expires", secondsAsString);
    }

    @Override
    public String toString() {
        if (isWildcard()) {
            return "*";
        } else {
            return address.toString();
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.address != null ? this.address.hashCode() : 0);
        hash = 47 * hash + (this.wildcard ? 1 : 0);
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
        final AddressImpl other = (AddressImpl) obj;
        if (this.address != other.address && (this.address == null || !this.address.equals(other.address))) {
            return false;
        }
        if (this.wildcard != other.wildcard) {
            return false;
        }
        return true;
    }
}