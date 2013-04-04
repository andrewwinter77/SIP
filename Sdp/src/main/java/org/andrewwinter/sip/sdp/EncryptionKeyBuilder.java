package org.andrewwinter.sip.sdp;

/**
 *
 * @author andrewwinter77
 */
public class EncryptionKeyBuilder {

    private KeyMethod method;
    
    private String key;
    
    /**
     * Set some legal defaults.
     */
    public EncryptionKeyBuilder() {
    }

    private static void nullCheck(final Object obj, final String desc) {
        if (obj == null) {
            throw new IllegalArgumentException(desc + " must not be null.");
        }
    }

    /**
     *
     * @param method
     * @return
     */
    public EncryptionKeyBuilder method(final KeyMethod method) {
        nullCheck(method, "method");
        this.method = method;
        return this;
    }

    /**
     *
     * @param key
     * @return
     */
    public EncryptionKeyBuilder key(final String key) {
        this.key = key;
        return this;
    }
    
    /**
     *
     * @return
     */
    public EncryptionKey build() {
        return new EncryptionKey(method, key);
    }
    
    /**
     *
     * @param str
     * @return
     */
    public static EncryptionKey parse(final String str) {
        final String[] parts = str.split(":");

        EncryptionKeyBuilder builder = new EncryptionKeyBuilder();

        final KeyMethod method = KeyMethod.fromString(parts[0]);
        builder = builder.method(method);
        if (parts.length > 1) {
            builder = builder.key(parts[1]);
        }
        
        return builder.build();
    }
}
