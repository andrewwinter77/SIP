package org.andrewwinter.sip.sdp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author andrewwinter77
 */
public class TimeZones {

    private List<TimeZone> timeZones;
    
    TimeZones(final List<TimeZone> timeZones) {
        this.timeZones = new ArrayList<TimeZone>(timeZones);
    }
            
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        String delimiter = "";
        for (final TimeZone tz : timeZones) {
            sb.append(delimiter).append(tz);
            delimiter = " ";
        }
        return sb.toString();
    }

    /**
     *
     * @return
     */
    public List<TimeZone> getTimeZones() {
        return Collections.unmodifiableList(timeZones);
    }
}
