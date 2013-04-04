package org.andrewwinter.sip.sdp;

/**
 *
 * @author andrewwinter77
 */
public class EncryptionKey {

    private final KeyMethod method;
    
    private final String key;
    
    /**
     * 
     * @param method
     * @param key
     */
    EncryptionKey(
            final KeyMethod method,
            final String key) {
        
        this.method = method;
        this.key = key;
    }
            
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(method);
        if (key != null) {
            sb.append(":").append(key);
        }
        return sb.toString();
    }

    /**
     *
     * @return
     */
    public KeyMethod getMethod() {
        return method;
    }

    /**
     *
     * @return
     */
    public String getKey() {
        return key;
    }
}
