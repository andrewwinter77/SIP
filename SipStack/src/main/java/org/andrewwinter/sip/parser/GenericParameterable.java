package org.andrewwinter.sip.parser;

/**
 *
 * @author andrew
 */
public class GenericParameterable extends Parameterable {

    private String value;
    
    private GenericParameterable(final String value) {
        this.value = value.trim();
    }
    
    @Override
    public String getValue() {
        return value;
    }
    

    @Override
    public void setValue(final String value) throws IllegalStateException {
        if (value == null) {
            throw new NullPointerException("Null value.");
        }
        
        this.value = value.trim();
    }

    /**
     *
     * @param addr
     * @return
     * @throws ParseException  
     */
    public static GenericParameterable parse(String s) throws ParseException {
        
        if (s == null || s.isEmpty()) {
            throw new IllegalArgumentException("Empty string or null.");
        }
        
        s = s.trim();

        final GenericParameterable result;
        int indexOfSemicolon = s.indexOf(';');
        if (indexOfSemicolon == -1) {
            result = new GenericParameterable(s);
        } else {
            int indexOfQuotes = s.indexOf('"');
            if (indexOfQuotes == -1) {
                result = new GenericParameterable(s.substring(0, indexOfSemicolon));
                s = s.substring(indexOfSemicolon);
                result.parseParams(s);
            } else {
                while (indexOfQuotes != -1 && indexOfSemicolon > indexOfQuotes) {
                    
                    try {
                        int index = Util.findEndOfQuotedString(s, indexOfQuotes);
                        indexOfQuotes = s.indexOf('"', index+1);
                        indexOfSemicolon = s.indexOf(';', index+1);
                    } catch (IllegalArgumentException e) {
                        throw new ParseException("String has mismatched quotes.");
                    }
                }
                if (indexOfSemicolon == -1) {
                    result = new GenericParameterable(s);
                } else {
                    result = new GenericParameterable(s.substring(0, indexOfSemicolon));
                    s = s.substring(indexOfSemicolon);
                    result.parseParams(s);
                }
            }
        }
        return result;
    }

    @Override
    public GenericParameterable clone() {
        return parse(toString());
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return getValue() + toParamString();
    }
}
