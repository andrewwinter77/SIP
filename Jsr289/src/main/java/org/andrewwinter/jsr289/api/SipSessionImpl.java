package org.andrewwinter.jsr289.api;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.sip.Address;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipSession.State;
import javax.servlet.sip.SipSessionAttributeListener;
import javax.servlet.sip.SipSessionBindingEvent;
import javax.servlet.sip.SipSessionBindingListener;
import javax.servlet.sip.SipSessionEvent;
import javax.servlet.sip.SipSessionListener;
import javax.servlet.sip.URI;
import javax.servlet.sip.ar.SipApplicationRoutingRegion;
import org.andrewwinter.jsr289.model.SipServletDelegate;
import org.andrewwinter.jsr289.threadlocal.AppNameThreadLocal;
import org.andrewwinter.jsr289.threadlocal.MainServletNameThreadLocal;
import org.andrewwinter.jsr289.threadlocal.ServletContextThreadLocal;
import org.andrewwinter.jsr289.store.SipListenerStore;
import org.andrewwinter.jsr289.store.SipServletStore;
import org.andrewwinter.jsr289.store.SipSessionStore;
import org.andrewwinter.jsr289.util.Util;
import org.andrewwinter.sip.SipRequestHandler;
import org.andrewwinter.sip.SipResponseHandler;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.dialog.DialogState;
import org.andrewwinter.sip.message.InboundSipRequest;
import org.andrewwinter.sip.message.InboundSipResponse;
import org.andrewwinter.sip.message.SipMessageFactory;
import org.andrewwinter.sip.parser.SipRequest;

/**
 *
 * @author andrewwinter77
 */
public class SipSessionImpl implements SipSession, SipRequestHandler, SipResponseHandler {

    private final String callId;
    
    private final String id;
    
    private Map<String, Object> attributes;
    
    private SipApplicationSessionImpl appSession;
    
    private final ServletContext servletContext;
    
    private boolean valid;
    
    private long creationTime;
    
    private boolean invalidateWhenReady;
    
    private SipApplicationRoutingRegion routingRegion;
    
    /**
     * State info used by app router. Not exposed to applications.
     */
    private Serializable stateInfo;

    /**
     * Determined by the app router.
     */
    private URI subscriberUri;
    
    /**
     * The name of the servlet which should handle all subsequently received
     * messages for this SipSession. The servlet must belong to the same
     * application (i.e. same ServletContext) as the caller.
     */
    private String handler;

    private final String mainServletName;
    
    /**
     * ID of linked SipSession. See B2bUaHelper for more information.
     */
    private String linkedSessionId;
    
    private long lastAccessedTime;
    
    private final String appName;
    
    private Dialog dialog;
    
    /**
     * 
     * @param appSession Null ONLY when called for inbound initial requests, in
     *   which case {@link #createForInboundInitialRequests(java.lang.String, javax.servlet.ServletContext)}
     *   should be used instead.
     * @param callId
     * @return 
     */
    public static SipSessionImpl createForInitialOutboundRequests(
            final String callId, final SipApplicationSession appSession) {
        return create(callId, null, appSession);
    }
    
    /**
     * 
     * @param callId
     * @return 
     */
    public static SipSessionImpl createForInboundInitialRequests(
            final String callId, final Dialog dialog) {
        return create(callId, dialog, null);
    }

    private static SipSessionImpl create(
            final String callId,
            final Dialog dialog,
            final SipApplicationSession appSession) {
        final SipSessionImpl ss = new SipSessionImpl(
                appSession,
                ServletContextThreadLocal.get(),
                callId,
                AppNameThreadLocal.get(),
                MainServletNameThreadLocal.get(),
                dialog);
        
        if (appSession != null) {
            ((SipApplicationSessionImpl) appSession).addSipSession(ss);
        }
        
        SipSessionStore.getInstance().put(ss);

        final Set<SipSessionListener> listeners = SipListenerStore.getInstance().get(
                AppNameThreadLocal.get(),
                SipSessionListener.class);
        
        if (listeners != null) {
            final SipSessionEvent event = new SipSessionEvent(ss);
            for (final SipSessionListener listener : listeners) {
                
                // TODO: Make listener all asynchronously, maybe using a dynamic proxy.
                listener.sessionCreated(event);
            }
        }
        
        return ss;
    }
    
