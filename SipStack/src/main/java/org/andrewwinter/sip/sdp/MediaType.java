package org.andrewwinter.sip.sdp;

import java.util.Locale;

/**
 *
 * @author andrewwinter77
 */
public enum MediaType {

    /**
     *
     */
    AUDIO,
    /**
     *
     */
    VIDEO,
    /**
     *
     */
    TEXT,
    /**
     *
     */
    APPLICATION,
    /**
     *
     */
    MESSAGE;
    
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
    public static MediaType fromString(final String type) {
        return valueOf(type.toUpperCase(Locale.US));
    }
}
