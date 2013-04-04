package org.andrewwinter.sip.sdp;

import java.util.Locale;

/**
 *
 * @author andrewwinter77
 */
public enum AttributeName {

    /**
     *
     */
    CAT,
    /**
     *
     */
    KEYWDS,
    /**
     *
     */
    TOOL,
    /**
     *
     */
    PTIME,
    /**
     *
     */
    MAXPTIME,
    /**
     *
     */
    RTPMAP,
    /**
     *
     */
    RECVONLY,
    /**
     *
     */
    SENDRECV,
    /**
     *
     */
    SENDONLY,
    /**
     *
     */
    INACTIVE,
    /**
     *
     */
    ORIENT,
    /**
     *
     */
    TYPE,
    /**
     *
     */
    CHARSET,
    /**
     *
     */
    SDPLANG,
    /**
     *
     */
    LANG,
    /**
     *
     */
    FRAMERATE,
    /**
     *
     */
    QUALITY,
    /**
     *
     */
    FMTP;

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
     * @param name
     * @return
     */
    public static AttributeName fromString(final String name) {
        return valueOf(name.toUpperCase(Locale.US));
    }
}
