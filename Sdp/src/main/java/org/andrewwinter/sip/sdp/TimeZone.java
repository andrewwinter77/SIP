package org.andrewwinter.sip.sdp;

/**
 *
 * @author andrewwinter77
 */
public class TimeZone {

    private final long time;
    
    private final Time offset;
    
    TimeZone(
            final long time,
            final Time offset) {
        this.time = time;
        this.offset = offset;
    }
            
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(time).append(" ");
        sb.append(offset);
        return sb.toString();
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
    public Time getOffset() {
        return offset;
    }
}
