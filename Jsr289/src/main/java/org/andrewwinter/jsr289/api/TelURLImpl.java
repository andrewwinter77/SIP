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

    private final TelUrl url;
    
    TelURLImpl(final TelUrl url) {
        super(url);
        this.url = url;
    }
    
    @Override
    public String getPhoneNumber() {
        String number = url.getNumber();
        if (number.startsWith("+")) {
            return number.substring(1);
        } else {
            return number;
        }
    }

    @Override
    public String getPhoneContext() {
        return url.getParameter("phone-context");
    }

    @Override
    public void setPhoneNumber(final String number) {
        try {
            url.setGlobalNumber(number);
        } catch (final ParseException e) {
            throw new IllegalStateException("Invalid global number.");
        }
    }

    @Override
    public void setPhoneNumber(final String number, final String phoneContext) {
        try {
            url.setLocalNumber(number, phoneContext);
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
        return new TelURLImpl((TelUrl) Uri.parse(url.toString()));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.url != null ? this.url.hashCode() : 0);
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
        if (this.url != other.url && (this.url == null || !this.url.equals(other.url))) {
            return false;
        }
        return true;
    }
}
