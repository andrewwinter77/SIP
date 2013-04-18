package org.andrewwinter.jsr289.api;

import javax.servlet.sip.TelURL;
import javax.servlet.sip.URI;
import org.andrewwinter.sip.parser.ParseException;
import org.andrewwinter.sip.parser.TelUrl;
import org.andrewwinter.sip.parser.Uri;

/**
 *
 * @author andrewwinter77
 */
public class TelURLImpl extends URIImpl implements TelURL {

    TelURLImpl(final TelUrl url) {
        super(url);
    }
    
    private TelUrl getTelUrl() {
        return (TelUrl) uri;
    }
    
    @Override
    public String getPhoneNumber() {
        String number = getTelUrl().getNumber();
        if (number.startsWith("+")) {
            return number.substring(1);
        } else {
            return number;
        }
    }

    @Override
    public String getPhoneContext() {
        return uri.getParameter("phone-context");
    }

    @Override
    public void setPhoneNumber(final String number) {
        try {
            getTelUrl().setGlobalNumber(number);
        } catch (final ParseException e) {
            throw new IllegalStateException("Invalid global number.");
        }
    }

    @Override
    public void setPhoneNumber(final String number, final String phoneContext) {
        try {
            getTelUrl().setLocalNumber(number, phoneContext);
        } catch (final ParseException e) {
            throw new IllegalStateException("Invalid local number.");
        }
    }

    @Override
    public boolean isGlobal() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public URI clone() {
        return new TelURLImpl((TelUrl) Uri.parse(uri.toString()));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.uri != null ? this.uri.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TelURLImpl other = (TelURLImpl) obj;
        if (this.uri != other.uri && (this.uri == null || !this.uri.equals(other.uri))) {
            return false;
        }
        return true;
    }
}
