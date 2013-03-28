package org.andrewwinter.sip.sdp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author andrewwinter77
 */
public class RepeatTimes {

    private final Time interval;

    private final Time duration;
    
    private final List<Time> offsets;
    
    /**
     * 
     * @param username
     * @param id
     * @param version
     * @param netType
     * @param addrType
     * @param unicastAddr 
     */
    RepeatTimes(
            final Time interval,
            final Time duration,
            final List<Time> offsets) {
        
        this.interval = interval;
        this.duration = duration;
        this.offsets = new ArrayList<Time>(offsets);
    }
            
    /**
     *
     * @return
     */
    public Time getRepeatInterval() {
        return interval;
    }

    /**
     *
     * @return
     */
    public Time getActiveDuration() {
        return duration;
    }

    /**
     *
     * @return
     */
    public List<Time> getOffsets() {
        return Collections.unmodifiableList(offsets);
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(interval).append(" ");
        sb.append(duration);
        for (final Time offset : offsets) {
            sb.append(" ").append(offset);
        }
        return sb.toString();
    }
}
