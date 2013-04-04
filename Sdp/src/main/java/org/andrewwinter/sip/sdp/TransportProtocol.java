package org.andrewwinter.sip.sdp;

/**
 *
 * @author andrewwinter77
 */
public enum TransportProtocol {

    /**
     *
     */
    UDP("udp"),

    /**
     *
     */
    RTPAVP("RTP/AVP"),
    
    /**
     *
     */
    RTPSAVP("RTP/SAVP");
    
    private final String type;
    
    private TransportProtocol(final String type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return type;
    }
    
    /**
     *
     * @param type
     * @return
     */
    public static TransportProtocol fromString(final String type) {
        for (final TransportProtocol tp : values()) {
            if (tp.toString().equals(type)) {
                return tp;
            }
        }
        throw new IllegalArgumentException("Unknown protocol " + type);
    }
}
