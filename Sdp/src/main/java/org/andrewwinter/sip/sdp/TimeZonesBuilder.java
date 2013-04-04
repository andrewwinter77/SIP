package org.andrewwinter.sip.sdp;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrewwinter77
 */
public class TimeZonesBuilder {

    private List<TimeZone> timeZones;
    
    /**
     * Set some legal defaults.
     */
    public TimeZonesBuilder() {
        timeZones = new ArrayList<TimeZone>();
    }

    private static void nullCheck(final Object obj, final String desc) {
        if (obj == null) {
            throw new IllegalArgumentException(desc + " must not be null.");
        }
    }

    /**
     *
     * @param tz
     * @return
     */
    public TimeZonesBuilder timeZone(final TimeZone tz) {
        nullCheck(tz, "tz");
        this.timeZones.add(tz);
        return this;
    }
    
    /**
     *
     * @return
     */
    public TimeZones build() {
        return new TimeZones(timeZones);
    }
    
    /**
     *
     * @param str
     * @return
     */
    public static TimeZones parse(final String str) {
        final String[] parts = str.split(" ");
        
        if (parts.length % 2 != 0) {
            throw new IllegalArgumentException("Invalid z= line.");
        }
        
        TimeZonesBuilder builder = new TimeZonesBuilder();
        for (int i=0; i<parts.length/2; ++i) {
            final int index = i*2;
            TimeZoneBuilder tzb = new TimeZoneBuilder();
            tzb = tzb.time(Long.parseLong(parts[index]));
            tzb = tzb.offset(TimeBuilder.parse(parts[index+1]));
            builder = builder.timeZone(tzb.build());
        }
        
        return builder.build();
    }
}
