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

    private final boolean wildcard;
    
    public AddressImpl(final org.andrewwinter.sip.parser.Address address, final HeaderName hn) {
        super(address, hn);
        wildcard = false;
    }

    AddressImpl(final Uri uri, final HeaderName hn) {
        this(new org.andrewwinter.sip.parser.Address(uri), hn);
    }
    
    private org.andrewwinter.sip.parser.Address getAddress() {
        return (org.andrewwinter.sip.parser.Address) parameterable;
    }
    
    /**
     * Creates a wildcard address.
     */
    AddressImpl() {
        super(null, null);
        wildcard = true;
    }
    
    @Override
    public Object clone() {
        if (wildcard) {
            return new AddressImpl();
        } else {
            // Use null for the HeaderName because the application is creating
            // a new address.
            return new AddressImpl(getAddress().clone(), null);
        }
    }

    @Override
    public String getDisplayName() {
        return getAddress().getDisplayName();
    }

    @Override
    public void setDisplayName(String name) {
        if (HeaderName.FROM.equals(getHeaderName()) || HeaderName.TO.equals(getHeaderName())) {
            throw new IllegalStateException("Display name cannot be modified here.");
        }
        getAddress().setDisplayName(name);
    }

    @Override
    public URI getURI() {
        if (isWildcard()) {
            return null;
        } else {
            return URIImpl.create(getAddress().getUri());
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
            getAddress().setUri(((URIImpl) uri).getRfc3261Uri());
        }
    }

    org.andrewwinter.sip.parser.Address getRfc3261Address() {
        return getAddress();
    }
    
    @Override
    public boolean isWildcard() {
        return wildcard;
    }

    @Override
    public float getQ() {
        final String qAsString = parameterable.getParameter("q");
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
            parameterable.setParameter("q", null);
        } else {
            if (q >= 0 && q <= 1) {
                parameterable.setParameter("q", String.valueOf(q));
            } else {
                throw new IllegalArgumentException("Illegal Q value.");
            }
        }
    }

    @Override
    public int getExpires() {
        final String expiresAsString = parameterable.getParameter("expires");
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
        parameterable.setParameter("expires", secondsAsString);
    }

    @Override
    public String toString() {
        if (isWildcard()) {
            return "*";
        } else {
            return parameterable.toString();
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.parameterable != null ? this.parameterable.hashCode() : 0);
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
        if (this.parameterable != other.parameterable && (this.parameterable == null || !this.parameterable.equals(other.parameterable))) {
            return false;
        }
        if (this.wildcard != other.wildcard) {
            return false;
        }
        return true;
    }
}
