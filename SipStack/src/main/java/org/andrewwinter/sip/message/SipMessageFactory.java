package org.andrewwinter.sip.message;


import java.util.List;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.dialog.DialogId;
import org.andrewwinter.sip.parser.*;
import org.andrewwinter.sip.properties.ServerProperties;
import org.andrewwinter.sip.util.RandomStringFactory;
import org.andrewwinter.sip.util.Util;

/**
 *
 * @author andrewwinter77
 */
public class SipMessageFactory {

    /**
     *
     * @return
     */
    public static String createBranchTag() {
        return Via.MAGIC_COOKIE + RandomStringFactory.generate(30);
    }
    
    /**
     * 
     * @param request
     * @param dialog 
     */
    private static void populateInDialogHeaders(final SipRequest request, final Dialog dialog) {
        
        String tag;
        final DialogId dialogId = dialog.getId();
        
        // The URI in the To field of the request MUST be set to the remote URI
        // from the dialog state. The tag in the To header field of the request
        // MUST be set to the remote tag of the dialog ID.
        
        final Address to = new Address(dialog.getRemoteUri());
        tag = dialogId.getRemoteTag();
        if (tag != null) {
            to.setParameter("tag", tag);
        }
        SipMessageHelper.setTo(to, request);

        // The From URI of the request MUST be set to the local URI from the
        // dialog state. The tag in the From header field of the request MUST be
        // set to the local tag of the dialog ID. If the value of the remote or
        // local tags is null, the tag parameter MUST be omitted from the To or
        // From header fields, respectively.        
        
        final Address from = new Address(dialog.getLocalUri());
        tag = dialogId.getLocalTag();
        if (tag != null) {
            from.setParameter("tag", tag);
        }
        SipMessageHelper.setFrom(from, request);
    
        // The Call-ID of the request MUST be set to the Call-ID of the dialog.

        request.setCallId(dialogId.getCallId());
    }
    
    private static Via createVia(final String method, final SipRequest associatedRequest) {
        
        // The Via header maddr, ttl, and sent-by components will be set when
        // the request is processed by the transport layer (Section 18).

        final Via via = new Via(Util.getIpAddress());
        if (associatedRequest != null && ("CANCEL".equals(method) || "ACK".equals(method))) {
            
            // The branch parameter value MUST be unique across space and time
            // for all requests sent by the UA. The exceptions to this rule are
            // CANCEL and ACK for non-2xx responses. As discussed below, a
            // CANCEL request will have the same value of the branch parameter
            // as the request it cancels. As discussed in Section 17.1.1, an ACK
            // for a non-2xx response will also have the same branch ID as the
            // INVITE whose response it acknowledges.
            
            via.setBranch(associatedRequest.getTopmostVia().getBranch());
        } else {
            
            // The branch ID inserted by an element compliant with this
            // specification MUST always begin with the characters "z9hG4bK".
            // These 7 characters are used as a magic cookie (7 is deemed
            // sufficient to ensure that an older RFC 2543 implementation would
            // not pick such a value), so that servers receiving the request can
            // determine that the branch ID was constructed in the fashion
            // described by this specification (that is, globally unique).
            // Beyond this requirement, the precise format of the branch token
            // is implementation-defined.            
            
            // [Andrew] UserAgentClient will set a branch param for us if we
            // don't set one ourselves.
        }
        via.setPort(ServerProperties.getInstance().getUnsecurePort());
        
        // TODO: This is temporary. Remove it when we support listening for responses on the correct port.
        via.setParameter("maddr", Util.getIpAddress());
        // End of temporary stuff
        
        if (ServerProperties.getInstance().useRport()) {
            via.setParameter("rport", "");
        }
        
        return via;
    }
    
    
    /**
     * Creates an in-dialog request.
     * @param dialog
     * @param method
     * @return 
     */
    public static SipRequest createInDialogRequest(
            final Dialog dialog,
            final String method) {
    
        Uri requestUri;
        if (dialog.getRouteSet().isEmpty()) {
            
            // If the route set is empty, the UAC MUST place the remote target
            // URI into the Request-URI.
            
            // The UAC MUST NOT add a Route header field to the request.
            
            requestUri = dialog.getRemoteTarget();
        } else {
            
            final SipUri uri = dialog.getRouteSet().get(0);
            
            // TODO: Handle case where route set is not empty
            requestUri = null; // TODO: FIX THIS!
        }
        
        final SipRequest request = new SipRequest(method, requestUri);

        populateInDialogHeaders(request, dialog);
        SipMessageHelper.pushVia(createVia(method, null), request);
        
        int seq = dialog.getLocalSeqNumber();
        if (seq < 0) {

            // If the local sequence number is empty, an initial value MUST be
            // chosen using the guidelines of Section 8.1.1.
            
            // The sequence number value MUST be expressible as a
            // 32-bit unsigned integer and MUST be less than 2**31.
            
            // A client could, for example, choose the 31 most significant bits
            // of a 32-bit second clock as an initial sequence number.
            
            seq = generateSequence();
        } else {
            
            // Therefore, if the local sequence number is not empty, the value
            // of the local sequence number MUST be incremented by one, and this
            // value MUST be placed into the CSeq header field.
            
            ++seq;
        }
        dialog.setLocalSeqNumber(seq);
        
        // The method field in the CSeq header field value MUST match the method
        // of the request.
        
        final CSeq cseq = new CSeq(method, seq);
        request.setCSeq(cseq);
        
        // A UAC MUST insert a Max-Forwards header field into each request it
        // originates with a value that SHOULD be 70.
        
        SipMessageHelper.setMaxForwards(70, request);
                
        return request;
    }
    
