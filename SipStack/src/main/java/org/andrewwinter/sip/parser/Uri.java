package org.andrewwinter.sip.parser;

import java.util.Locale;

/**
 *
 * @author andrewwinter77
 */
public abstract class Uri extends Parameterable {

    /**
     *
     * @return
     */
    public abstract String getScheme();
    
    /**
     *
     * @return
     */
    public boolean isSipUri() {
        final String scheme = getScheme();
        return "sip".equals(scheme) || "sips".equals(scheme);
    }
    
    /**
     *
     * @param str
     * @return
     * @throws ParseException  
     */
    public static Uri parse(final String str) throws ParseException {
        final String lower = str.toLowerCase(Locale.US);
        if (lower.startsWith("sip:") || lower.startsWith("sips:")) {
            return SipUri.parse(str);
        } else if (lower.startsWith("tel:")) {
            return TelUrl.parse(str);
        } else {
            return GenericUri.parse(str);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String getValue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setValue(final String value) throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