    /**
     * 
     * @param appSession May be null.
     * @param callId 
     */
    private SipSessionImpl(
            final SipApplicationSession appSession,
            final ServletContext servletContext,
            final String callId,
            final String appName,
            final String mainServletName,
            final Dialog dialog) {
        
        this.id = UUID.randomUUID().toString();
        this.servletContext = servletContext;
        this.callId = callId;
        this.attributes = new HashMap<>();
        this.appSession = (SipApplicationSessionImpl) appSession;
        this.valid = true;
        this.appName = appName;
        this.creationTime = System.currentTimeMillis();
        
        // The default is true for v1.1 applications and false for v1.0
        // applications.
        this.invalidateWhenReady = true;
        
        this.dialog = dialog;
        this.mainServletName = mainServletName;
    }
    
    public void setDialog(final Dialog dialog) {
        if (this.dialog == null) {
            this.dialog = dialog;
            SipSessionStore.getInstance().putUsingDialogId(dialog.getId(), this);
        }
    }
    
    public Dialog getDialog() {
        return dialog;
    }
    
    public String getApplicationName() {
        return appName;
    }
    
    public void setLinkedSessionId(final String id) {
        this.linkedSessionId = id;
    }
    
    public String getLinkedSessionId() {
        return linkedSessionId;
    }
    
