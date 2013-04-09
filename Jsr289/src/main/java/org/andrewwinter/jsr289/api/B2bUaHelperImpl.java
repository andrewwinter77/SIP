package org.andrewwinter.jsr289.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.sip.B2buaHelper;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletMessage;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipSession.State;
import javax.servlet.sip.TooManyHopsException;
import javax.servlet.sip.UAMode;
import org.andrewwinter.jsr289.store.SipSessionStore;
import org.andrewwinter.sip.parser.HeaderName;
import org.andrewwinter.sip.parser.SipRequest;

/**
 *
 * @author andrew
 */
public class B2bUaHelperImpl implements B2buaHelper {

    /**
     * Disallowed system headers as used in createRequest().
     */
    private static final Set<HeaderName> UNCONDITIONALLY_DISALLOWED_SYSTEM_HEADERS = new HashSet<>();
    static {
        Collections.addAll(UNCONDITIONALLY_DISALLOWED_SYSTEM_HEADERS,
            HeaderName.CALL_ID,
            HeaderName.CSEQ,
            HeaderName.VIA,
            HeaderName.RECORD_ROUTE);
//            HeaderName.PATH);
    }
            
    /**
     * 
     * @param session
     * @return 
     */
    @Override
    public SipSession getLinkedSession(final SipSession session) {
        if (session == null || !session.isValid()) {
            throw new IllegalArgumentException("Invalid session.");
        }
        
        SipSession linkedSession = null;
        final String linkedSessionId = ((SipSessionImpl) session).getLinkedSessionId();
        if (linkedSessionId != null) {
            linkedSession = SipSessionStore.getInstance().get(linkedSessionId);
        }
        return linkedSession;
    }

