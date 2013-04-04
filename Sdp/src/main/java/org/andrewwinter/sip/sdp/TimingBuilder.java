package org.andrewwinter.sip.sdp;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrewwinter77
 */
public class TimingBuilder {

    private long startTime;
    
    private long stopTime;
    
    private List<RepeatTimes> repeatTimes;
    
    /**
     * Set some legal defaults.
     */
    public TimingBuilder() {
        repeatTimes = new ArrayList<RepeatTimes>();
    }
    
    private static void nullCheck(final Object obj, final String desc) {
        if (obj == null) {
            throw new IllegalArgumentException(desc + " must not be null.");
        }
    }
    
    /**
     *
     * @param startTime
     * @return
     */
    public TimingBuilder startTime(final long startTime) {
        this.startTime = startTime;
        return this;
    }
    
    /**
     *
     * @param stopTime
     * @return
     */
    public TimingBuilder stopTime(final long stopTime) {
        this.stopTime = stopTime;
        return this;
    }

    /**
     *
     * @param repeatTimes
     * @return
     */
    public TimingBuilder repeatTimes(final RepeatTimes repeatTimes) {
        nullCheck(repeatTimes, "repeatTimes");
        this.repeatTimes.add(repeatTimes);
        return this;
    }

    /**
     *
     * @return
     */
    public Timing build() {
        return new Timing(startTime, stopTime, repeatTimes);
    }
    
    /**
     *
     * @param originLine
     * @return
     */
    public static TimingBuilder parse(final String originLine) {
        final String[] parts = originLine.split(" ");
        
        TimingBuilder builder = new TimingBuilder();
        
        try {
            builder = builder.startTime(Long.parseLong(parts[0]));
            builder = builder.stopTime(Long.parseLong(parts[1]));
        } catch (IllegalArgumentException e) {
            return null;
        }

        return builder;
    }
}