    private void handleInvalidSession() throws IllegalStateException {
        if (!isValid()) {
            throw new IllegalStateException("SIP session not valid.");
        }
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    @Override
    public void invalidate() {
        handleInvalidSession();
        valid = false;
        SipSessionStore.getInstance().remove(id);
        if (dialog != null) {
            SipSessionStore.getInstance().removeUsingDialogId(dialog.getId());
        }
        if (appSession != null) {
            appSession.removeSipSession(id);
        }
        
        // TODO: Remove any transaction state from the RFC 3261 stack.
        
        // On invalidation, the container MUST invoke the sessionDestroyed()
        // callback on implementations of the SipSessionListener interface, if
        // any exist.
        
        final Set<SipSessionListener> listeners = SipListenerStore.getInstance().get(appName, SipSessionListener.class);
        if (listeners != null) {
            final SipSessionEvent event = new SipSessionEvent(this);
            for (final SipSessionListener listener : listeners) {
                
                // TODO: Make listener all asynchronously, maybe using a dynamic proxy.
                listener.sessionDestroyed(event);
            }
        }
    }

    @Override
    public boolean isReadyToInvalidate() {
        handleInvalidSession();
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setInvalidateWhenReady(final boolean invalidateWhenReady) {
        handleInvalidSession();
        this.invalidateWhenReady = invalidateWhenReady;
    }

    @Override
    public boolean getInvalidateWhenReady() {
        handleInvalidSession();
        return invalidateWhenReady;
    }

    @Override
    public SipApplicationSession getApplicationSession() {
        if (appSession == null) {
            appSession = SipApplicationSessionImpl.create(this, appName);
        }
        return appSession;
    }

    /**
     * Non-standard method. Used internally only.
     */
    SipApplicationSession getApplicationSession(final boolean create) {
        if (create) {
            return getApplicationSession();
        } else {
            return appSession;
        }
    }
    
    @Override
    public String getCallId() {
        return callId;
    }

    @Override
    public Address getLocalParty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Address getRemoteParty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SipServletRequest createRequest(final String method) {
        
        handleInvalidSession();
        
        if ("ACK".equals(method) || "CANCEL".equals(method)) {
            throw new IllegalArgumentException("Method not allowed.");
        }
        
        if (getState() == State.TERMINATED) {
            throw new IllegalStateException("Session is already terminated.");
        }

        // TODO: IllegalStateException when there's an ongoing transaction
        
        final SipRequest request;
        if (dialog == null) {
//            request = SipMessageFactory.createOutOfDialogRequest(
//                    method,
            // TODO: Create out-of-dialog request. Need to get To, From, etc.
            // headers from somewhere.
            throw new UnsupportedOperationException("Not supported yet.");
                    
        } else {
            request = SipMessageFactory.createInDialogRequest(dialog, method);
        } 
                
        final SipServletRequestImpl servletRequest = new SipServletRequestImpl(request);
        servletRequest.setSipSession(this);
        return servletRequest;
    }

    @Override
    public void setHandler(final String name) throws ServletException {
        handleInvalidSession();
        
        // TODO: Throw ServletException if the app doesn't contain any servlet with the given name
        
        this.handler = name;
    }

    /**
     * Returns the handler or {@code null} if no handler has been set.
     * @return The name of the handler.
     */
    public String getHandler() {
        return handler;
    }
    
    @Override
    public Object getAttribute(final String name) {
        if (name == null) {
            throw new NullPointerException("Null name not allowed.");
        }
        handleInvalidSession();
        return attributes.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        handleInvalidSession();
        return Collections.enumeration(attributes.keySet());
    }

    @Override
    public void setAttribute(final String name, final Object attribute) {
        if (name == null || attribute == null) {
            throw new NullPointerException("Null name or attribute");
        }
        
        // TODO: The servlet container MAY throw an IllegalArgumentException if an object is placed into the sessions that is not Serializable or for which specific support has not been made available.
        
        handleInvalidSession();

        final SipSessionBindingEvent event = new SipSessionBindingEvent(this, name);
        
        // Some objects may require notification when they are placed into, or
        // removed from, a session. This information can be obtained by having
        // the object implement the SipSessionBindingListener interface.
        
        // The valueBound method must be called before the object is made
        // available via the getAttribute method of the SipSession interface.
        if (attribute instanceof SipSessionBindingListener) {
           // TODO: Ensure this call is asynchronous
           ((SipSessionBindingListener) attribute).valueBound(event);
        }
        
        final Object attributeBeingReplaced = attributes.put(name, attribute);
        final Set<SipSessionAttributeListener> listeners = SipListenerStore.getInstance().get(appName, SipSessionAttributeListener.class);
        if (listeners != null) {
            for (final SipSessionAttributeListener listener : listeners) {
                // TODO: Ensure these calls are asynchronous
                if (attributeBeingReplaced == null) {
                    listener.attributeAdded(event);
                } else {
                    listener.attributeReplaced(event);
                }
            }
        }
    }

    @Override
    public void removeAttribute(final String name) {
        handleInvalidSession();
        if (name != null) {
            final Object attribute = attributes.remove(name);
            if (attribute != null) {

                final SipSessionBindingEvent event = new SipSessionBindingEvent(this, name);
                    
                // The valueUnbound method must be called after the object is no
                // longer available via the getAttribute method of the
                // SipSession interface.
                if (attribute instanceof SipSessionBindingListener) {
                    // TODO: Ensure this call is asynchronous
                    ((SipSessionBindingListener) attribute).valueUnbound(event);
                }
                
                final Set<SipSessionAttributeListener> listeners = SipListenerStore.getInstance().get(appName, SipSessionAttributeListener.class);
                if (listeners != null) {
                    for (final SipSessionAttributeListener listener : listeners) {
                        // TODO: Ensure this call is asynchronous
                        listener.attributeRemoved(event);
                    }
                }
            }
        }
    }

    @Override
    /**
     * See section 6-8 of the Sip Servlet 1.1 Specification for rules for
     * state changes.
     */
    public State getState() {
        handleInvalidSession();

        if (dialog == null) {

            // Before a dialog creating request is received or sent on a SipSession,
            // the SipSession state is defined to be INITIAL.
            return State.INITIAL;
            
        } else {
            if (dialog.getState() == DialogState.EARLY) {
                return State.EARLY;
            } else if (dialog.getState() == DialogState.CONFIRMED) {
                return State.CONFIRMED;
            } else {
                // TODO: How do we know when we're in the TERMINATED state??
                return null;
            }
        }
    }

    @Override
    public void setOutboundInterface(final InetSocketAddress address) {
        handleInvalidSession();
        if (address == null) {
            throw new NullPointerException("Address must not be null.");
        }
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setOutboundInterface(final InetAddress address) {
        handleInvalidSession();
        if (address == null) {
            throw new NullPointerException("Address must not be null.");
        }

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public SipApplicationRoutingRegion getRegion() {
        handleInvalidSession();
        return routingRegion;
    }

    /**
     * 
     * @param region 
     */
    public void setRegion(final SipApplicationRoutingRegion region) {
        this.routingRegion = region;
    }
    
    @Override
    public URI getSubscriberURI() {
        handleInvalidSession();
        return subscriberUri;
    }
    
    /**
     * 
     * @param uri 
     */
    public void setSubscriberURI(final URI uri) {
        this.subscriberUri = uri;
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * 
     * @return 
     */
    public Serializable getStateInfo() {
        return stateInfo;
    }
    
    /**
     * 
     * @param stateInfo 
     */
    public void setStateInfo(final Serializable stateInfo) {
        this.stateInfo = stateInfo;
    }
    
    /**
     * For subsequent requests only!
     * @param isr 
     */
    @Override
    public void doRequest(final InboundSipRequest isr) {
        final String servletName;
        if (handler == null) {
            servletName = mainServletName;
        } else {
            servletName = handler;
        }
        
        final SipServletDelegate servlet = SipServletStore.getInstance().get(appName, servletName);
        if (servlet == null) {
            
        } else {

            final SipServletRequestImpl subsequentRequest = new SipServletRequestImpl(isr);
            subsequentRequest.setSipSession(this);
            
            try {
                org.andrewwinter.jsr289.util.Util.invokeServlet(
                        servlet,
                        subsequentRequest,
                        null,
                        servletContext,
                        appName,
                        mainServletName);
            } catch (final Exception e) {
                e.printStackTrace();

                // If a servlet throws an exception when invoked to process a
                // request other than ACK and CANCEL, the servlet container MUST
                // generate a 500 response to that request.

                if (!"CANCEL".equals(subsequentRequest.getMethod()) && !"ACK".equals(subsequentRequest.getMethod())) {

                    try {
                        // Note this could (and does sometimes) throw an exception when the servlet
                        // threw an exception due to a final response already being sent. If a final
                        // response has already been sent then clearly we won't be able to send a
                        // 500.
                        final SipServletResponse response = subsequentRequest.createResponse(SipServletResponse.SC_SERVER_INTERNAL_ERROR);

                        response.send();
                    } catch (final Exception ignore) {
                    }
                }
            }
        }
    }

    /**
     * Although SipStack invokes this method for all responses including 100s,
     * the Sip Servlet v1.1 spec says we shouldn't invoke the app for 100s.
     * @param isr 
     */
    @Override
    public void doResponse(final InboundSipResponse isr) {

        if (isr.getResponse().getStatusCode() == 100) {
            // The UAC application is invoked for all incoming responses except
            // 100 responses (and retransmissions).
            return;
        }
        
        if (dialog == null && isr.getDialog() != null) {
            // Response has just created a dialog, so update this session.
            setDialog(isr.getDialog());
        }
        
        final String servletName;
        if (handler == null) {
            servletName = mainServletName;
        } else {
            servletName = handler;
        }
        
        final SipServletDelegate servlet = SipServletStore.getInstance().get(appName, servletName);
        if (servlet == null) {
            
        } else {

            final SipServletResponseImpl response = new SipServletResponseImpl(isr);
            response.setSipSession(this);
            
            try {
                
                Util.invokeServlet(
                        servlet,
                        null,
                        response,
                        servletContext,
                        appName,
                        mainServletName);
            } catch (final Exception e) {
                e.printStackTrace();
                // TODO: What do we do here?
            }
        }
    }
}
