package org.andrewwinter.jsr289.api;

import java.util.Objects;
import javax.servlet.sip.URI;
import org.andrewwinter.sip.parser.GenericUri;
import org.andrewwinter.sip.parser.Uri;

/**
 *
 * @author andrewwinter77
 */
public class GenericURIImpl extends URIImpl {

    GenericURIImpl(final GenericUri uri) {
        super(uri);
    }
    
    @Override
    public URI clone() {
        return new GenericURIImpl((GenericUri) Uri.parse(uri.toString()));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.uri);
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
        final GenericURIImpl other = (GenericURIImpl) obj;
        if (this.uri != other.uri && (this.uri == null || !this.uri.equals(other.uri))) {
            return false;
        }
        return true;
    }
}
