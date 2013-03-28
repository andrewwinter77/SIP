package org.andrewwinter.sip.dialog;

import org.andrewwinter.sip.parser.SipMessageHelper;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;

/**
 * Immutable.
 * @author andrewwinter77
 */
public class DialogId {
    
    private final String callId;
    
    private final String localTag;
    
    private final String remoteTag;
    
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
    public String getLocalTag() {
        return localTag;
    }
    
    /**
     * 
     * @return 
     */
    public String getRemoteTag() {
        return remoteTag;
    }
    
    /**
     * 
     * @param callId
     * @param localTag
     * @param remoteTag 
     */
    private DialogId(final String callId, final String localTag, final String remoteTag) {
        this.callId = callId;
        this.localTag = localTag;
        this.remoteTag = remoteTag;
    }

    /**
     * {@inheritDoc}
     * @param obj
     * @return  
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DialogId other = (DialogId) obj;
        if ((this.callId == null) ? (other.callId != null) : !this.callId.equals(other.callId)) {
            return false;
        }
        if ((this.localTag == null) ? (other.localTag != null) : !this.localTag.equals(other.localTag)) {
            return false;
        }
        if ((this.remoteTag == null) ? (other.remoteTag != null) : !this.remoteTag.equals(other.remoteTag)) {
            return false;
        }
        return true;
    }
    
    /**
     * 
     * @return 
     */
    @Override
    public String toString() {
        return "DialogId[" + callId + ", " + localTag + ", " + remoteTag + "]";
    }

    /**
     * {@inheritDoc}
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.callId != null ? this.callId.hashCode() : 0);
        hash = 71 * hash + (this.localTag != null ? this.localTag.hashCode() : 0);
        hash = 71 * hash + (this.remoteTag != null ? this.remoteTag.hashCode() : 0);
        return hash;
    }
    
    /**
     * 
     * @param request
     * @param response
     * @return 
     */
    public static DialogId createForUAC(final SipRequest request, final SipResponse response) {
        
        // The call identifier component of the dialog ID MUST be set to the
        // value of the Call-ID in the request. The local tag component of the
        //dialog ID MUST be set to the tag in the From field in the request, and
        // the remote tag component of the dialog ID MUST be set to the tag in
        // the To field of the response. A UAC MUST be prepared to receive a
        // response without a tag in the To field, in which case the tag is
        // considered to have a value of null.
        
        return new DialogId(
                request.getCallId(),
                SipMessageHelper.getFrom(request).getTag(),
                SipMessageHelper.getTo(response).getTag());
    }

    /**
     * 
     * @param request
     * @param response
     * @return 
     */
    public static DialogId createForUAS(final SipRequest request, final SipResponse response) {
        
        // The call identifier component of the dialog ID MUST be set to the
        // value of the Call-ID in the request. The local tag component of the
        // dialog ID MUST be set to the tag in the To field in the response to
        // the request (which always includes a tag), and the remote tag
        // component of the dialog ID MUST be set to the tag from the From field
        // in the request. A UAS MUST be prepared to receive a request without a
        // tag in the From field, in which case the tag is considered to have a
        // value of null.
        
        return new DialogId(
                response.getCallId(),
                SipMessageHelper.getTo(response).getTag(),
                SipMessageHelper.getFrom(request).getTag());
    }

    /**
     * Requires that the request contains a To tag.
     * @param request
     * @return 
     */
    public static DialogId createForUAS(final SipRequest request) {
        
        // As one would expect for a UAS, the Call-ID value of the dialog ID is
        // set to the Call-ID of the message, the remote tag is set to the tag
        // in the From field of the message, and the local tag is set to the tag
        // in the To field of the message.
        
        // A dialog ID is also associated with all responses and with any
        // request that contains a tag in the To field.
        
        return new DialogId(
                request.getCallId(),
                SipMessageHelper.getTo(request).getTag(),
                SipMessageHelper.getFrom(request).getTag());
    }
}