    /**
     * 
     * @param ssr
     * @return 
     */
    @Override
    public SipServletRequest getLinkedSipServletRequest(SipServletRequest ssr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param session1
     * @param session2 
     */
    @Override
    public void linkSipSessions(final SipSession session1, final SipSession session2) {
        
        if (session1 == null || session2 == null) {
            throw new NullPointerException("Null sessions are not allowed.");
        }

        final SipSessionImpl s1 = (SipSessionImpl) session1;
        final SipSessionImpl s2 = (SipSessionImpl) session2;

        if (s1.getState() == State.TERMINATED || s2.getState() == State.TERMINATED) {
            throw new IllegalArgumentException("At least one session is terminated.");
        }
        
        if (!s1.getApplicationSession().getId().equals(s2.getApplicationSession().getId())) {
            throw new IllegalArgumentException("Sessions must belong to the same application session.");
        }
        
        if (s1.getLinkedSessionId() != null && !s1.getLinkedSessionId().equals(s2.getId())) {
            throw new IllegalArgumentException("session1 is already linked to a different session.");
        }

        if (s2.getLinkedSessionId() != null && !s2.getLinkedSessionId().equals(s1.getId())) {
            throw new IllegalArgumentException("session2 is already linked to a different session.");
        }
        
        s1.setLinkedSessionId(s2.getId());
        s2.setLinkedSessionId(s1.getId());
    }

    /**
     * 
     * @param request1
     * @param request2 
     */
    private void linkSipServletRequests(
            final SipServletRequestImpl request1,
            final SipServletRequestImpl request2) {
        
        
        // TODO: Link SipRequests
        
    }
    
    /**
     * 
     * @param ss 
     */
    @Override
    public void unlinkSipSessions(final SipSession session) {
        
        final SipSessionImpl sessionImpl = (SipSessionImpl) session;
        
        final String linkedId = sessionImpl.getLinkedSessionId();
        
        if (linkedId == null) {
            throw new IllegalArgumentException("Session not linked to another session.");
        }
        
        if (session.getState() == State.TERMINATED) {
            throw new IllegalArgumentException("Session is terminated.");
        }
        
        sessionImpl.setLinkedSessionId(null);

        final SipSessionImpl linked = SipSessionStore.getInstance().get(linkedId);
        if (linked != null) {
            linked.setLinkedSessionId(null);
        }
    }

    @Override
    public List<SipServletMessage> getPendingMessages(final SipSession session, final UAMode mode) {
        if (session == null || !session.isValid()) {
            throw new IllegalArgumentException("Invalid session.");
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SipServletResponse createResponseToOriginalRequest(
            final SipSession session,
            final int status,
            final String reasonPhrase) {
        if (session == null || !session.isValid()) {
            throw new IllegalArgumentException("Invalid session.");
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param origRequest
     * @param linked
     * @param headerMap
     * @return
     * @throws IllegalArgumentException
     * @throws TooManyHopsException 
     */
    private SipServletRequest createRequest(
            final SipServletRequest origRequest,
            final boolean linked,
            final Map<String, List<String>> headerMap,
            final boolean checkMaxForwards)
            throws IllegalArgumentException, TooManyHopsException {
        
        if (origRequest == null) {
            throw new NullPointerException("Original request cannot be null.");
        }

        if (checkMaxForwards && origRequest.getMaxForwards() == 0) {
            throw new TooManyHopsException("Max-Forwards of original request is already zero.");
        }

        for (final String headerToCopy : headerMap.keySet()) {
            final HeaderName headerName = HeaderName.fromString(headerToCopy);
            if (UNCONDITIONALLY_DISALLOWED_SYSTEM_HEADERS.contains(headerName)) {
                throw new IllegalArgumentException("Map must not contain a system header.");
            }
        }
        
        if (!origRequest.isInitial()) {
            throw new IllegalArgumentException("origRequest is not initial.");
        }
        
        final SipSessionImpl origSipSession = (SipSessionImpl) origRequest.getSession();
        final SipFactory sipFactory = (SipFactory) origSipSession
                .getServletContext().getAttribute("javax.servlet.sip.SipFactory");

        // Creates a new SipServletRequest and a new SipSession
        final SipServletRequestImpl newRequest = (SipServletRequestImpl) sipFactory.createRequest(
                origSipSession.getApplicationSession(),
                origRequest.getMethod(),
                origRequest.getFrom(),
                origRequest.getTo());

        if (linked) {
            linkSipSessions(origSipSession, newRequest.getSession());
            linkSipServletRequests((SipServletRequestImpl) origRequest, (SipServletRequestImpl) newRequest);
        }
        
        
        // TODO: Copy headers from origRequest

        
        final SipRequest newRequestAsSipRequest = newRequest.getSipRequest();
        for (Map.Entry<String, List<String>> header : headerMap.entrySet()) {
            newRequestAsSipRequest.setHeaders(HeaderName.fromString(header.getKey()), header.getValue());
        }
        
        // Otherwise, the value of the new requests Max-Forwards header is set
        // to that of the original request minus one.
        if (checkMaxForwards && origRequest.getMaxForwards() != -1) {
            newRequest.setMaxForwards(origRequest.getMaxForwards()-1);
        }
        
        return newRequest;
    }

    
    /**
     * 
     * @param origRequest
     * @param linked
     * @param headerMap
     * @return
     * @throws IllegalArgumentException
     * @throws TooManyHopsException 
     */
    @Override
    public SipServletRequest createRequest(
            final SipServletRequest origRequest,
            final boolean linked,
            final Map<String, List<String>> headerMap)
            throws IllegalArgumentException, TooManyHopsException {
        
        return createRequest(origRequest, linked, headerMap, true);
    }

    /**
     * 
     * @param origRequest
     * @return 
     */
    @Override
    public SipServletRequest createRequest(final SipServletRequest origRequest) {
        try {
            return createRequest(origRequest, false, new HashMap<String, List<String>>(), false);
        } catch (final TooManyHopsException e) {
            // Should never be thrown due to the 'false' argument above.
            return null;
        }
    }

    @Override
    public SipServletRequest createRequest(
            final SipSession session,
            final SipServletRequest origRequest,
            final Map<String, List<String>> headerMap) throws IllegalArgumentException {
        
        if (origRequest == null) {
            throw new NullPointerException("Original request cannot be null.");
        }
        
        if (session == null) {
            throw new NullPointerException("Session cannot be null.");
        }
        
        if (!origRequest.isInitial()) {
            throw new IllegalArgumentException("origRequest is not initial.");
        }

        // TODO: Do this.
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SipServletRequest createCancel(final SipSession session) {
        if (session == null) {
            throw new NullPointerException("Session cannot be null.");
        }

        // TODO: Do this
        
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
