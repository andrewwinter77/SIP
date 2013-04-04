package org.andrewwinter.sip.sdp;

/**
 * Note that there is intentionally no parser in this class. See
 * TimeZonesBuilder for the z= field parser.
 * @author andrewwinter77
 */
public class TimeZoneBuilder {

    private long time;

    private Time offset;
    
    /**
     * Set some legal defaults.
     */
    public TimeZoneBuilder() {
    }

    private static void nullCheck(final Object obj, final String desc) {
        if (obj == null) {
            throw new IllegalArgumentException(desc + " must not be null.");
        }
    }

    /**
     *
     * @param time
     * @return
     */
    public TimeZoneBuilder time(final long time) {
        this.time = time;
        return this;
    }
    
    /**
     *
     * @param offset
     * @return
     */
    public TimeZoneBuilder offset(final Time offset) {
        nullCheck(offset, "offset");
        this.offset = offset;
        return this;
    }

    /**
     *
     * @return
     */
    public TimeZone build() {
        nullCheck(offset, "offset");
        return new TimeZone(time, offset);
    }
}
