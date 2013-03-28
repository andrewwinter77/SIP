package org.andrewwinter.sip.sdp;

/**
 *
 * @author andrewwinter77
 */
public class Time {

    private final long time;
    
    private final TimeUnit unit;
    
    /**
     *
     * @param time
     * @param unit
     */
    public Time(final long time, final TimeUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Null unit not allowed.");
        }
        this.time = time;
        this.unit = unit;
    }
    
    /**
     *
     * @return
     */
    public long getTime() {
        return time;
    }
    
    /**
     *
     * @return
     */
    public TimeUnit getTimeUnit() {
        return unit;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(time);
        if (!unit.equals(TimeUnit.SECONDS)) {
            sb.append(unit);
        }
        return sb.toString();
    }
}
