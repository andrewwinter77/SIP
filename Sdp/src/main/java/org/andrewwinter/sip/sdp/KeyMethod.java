package org.andrewwinter.sip.sdp;

import java.util.Locale;

/**
 *
 * @author andrewwinter77
 */
public enum KeyMethod {

    /**
     *
     */
    CLEAR,
    /**
     *
     */
    BASE64,
    /**
     *
     */
    URI,
    /**
     *
     */
    PROMPT;
    
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return super.toString().toLowerCase(Locale.US);
    }

    /**
     *
     * @param type
     * @return
     */
    public static KeyMethod fromString(final String type) {
        return valueOf(type.toUpperCase(Locale.US));
    }
}
