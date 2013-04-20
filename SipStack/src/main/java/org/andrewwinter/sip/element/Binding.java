package org.andrewwinter.sip.element;

import java.util.Date;
import org.andrewwinter.sip.parser.Uri;

/**
 *
 * @author andrewwinter77
 */


// TODO: Implement hashcode and equals - it's important we do this for this class
public class Binding {

    private final String callId;
    
    private final int cseq;
    
    private final Uri uri;
    
    private final Date expiryTime;
    
    /**
     * 
     * @param callId
     * @param cseq
     * @param uri Contact URI.
     * @param expiryTime  
     */
    public Binding(final String callId, final int cseq, final Uri uri, Date expiryTime) {
        this.callId = callId;
        this.cseq = cseq;
        this.uri = uri;
        this.expiryTime = (Date) expiryTime.clone();
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
    public Uri getUri() {
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
