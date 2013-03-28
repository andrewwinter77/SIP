package org.andrewwinter.sip.sdp;

/**
 *
 * @author andrewwinter77
 */
public class TimeBuilder {

    private long time;
    
    private TimeUnit timeUnit;
    
    /**
     * Set some legal defaults.
     */
    public TimeBuilder() {
        timeUnit = TimeUnit.SECONDS;
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
    public TimeBuilder time(final long time) {
        this.time = time;
        return this;
    }
    
    /**
     *
     * @param timeUnit
     * @return
     */
    public TimeBuilder timeUnit(final TimeUnit timeUnit) {
        nullCheck(timeUnit, "timeUnit");
        this.timeUnit = timeUnit;
        return this;
    }

    /**
     *
     * @return
     */
    public Time build() {
        nullCheck(timeUnit, "timeUnit");
        return new Time(time, timeUnit);
    }
    
    /**
     *
     * @param str
     * @return
     */
    public static Time parse(final String str) {
        
        TimeBuilder builder = new TimeBuilder();

        // The string may not have a char indicating the unit at the end.
        // If this is the case then the unit defaults to seconds.
        final int indexOfLastDigit;
        char unitChar = str.charAt(str.length()-1);
        if (unitChar >= '0' && unitChar <= '9') {
            indexOfLastDigit = str.length()-1;
        } else {
            indexOfLastDigit = str.length()-2;
            TimeUnit unit = TimeUnit.fromChar(str.charAt(str.length()-1));
            builder = builder.timeUnit(unit);
        }
        
        try {
            
            final long time = Long.parseLong(str.substring(0, indexOfLastDigit+1));
            builder = builder.time(time);
        } catch (NumberFormatException e) {
            // TODO: Handle this
            throw new IllegalArgumentException("Illegal time: " + str);
//            return null;
//        } catch (IllegalArgumentException e) {
//            return null;
        }

        return builder.build();
    }
}
