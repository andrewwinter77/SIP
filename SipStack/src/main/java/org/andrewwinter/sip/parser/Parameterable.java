package org.andrewwinter.sip.parser;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author andrewwinter77
 */
public abstract class Parameterable implements Serializable, Cloneable {

    private Map<String, String> params;
    
    Parameterable() {
        params = new HashMap<String, String>();
    }
    
    @Override
    public Object clone() {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Returns the field value (minus any parameters) as a string.
     * @return
     */
    public abstract String getValue();
    
    /**
     * 
     * @param value
     * @throws IllegalStateException if the header field cannot be modified for
     * this object
     */
    public abstract void setValue(String value) throws IllegalStateException;
    
    /**
     * Returns the value of the named parameter, or null if it is not set.
     * A zero-length String indicates a flag parameter.
     * @param key
     * @return
     */
    public String getParameter(final String key) {
        return params.get(key.toLowerCase(Locale.US));
    }
    
    /**
     * Returns an unmodifiable collection of parameter name-value mappings.
     * @return 
     */
    public Set<Entry<String, String>> getParameters() {
        return Collections.unmodifiableSet(params.entrySet());
    }
    
    /**
     *
     * @return
     */
    public Set<String> getParameterNames() {
        return Collections.unmodifiableSet(params.keySet());
    }

    /**
     *
     * @param key
     * @param value Use null to remove the parameter.
     */
    public void setParameter(String key, final String value) {
        if (key == null) {
            throw new IllegalArgumentException("Null param name.");
        }
        
        key = key.toLowerCase(Locale.US);
        
        if (value == null) {
            params.remove(key);
        } else {
            params.put(key, value);
        }
    }
    
    final void parseParams(String str) {
        while (str.startsWith(";")) {
            str = str.replaceFirst(";\\s*", "");
            
            final int indexOfEquals = str.indexOf('=');
            int indexOfSemicolon = str.indexOf(';');
            
            int endOfKey = indexOfEquals;
            if (endOfKey == -1 || (indexOfSemicolon != -1 && indexOfSemicolon < indexOfEquals)) {
                endOfKey = indexOfSemicolon;
            }
            if (endOfKey == -1) {
                endOfKey = str.length();
            }
            
            final String key = str.substring(0, endOfKey).trim();
            
            str = str.substring(endOfKey);
            
            if (str.startsWith("=")) {
                
                String value;
                str = str.substring(1);
                
                if (str.startsWith("\"")) {
                    int end = Util.findEndOfQuotedString(str);
                    value = str.substring(1, end);
                    str = str.substring(end+1);
                } else {
                    indexOfSemicolon = str.indexOf(';');
                    if (indexOfSemicolon == -1) {
                        value = str;
                    } else {
                        value = str.substring(0, indexOfSemicolon);
                        str = str.substring(indexOfSemicolon);
                    }
                }
                
                setParameter(key, value.trim());
                
            } else {
                setParameter(key, "");
            }
        }
    }
    
    /**
     *
     * @return
     */
    String toParamString() {
        final StringBuilder sb = new StringBuilder();
        
        for (final Entry<String, String> entry : params.entrySet()) {
            sb.append(";");
            sb.append(entry.getKey());
            if (!entry.getValue().isEmpty()) {
                sb.append("=");
                
                // TODO: Turn into a quoted string, if necessary
                sb.append(entry.getValue());
            }
        }
        
        return sb.toString();
    }
}
