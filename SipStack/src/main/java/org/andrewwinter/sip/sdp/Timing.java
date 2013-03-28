package org.andrewwinter.sip.sdp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.andrewwinter.sip.sdp.util.Util;

/**
 *
 * @author andrewwinter77
 */
public class Timing {

    private final long startTime;

    private final long stopTime;
    
    private final List<RepeatTimes> repeatTimes;
    
    /**
     * 
     * @param username
     * @param id
     * @param version
     * @param netType
     * @param addrType
     * @param unicastAddr 
     */
    Timing(
            final long startTime,
            final long stopTime,
            final List<RepeatTimes> repeatTimes) {
        
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.repeatTimes = new ArrayList<RepeatTimes>(repeatTimes);
    }
            
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(startTime).append(" ");
        sb.append(stopTime);
        for (final RepeatTimes rt : repeatTimes) {
            sb.append(Util.CRLF).append("r=").append(rt);
        }
        return sb.toString();
    }

    /**
     *
     * @return
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     *
     * @return
     */
    public long getStopTime() {
        return stopTime;
    }

    /**
     *
     * @return
     */
    public List<RepeatTimes> getRepeatTimes() {
        return Collections.unmodifiableList(repeatTimes);
    }
}
