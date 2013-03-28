package org.andrewwinter.sip.parser;

import java.io.Serializable;
import java.util.Locale;

/**
 *
 * @author andrew
 */
public class CSeq implements Serializable {
    
    private int sequence;
    
    private String method;

    /**
     *
     * @return
     */
    public int getSequence() {
        return sequence;
    }
    
    /**
     *
     * @return
     */
    public String getMethod() {
        return method;
    }
    
    /**
     *
     * @param method
     * @param sequence
     */
    public CSeq(final String method, final int sequence) {
        this.method = method;
        this.sequence = sequence;
    }
    
    /**
     *
     * @param str
     */
    public CSeq(final String str) {
        final String[] parts = str.trim().split("\\s+");
        sequence = Integer.parseInt(parts[0]);
        method = parts[1].toUpperCase(Locale.US);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return sequence + " " + method;
    }
}
