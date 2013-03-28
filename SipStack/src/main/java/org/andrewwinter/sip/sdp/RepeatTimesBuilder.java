package org.andrewwinter.sip.sdp;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrewwinter77
 */
public class RepeatTimesBuilder {

    private Time interval;
    
    private Time duration;
    
    private List<Time> offsets;
    
    /**
     * Set some legal defaults.
     */
    public RepeatTimesBuilder() {
        offsets = new ArrayList<Time>();
    }
    
    private static void nullCheck(final Object obj, final String desc) {
        if (obj == null) {
            throw new IllegalArgumentException(desc + " must not be null.");
        }
    }
    
    /**
     *
     * @param interval
     * @return
     */
    public RepeatTimesBuilder interval(final Time interval) {
        nullCheck(interval, "interval");
        this.interval = interval;
        return this;
    }
    
    /**
     *
     * @param duration
     * @return
     */
    public RepeatTimesBuilder duration(final Time duration) {
        nullCheck(duration, "duration");
        this.duration = duration;
        return this;
    }

    /**
     *
     * @param offset
     * @return
     */
    public RepeatTimesBuilder offset(final Time offset) {
        nullCheck(offset, "offset");
        offsets.add(offset);
        return this;
    }

    /**
     *
     * @return
     */
    public RepeatTimes build() {
        nullCheck(interval, "interval");
        nullCheck(duration, "duration");
        if (offsets.isEmpty()) {
            throw new IllegalArgumentException("At least one offset must be specified.");
        }
        return new RepeatTimes(interval, duration, offsets);
    }
    
    /**
     *
     * @param originLine
     * @return
     */
    public static RepeatTimes parse(final String originLine) {
        final String[] parts = originLine.split(" ");
        
        RepeatTimesBuilder builder = new RepeatTimesBuilder();
        
//        try {
            builder = builder.interval(TimeBuilder.parse(parts[0]));
            builder = builder.duration(TimeBuilder.parse(parts[1]));
            for (int i=2; i<parts.length; ++i) {
                builder = builder.offset(TimeBuilder.parse(parts[i]));
            }
//        } catch (IllegalArgumentException e) {
//            return null;
//        }

        return builder.build();
    }
}
