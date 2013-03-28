package org.andrewwinter.sip.parser;

import java.util.Locale;

/**
 *
 * @author andrew
 */
public class GenericUri extends Uri {

    private String uriAsString;

    private String scheme;
    
    
    private GenericUri() {
    }
    
    @Override
    public String getScheme() {
        return scheme;
    }
    
    private void setUriAsString(final String uriAsString) {
        this.uriAsString = uriAsString;
    }

    /**
     *
     * @param scheme
     */
    private void setScheme(final String scheme) {
        
        // From http://en.wikipedia.org/wiki/URI_scheme: 
        // Although schemes are case-insensitive, the canonical form is
        // lowercase and documents that specify schemes must do so with
        // lowercase letters.
        this.scheme = scheme.toLowerCase(Locale.US);
    }

    public static GenericUri parse(String str) throws ParseException {
        final GenericUri uri = new GenericUri();
        
        final String parts[] = str.split(":", 2);
        if (parts.length != 2) {
            throw new ParseException("Invalid URL.");
        }
        uri.setScheme(parts[0]);

        uri.setUriAsString(str);

        return uri;
    }

    @Override
    public String toString() {
        return uriAsString;
    }
}
