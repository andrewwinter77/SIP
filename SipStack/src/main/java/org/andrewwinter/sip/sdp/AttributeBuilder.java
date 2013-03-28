package org.andrewwinter.sip.sdp;

/**
 *
 * @author andrewwinter77
 */
public class AttributeBuilder {

    private AttributeName name;
    
    private String value;
    
    /**
     * Set some legal defaults.
     */
    public AttributeBuilder() {
    }

    private static void assertNotYetSet(final Object obj, final String field) {
        if (obj != null) {
            throw new IllegalStateException(field + 
                    " cannot be set more than once.");
        }
    }

    private static void nullCheck(final Object obj, final String desc) {
        if (obj == null) {
            throw new IllegalArgumentException(desc + " must not be null.");
        }
    }

    /**
     *
     * @param name
     * @return
     */
    public AttributeBuilder name(final AttributeName name) {
        assertNotYetSet(this.name, "name");
        nullCheck(name, "name");
        this.name = name;
        return this;
    }
    
    /**
     *
     * @param value
     * @return
     */
    public AttributeBuilder value(final String value) {
        assertNotYetSet(this.value, "value");
        this.value = value;
        return this;
    }

    /**
     *
     * @return
     */
    public Attribute build() {
        nullCheck(name, "name");
        return new Attribute(name, value);
    }
    
    /**
     * Returns null if the attribute type is not recognized.
     * @param str
     * @return 
     */
    public static Attribute parse(final String str) {
        final String[] parts = str.split(":");

        AttributeBuilder builder = new AttributeBuilder();

        try {
            final AttributeName name = AttributeName.fromString(parts[0]);

            builder = builder.name(name);
            if (parts.length > 1) {
                builder = builder.value(parts[1]);
            }

            return builder.build();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
