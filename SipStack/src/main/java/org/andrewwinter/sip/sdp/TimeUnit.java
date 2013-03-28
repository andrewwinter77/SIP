package org.andrewwinter.sip.sdp;

/**
 *
 * @author andrewwinter77
 */
public enum TimeUnit {

    /**
     *
     */
    DAYS('d'),
    
    /**
     *
     */
    HOURS('h'),

    /**
     *
     */
    MINUTES('m'),
    
    /**
     *
     */
    SECONDS('s');

    private final char unit;
    
    private TimeUnit(final char unit) {
        this.unit = unit;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return String.valueOf(unit);
    }
    
    /**
     *
     * @param unit
     * @return
     */
    public static TimeUnit fromChar(final char unit) {
        for (final TimeUnit tu : values()) {
            if (tu.unit == unit) {
                return tu;
            }
        }
        throw new IllegalArgumentException("Unknown time unit: " + unit);
    }
}
