package org.andrewwinter.sip.sdp;

/**
 *
 * @author andrewwinter77
 */
public class Attribute {

    private final AttributeName name;
    
    /**
     * Can be null.
     */
    private final String value;
    
    /**
     * 
     * @param name
     * @param value
     */
    Attribute(
            final AttributeName name,
            final String value) {
        
        this.name = name;
        this.value = value;
    }
            
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(name);
        if (value != null) {
            sb.append(":").append(value);
        }
        return sb.toString();
    }

    /**
     *
     * @return
     */
    public AttributeName getName() {
        return name;
    }

    /**
     * Null if this is a 'property' attribute.
     * Non-null if this is a 'value' attribute.
     * @return 
     */
    public String getValue() {
        return value;
    }
}
