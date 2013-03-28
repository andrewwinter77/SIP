package org.andrewwinter.sip.message;

import java.util.List;
import org.andrewwinter.sip.element.StatefulProxy;
import org.andrewwinter.sip.parser.Address;
import org.andrewwinter.sip.parser.SipMessageHelper;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.parser.Uri;
import org.andrewwinter.sip.properties.ServerProperties;
import org.andrewwinter.sip.transaction.server.ServerTransaction;
import org.andrewwinter.sip.transport.ResponseSender;
import org.andrewwinter.sip.util.RandomStringFactory;

/**
 *
 * @author andrew
 */
public class InboundSipRequest {

    private final SipRequest request;
    
    private ServerTransaction serverTransaction;

    private final ResponseSender responseSender;
    
    private String toTag;
    
    /**
     * 
     * @param request
     * @param responseSender
     */
    public InboundSipRequest(final SipRequest request, final ResponseSender responseSender) {
        this.request = request;
        this.responseSender = responseSender;
    }
    
    /**
     * 
     * @return 
     */
    public ResponseSender getResponseSender() {
        return responseSender;
    }
    
    /**
     * 
     * @return 
     */
    public ServerTransaction getServerTransaction() {
        return serverTransaction;
    }

    /**
     * 
     * @param serverTransaction 
     */
    public void setServerTransaction(final ServerTransaction serverTransaction) {
        this.serverTransaction = serverTransaction;
    }
    
    /**
     * 
     * @return 
     */
    public SipRequest getRequest() {
        return request;
    }
    
    // TODO: This method is temporary. When we introduce dialogs we probably
    // won't need this.
    /**
     *
     * @return
     */
    public String getToTag() {
        return toTag;
    }
    
    /**
     * 
     * @param response 
     */
    public void sendResponse(final SipResponse response) {
        serverTransaction.handleResponseFromTU(response);
    }
    
    /**
     * 
     * @param targets
     * @param parallelFork  
     */
    public void proxy(final List<Uri> targets, final boolean parallelFork) {
        final StatefulProxy sp = new StatefulProxy(targets, parallelFork, this);
        serverTransaction.setProxy(sp);
        sp.proxy();
    }
    
    /**
     * Uses the default reason phrase for the response code.
     * @param type
     * @return 
     */
    public SipResponse createResponse(final ResponseType type) {
        return createResponse(type.getStatusCode(), type.getReasonPhrase());
    }

    /**
     * 
     * @param type
     * @param reasonPhrase 
     * @return 
     */
    public SipResponse createResponse(final int statusCode, final String reasonPhrase) {
        final SipResponse response = new SipResponse(statusCode, reasonPhrase);
        
        // The From field of the response MUST equal the From header field of
        // the request.
        SipMessageHelper.setFrom(SipMessageHelper.getFrom(request), response);
        
        // The Call-ID header field of the response MUST equal the Call-ID
        // header field of the request.
        response.setCallId(request.getCallId());
        
        // The CSeq header field of the response MUST equal the CSeq field of
        // the request.
        response.setCSeq(request.getCSeq());
        
        // The Via header field values in the response MUST equal the Via header
        // field values in the request and MUST maintain the same ordering.
        SipMessageHelper.setVias(request.getVias(), response);
        
        
        final Address to = SipMessageHelper.getTo(request);
        if (to.getTag() != null && to.getTag().length() > 0) {

            // If a request contained a To tag in the request, the To header
            // field in the response MUST equal that of the request.
        } else {
            
            // However, if the To header field in the request did not contain a
            // tag, the URI in the To header field in the response MUST equal
            // the URI in the To header field; additionally, the UAS MUST add a
            // tag to the To header field in the response (with the exception of
            // the 100 (Trying) response, in which a tag MAY be present).
            
            // The 100 (Trying) response is constructed according to the
            // procedures in Section 8.2.6, except that the insertion of tags in
            // the To header field of the response (when none was present in the
            // request) is downgraded from MAY to SHOULD NOT.
            
            if (statusCode != 100) {
                
                // The same tag MUST be used for all responses to that request,
                // both final and provisional (again excepting the 100
                // (Trying)).
                if (toTag == null) {
                    toTag = RandomStringFactory.generateTag();
                }
                
                to.setParameter("tag", toTag);
            }
        }
        SipMessageHelper.setTo(to, response);

        if (ServerProperties.getInstance().includeServerHeader()) {
            response.setServer(ServerProperties.getInstance().getSoftwareString());
        }
        
        return response;
    }
}
