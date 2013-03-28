package org.andrewwinter.sip.parser;

import java.io.Serializable;

/**
 *
 * @author andrew
 */
public class Timestamp implements Serializable {

    private float value;
    
    private float delay;

    /**
     *
     * @return
     */
    public float getValue() {
        return value;
    }
    
    /**
     *
     * @param value
     */
    public void setValue(final float value) {
        this.value = value;
    }
    
    /**
     *
     * @return
     */
    public float getDelay() {
        return delay;
    }
    
    /**
     *
     * @param delay
     */
    public void setDelay(final float delay) {
        this.delay = delay;
    }
    
    /**
     *
     */
    public Timestamp() {
    }
    
    /**
     *
     * @param str
     */
    public Timestamp(final String str) {
        final String[] parts = str.trim().split("\\s+");
        value = Float.parseFloat(parts[0]);
        if (parts.length > 1) {
            delay = Float.parseFloat(parts[1]);
        }
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return value + " " + delay;
    }
}