    /**
     * 
     * @return 
     */
    private static int generateSequence() {
        return (int) (System.currentTimeMillis() & (long) 0xFFFF);
    }
    
    /**
     * Creates an out-of-dialog request.
     * @param method
     * @param to String representation of either an Address or URI.
     * @param from 
     * @param associatedRequest If {@code method} is {@code CANCEL} then this
     * must be the {@code INVITE} being canceled. if {@code method} is 
     * {@code ACK} then this must be the corresponding {@code INVITE} but ONLY
     * if the response is non-2xx (for 2xx responses, {@code associatedRequest}
     * must be {@code null}).
     * @param contact 
     * @return 
     */
    public static SipRequest createOutOfDialogRequest(
            final String method,
            final String to,
            final String from,
            final SipRequest associatedRequest,
            final Address contact) throws ParseException {
        
        final Address toAddress = Address.parse(to);
        final Uri toUri = toAddress.getUri();

        final Address fromAddress = Address.parse(from);
        
        final Uri requestUri;
        if ("REGISTER".equals(method)) {
            // One notable exception is the REGISTER method; behavior for
            // setting the Request-URI of REGISTER is given in Section 10.
            
            // TODO: Do this
            requestUri = toUri; // TEMPORARY
        } else {
            
            // The initial Request-URI of the message SHOULD be set to the value
            // of the URI in the To field.
            
            requestUri = toUri;
        }
        
        final SipRequest request = new SipRequest(method, requestUri);
        
        // A UAC MUST insert a Max-Forwards header field into each request it
        // originates with a value that SHOULD be 70.
        SipMessageHelper.setMaxForwards(70, request);
        
        if ("INVITE".equals(method)) {
            
            // The Contact header field MUST be present and contain exactly one
            // SIP or SIPS URI in any request that can result in the
            // establishment of a dialog. For the methods defined in this
            // specification, that includes only the INVITE request.
            
            SipMessageHelper.setContact(contact, request);
        }
        
        // The From field MUST contain a new tag parameter, chosen by the UAC.
        fromAddress.setParameter("tag", RandomStringFactory.generateTag());
        SipMessageHelper.setFrom(fromAddress, request);
        
        
        // The sequence number value MUST be expressible as a 32-bit unsigned
        // integer and MUST be less than 2**31. As long as it follows the above
        // guidelines, a client may use any mechanism it would like to select
        // CSeq header field values.
        final CSeq cseq = new CSeq(method, generateSequence());
        request.setCSeq(cseq);
        
        SipMessageHelper.setTo(toAddress, request);
        
        request.setCallId(RandomStringFactory.generateCallId());
        
        // In the case of stream-oriented transports such as TCP, the
        // Content-Length header field indicates the size of the body. The
        // Content-Length header field MUST be used with stream oriented
        // transports.
        SipMessageHelper.setContentLength(0, request);
        
        SipMessageHelper.pushVia(createVia(method, associatedRequest), request);
        
        return request;
    }
    
    /**
     *
     * @param requestBeingCancelled
     * @return
     */
    public static SipRequest createCancel(final SipRequest requestBeingCancelled) {
        
        // The following procedures are used to construct a CANCEL request. The
        // Request-URI, Call-ID, To, the numeric part of CSeq, and From header
        // fields in the CANCEL request MUST be identical to those in the
        // request being cancelled, including tags.
        
        final SipRequest cancel = new SipRequest("CANCEL", requestBeingCancelled.getRequestUri());

        cancel.setCallId(requestBeingCancelled.getCallId());
        SipMessageHelper.setTo(SipMessageHelper.getTo(requestBeingCancelled), cancel);
        SipMessageHelper.setFrom(SipMessageHelper.getFrom(requestBeingCancelled), cancel);

        // However, the method part of the CSeq header field MUST have a value
        // of CANCEL.

        final CSeq cseq = new CSeq("CANCEL", requestBeingCancelled.getCSeq().getSequence());
        cancel.setCSeq(cseq);
        
        // A CANCEL constructed by a client MUST have only a single Via header
        // field value matching the top Via value in the request being
        // cancelled.

        SipMessageHelper.pushVia(requestBeingCancelled.getTopmostVia(), cancel);
        
        final List<Address> routes = requestBeingCancelled.getRoutes();
        if (routes != null && !routes.isEmpty()) {
            cancel.setRoutes(routes);
        }
        
        return cancel;
    }
    
    /**
     *
     * @param isr
     * @return
     */
    public static SipRequest createAck(final InboundSipResponse isr) {
        
        final SipRequest invite = isr.getRequest();
        final SipRequest ack = new SipRequest("ACK", invite.getRequestUri());
        
        // The header fields of the ACK are constructed in the same way as for
        // any request sent within a dialog (see Section 12) with the exception
        // of the CSeq and the header fields related to authentication.
        
        populateInDialogHeaders(ack, isr.getDialog());
        SipMessageHelper.pushVia(createVia("ACK", invite), ack);

        
        // The sequence number of the CSeq header field MUST be the same as the
        // INVITE being acknowledged, but the CSeq method MUST be ACK.
        
        final CSeq cseq = new CSeq("ACK", invite.getCSeq().getSequence());
        ack.setCSeq(cseq);
        
        return ack;
    }
}
