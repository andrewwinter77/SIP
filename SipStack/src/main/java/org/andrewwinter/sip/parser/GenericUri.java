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

    /**
     *
     * @param str
     * @return
     * @throws ParseException
     */
    public static GenericUri parse(String str) throws ParseException {
        final GenericUri uri = new GenericUri();
        
        final String parts[] = str.split(":", 2);
        if (parts.length != 2) {
            throw new ParseException("Invalid URL.");
        }
        
        final String scheme = parts[0];

        // One TCK test uses a URI without a scheme, in the form
        // <alice@localhost:5060>. Don't just assume everything before the
        // colon is the scheme, because in the above case we can see that
        // there is no scheme.
        
        if (!scheme.matches("[a-zA-Z]+")) {
            throw new ParseException("Missing scheme.");
        }
        
        uri.setScheme(scheme);

        uri.setUriAsString(str);

        return uri;
    }

    @Override
    public String toString() {
        return uriAsString;
    }
}
