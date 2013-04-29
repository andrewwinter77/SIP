package org.andrewwinter.sip.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import org.andrewwinter.sip.parser.Address;
import org.andrewwinter.sip.parser.SipMessageHelper;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.parser.SipUri;
import org.andrewwinter.sip.parser.Uri;

/**
 *
 * @author andrewwinter77
 */
public class Dialog {
    
    private final DialogId id;
    
    private DialogState state;
    
    /**
     * a local sequence number (used to order requests from the UA to its peer)
     */
    private int localSeqNumber;
    
    /**
     * a remote sequence number (used to order requests from its peer to the UA)
     */
    private int remoteSeqNumber;
    
    /**
     * a local URI
     */
    private final Uri localUri;
    
    /**
     * a remote URI
     */
    private final Uri remoteUri;
    
    /**
     * the URI from the Contact header field of the request.
     */
    private final Uri remoteTarget;
    
    private final boolean secure;
    
    /**
     * A route set is a collection of ordered SIP or SIPS URI which represent a
     * list of proxies that must be traversed when sending a particular request.
     * 
     * A route set, which is an ordered list of URIs. The route set is the
     * list of servers that need to be traversed to send a request to the peer.
     */
    private List<SipUri> routeSet;
    
    @Override
    public String toString() {
        return "[Dialog id:" + id + ", state: " + state + "]";
    }
    
    /**
     * This should be used only for transitioning from EARLY to CONFIRMED. If
     * the dialog is already in the CONFIRMED state, this is a no-op. If the
     * 
     * @param state 
     */
    public void setState(final DialogState state) {
        assert(state == DialogState.CONFIRMED);
        this.state = state;
    }
    
    /**
     *
     * @return
     */
    public DialogState getState() {
        return state;
    }
    
    /**
     * 
     * @return 
     */
    public DialogId getId() {
        return id;
    }
    
    /**
     * 
     * @return 
     */
    public Uri getRemoteUri() {
        return remoteUri;
    }
    
    /**
     * 
     * @return 
     */
    public Uri getLocalUri() {
        return localUri;
    }
    
    /**
     * 
     * @return -1 if not set.
     */
    public int getLocalSeqNumber() {
        return localSeqNumber;
    }
    
    /**
     * 
     * @param localSeqNumber 
     */
    public void setLocalSeqNumber(final int localSeqNumber) {
        this.localSeqNumber = localSeqNumber;
    }
    
    /**
     * 
     * @param remoteSeqNumber 
     */
    public void setRemoteSeqNumber(final int remoteSeqNumber) {
        this.remoteSeqNumber = remoteSeqNumber;
    }

    /**
     * 
     * @return 
     */
    public Uri getRemoteTarget() {
        return remoteTarget;
    }
    
    /**
     * 
     * @return Returns an empty list if the route set is empty.
     */
    public List<SipUri> getRouteSet() {
        return routeSet;
    }
    
    /**
     * 
     * @return 
     */
    public boolean isSecure() {
        return secure;
    }
    
    /**
     * 
     * @param id
     * @param secure
     * @param remoteSeqNumber
     * @param localUri
     * @param remoteUri
     * @param remoteTarget
     * @param routeSet
     */
    private Dialog(
            final DialogState state,
            final DialogId id,
            final boolean secure,
            final Uri localUri,
            final Uri remoteUri,
            final Uri remoteTarget,
            final List<SipUri> routeSet) {
        this.state = state;
        this.id = id;
        this.secure = secure;
        this.remoteSeqNumber = -1;
        this.localSeqNumber = -1;
        this.localUri = localUri;
        this.remoteUri = remoteUri;
        this.remoteTarget = remoteTarget;
        this.routeSet = Collections.unmodifiableList(routeSet);
    }
    
    /**
     * The route set MUST be set to the list of URIs in the Record-Route header
     * field from the request, taken in order and preserving all URI parameters.
     * If no Record-Route header field is present in the request, the route set
     * MUST be set to the empty set.
     * 
     * @param request
     * @return 
     */
    private static List<SipUri> getRouteSetFromRequest(final SipRequest request) {
        final List<SipUri> routes = new ArrayList<SipUri>();
        for (final Address address : SipMessageHelper.getRecordRoutes(request)) {
            if (address.getUri().isSipUri()) {
                routes.add((SipUri) address.getUri());
            }
        }
        return routes;
    }
    
    /**
     * The route set MUST be set to the list of URIs in the Record-Route header
     * field from the response, taken in reverse order and preserving all URI
     * parameters. If no Record-Route header field is present in the response,
     * the route set MUST be set to the empty set.
     * 
     * @param response
     * @return 
     */
    private static List<SipUri> getRouteSetFromResponse(final SipResponse response) {
        final List<SipUri> routes = new ArrayList<SipUri>();
        final List<Address> recordRoutes = SipMessageHelper.getRecordRoutes(response);
        final ListIterator<Address> iter = recordRoutes.listIterator(recordRoutes.size());
        while (iter.hasPrevious()) {
            final Uri uri = iter.previous().getUri();
            if (uri.isSipUri()) {
                routes.add((SipUri) uri);
            }
        }
        return routes;
    }
    
    /**
     * 
     * @param request
     * @param response
     * @param state 
     * @return 
     */
    public static Dialog createForUAS(final SipRequest request, final SipResponse response, final DialogState state) {
        
        final DialogId id = DialogId.createForUAS(request, response);
        
        // If the request arrived over TLS, and the Request-URI contained a SIPS
        // URI, the "secure" flag is set to TRUE.
        
        final boolean secure = request.getTopmostVia().getTransport().equals("TLS")
                && request.getRequestUri().getScheme().equals("sips");
        
        final Dialog dialog = new Dialog(
                state,
                id,
                secure,
                SipMessageHelper.getTo(request).getUri(),
                SipMessageHelper.getFrom(request).getUri(),
                
                // The remote target MUST be set to the URI from the Contact
                // header field of the request.
                SipMessageHelper.getContact(request).get(0).getUri(),
                
                getRouteSetFromRequest(request));

        dialog.setRemoteSeqNumber(request.getCSeq().getSequence());
        DialogStore.getInstance().put(dialog);
        
        return dialog;
    }

    /**
     * @param request
     * @param response
     * @param state 
     * @return 
     */
    public static Dialog createForUAC(final SipRequest request, final SipResponse response, final DialogState state) {
        
        final DialogId id = DialogId.createForUAC(request, response);
        
        // If the request was sent over TLS, and the Request-URI contained a
        // SIPS URI, the "secure" flag is set to TRUE.
        
        final boolean secure = request.getTopmostVia().getTransport().equals("TLS")
                && request.getRequestUri().getScheme().equals("sips");
        // TODO: Verify that the above works. Has the transport been stored in the message?
        
        final Dialog dialog = new Dialog(
                state,
                id,
                secure,
                
                // The remote URI MUST be set to the URI in the To field, and
                // the local URI MUST be set to the URI in the From field.
                SipMessageHelper.getFrom(request).getUri(),
                SipMessageHelper.getTo(request).getUri(),
                
                // The remote target MUST be set to the URI from the Contact
                // header field of the response.
                SipMessageHelper.getContact(response).get(0).getUri(),
                
                getRouteSetFromResponse(response));
        
        // The local sequence number MUST be set to the value of the sequence
        // number in the CSeq header field of the request.
        dialog.setLocalSeqNumber(request.getCSeq().getSequence());
        
        
        DialogStore.getInstance().put(dialog);
        
        return dialog;
    }
}
