package org.andrewwinter.sip.registrar;

import java.util.Date;

/**
 *
 * @author andrewwinter77
 */


// TODO: Implement hashcode and equals - it's important we do this for this class
public class Binding {

    private final String callId;
    
    private final int cseq;
    
    private final String uri;
    
    private final Date expiryTime;
    
    /**
     * 
     * @param callId
     * @param cseq
     * @param uri Contact URI.
     * @param expiryTime  
     */
    public Binding(final String callId, final int cseq, final String uri, Date expiryTime) {
        this.callId = callId;
        this.cseq = cseq;
        this.uri = uri;
        this.expiryTime = expiryTime;
    }
    
    /**
     *
     * @return
     */
    public String getCallId() {
        return callId;
    }
    
    /**
     *
     * @return
     */
    public int getCSeqValue() {
        return cseq;
    }
    
    /**
     *
     * @return
     */
    public String getUri() {
        return uri;
    }
    
    /**
     *
     * @return
     */
    public Date getExpiryTime() {
        return expiryTime;
    }
}
