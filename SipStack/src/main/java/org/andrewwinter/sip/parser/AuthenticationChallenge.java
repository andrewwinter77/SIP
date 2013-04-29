package org.andrewwinter.sip.parser;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 *
 * @author andrew
 */
public class AuthenticationChallenge implements Serializable {

    private final String scheme;
    
    private HashMap<String, String> params;
    
    private AuthenticationChallenge(final String scheme, final HashMap<String, String> params) {
        this.scheme = scheme;
        this.params = params;
    }
    
    /**
     *
     * @return
     */
    public String getScheme() {
        return scheme;
    }
    
    /**
     *
     * @param name
     * @return
     */
    public String getParam(final String name) {
        return params.get(name);
    }
    
    /**
     *
     * @return
     */
    public Set<String> getParamNames() {
        return Collections.unmodifiableSet(params.keySet());
    }
    
    /**
     *
     * @param str
     * @return
     * @throws ParseException
     */
    public static AuthenticationChallenge parse(final String str) throws ParseException {
        
        final String[] parts = str.trim().split("\\s+", 2);
        if (parts.length != 2) {
            throw new ParseException("Too few parts in challenge string.");
        }
        final String scheme = parts[0].toUpperCase(Locale.US);
        
        final HashMap<String, String> params = new HashMap<String, String>();
        final List<String> paramStrings = Util.safeSplit(parts[1]);
        for (final String paramString : paramStrings) {
            
            final String[] paramParts = paramString.split("=", 2);
            
            String paramValue = paramParts[1];
            if (paramValue.charAt(0) == '"' && paramValue.charAt(paramValue.length()-1) == '"') {
                paramValue = paramValue.substring(1, paramValue.length()-1);
            }
            
            params.put(paramParts[0].trim().toLowerCase(Locale.US), paramValue);
        }
        
        return new AuthenticationChallenge(scheme, params);
    }
}
