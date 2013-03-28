package org.andrewwinter.jsr289;

import java.util.Iterator;
import javax.servlet.sip.URI;
import org.andrewwinter.sip.parser.GenericUri;
import org.andrewwinter.sip.parser.SipUri;
import org.andrewwinter.sip.parser.TelUrl;
import org.andrewwinter.sip.parser.Uri;

/**
 *
 * @author andrew
 */
public abstract class URIImpl implements URI {

    private final Uri uri;
    
    URIImpl(final Uri uri) {
        this.uri = uri;
    }

    static URI create(final Uri uri) {
        if (uri instanceof SipUri) {
            return new SipURIImpl((SipUri) uri);
        } else if (uri instanceof TelUrl) {
            return new TelURLImpl((TelUrl) uri);
        } else {
            return new GenericURIImpl((GenericUri) uri);
        }
    }
    
    Uri getRfc3261Uri() {
        return uri;
    }
    
    @Override
    public String getScheme() {
        return uri.getScheme();
    }

    @Override
    public boolean isSipURI() {
        return uri instanceof SipUri;
    }

    @Override
    public final String getParameter(final String key) {
        if (key == null) {
            throw new NullPointerException("Key must be non-null.");
        }
        return uri.getParameter(key);
    }

    @Override
    public final void setParameter(final String name, final String value) {
        if (name == null || value == null) {
            throw new NullPointerException("Name and value must both be non-null.");
        }
        uri.setParameter(name, value);
    }

    @Override
    public void removeParameter(final String name) {
        uri.setParameter(name, null);
    }

    @Override
    public Iterator<String> getParameterNames() {
        return uri.getParameterNames().iterator();
    }

    @Override
    public abstract URI clone();
    
    @Override
    public String toString() {
        return uri.toString();
    }

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);
}
