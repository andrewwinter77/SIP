package org.andrewwinter.jsr289.api;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.servlet.http.HttpSession;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipApplicationSessionAttributeListener;
import javax.servlet.sip.SipApplicationSessionBindingEvent;
import javax.servlet.sip.SipApplicationSessionBindingListener;
import javax.servlet.sip.SipApplicationSessionEvent;
import javax.servlet.sip.SipApplicationSessionListener;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.URI;
import org.andrewwinter.jsr289.store.ApplicationSessionStore;
import org.andrewwinter.jsr289.store.SipListenerStore;
import org.andrewwinter.jsr289.store.SipSessionStore;

/**
 *
 * @author andrewwinter77
 */
public class SipApplicationSessionImpl implements SipApplicationSession {

    private final String id;
    
    private final Map<String, Object> attributes;
    
    private boolean invalidateWhenReady;
    
    private final Map<String, SipSession> sipSessions;
    
    private final Map<String, HttpSession> httpSessions;
    
    private final long creationTime;
    
    private boolean valid;
    
    private long lastAccessedTime;

    private final String appName;
    
    public static SipApplicationSessionImpl create(final String appName) {
        final SipApplicationSessionImpl appSession = new SipApplicationSessionImpl(appName);
        ApplicationSessionStore.getInstance().put(appSession);
        
        final Set<SipApplicationSessionListener> listeners = SipListenerStore.getInstance().get(appName, SipApplicationSessionListener.class);
        if (listeners != null) {
            final SipApplicationSessionEvent event = new SipApplicationSessionEvent(appSession);
            for (final SipApplicationSessionListener listener : listeners) {
                
                // TODO: Make listener all asynchronously, maybe using a dynamic proxy.
                listener.sessionCreated(event);
            }
        }

        return appSession;
    }

    public static SipApplicationSessionImpl create(final SipSession session, final String appName) {
        final SipApplicationSessionImpl appSession = create(appName);
        if (session != null) {
            appSession.addSipSession(session);
        }
        return appSession;
    }
    
    public static SipApplicationSessionImpl create(final HttpSession session, final String appName) {
        final SipApplicationSessionImpl appSession = create(appName);
        if (session != null) {
            appSession.addHttpSession(session);
        }
        return appSession;
    }

    private SipApplicationSessionImpl(final String appName) {
        id = UUID.randomUUID().toString();
        attributes = new HashMap<>();
        
        // The default is true for v1.1 applications and false for v1.0
        // applications.
        invalidateWhenReady = true;
        
        sipSessions = new HashMap<>();
        httpSessions = new HashMap<>();
        
        creationTime = System.currentTimeMillis();
        
        valid = true;
        
        this.appName = appName;
    }

    private void handleInvalidSession() throws IllegalStateException {
        if (!isValid()) {
            throw new IllegalStateException("Application session not valid.");
        }
    }

    @Override
    public long getCreationTime() {
        handleInvalidSession();
        return creationTime;
    }

    @Override
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    @Override
    public long getExpirationTime() {
        handleInvalidSession();
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getApplicationName() {
        return appName;
    }

    @Override
    public int setExpires(final int deltaMinutes) {
        handleInvalidSession();
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void invalidate() {
        handleInvalidSession();
        
        for (final HttpSession session : httpSessions.values()) {
            session.invalidate();
        }
        for (final SipSession session : sipSessions.values()) {
            session.invalidate();
        }
        
        valid = false;
        
        // TODO: Cancel any timers associated with this application session.
        
        ApplicationSessionStore.getInstance().remove(id);
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
    public Iterator<?> getSessions() {
        handleInvalidSession();
        final List<Object> sessions = new ArrayList<>();
        sessions.addAll(httpSessions.values());
        sessions.addAll(sipSessions.values());
        return sessions.iterator();
    }

    @Override
    public Iterator<?> getSessions(final String protocol) {
        handleInvalidSession();
        if (protocol == null) {
            throw new NullPointerException("Protocol must be non-null.");
        }
        switch (protocol) {
            case "SIP":
                return sipSessions.values().iterator();
            case "HTTP":
                return httpSessions.values().iterator();
            default:
                throw new IllegalArgumentException("Unrecognised protocol '" + protocol + '"');
        }
    }

    @Override
    public SipSession getSipSession(final String id) {
        handleInvalidSession();
        if (id == null) {
            throw new NullPointerException("Null ID.");
        }
        
        final SipSession session = SipSessionStore.getInstance().get(id);
        if (session == null) {
            return null;
        } else {
            if (session.isValid()) {
                return session;
            } else {
                throw new IllegalStateException("Invalid SipSession.");
            }
        }
    }

    private HttpSession getHttpSession(final String id) {
        return httpSessions.get(id);
    }
    
    @Override
    public Object getSession(final String id, final Protocol protocol) {
        handleInvalidSession();
        if (id == null || protocol == null) {
            throw new NullPointerException("Null ID or protocol.");
        }
        
        if (protocol == Protocol.SIP) {
            return getSipSession(id);
        } else {
            return getHttpSession(id);
        }
    }
    
    void addSipSession(final SipSession sipSession) {
        sipSessions.put(sipSession.getId(), sipSession);
    }
    
    void addHttpSession(final HttpSession httpSession) {
        httpSessions.put(httpSession.getId(), httpSession);
    }

    /**
     * The caller will take care of invalidating the SipSession and removing
     * its state.
     * @param id ID of the SipSession.
     */
    void removeSipSession(final String id) {
        sipSessions.remove(id);
    }
    
    /**
     * The caller will take care of invalidating the SipSession and removing
     * its state.
     * @param id ID of the SipSession.
     */
    void removeHttpSession(final String id) {
        httpSessions.remove(id);
    }

    @Override
    public void encodeURI(final URI uri) {
        handleInvalidSession();
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getAttribute(final String name) {
        handleInvalidSession();
        return attributes.get(name);
    }

    @Override
    public Iterator<String> getAttributeNames() {
        handleInvalidSession();
        return Collections.unmodifiableSet(attributes.keySet()).iterator();
    }

    @Override
    public void setAttribute(final String name, final Object attribute) {
        if (name == null || attribute == null) {
            throw new NullPointerException("Null name or attribute");
        }
        
        // TODO: The servlet container MAY throw an IllegalArgumentException if an object is placed into the sessions that is not Serializable or for which specific support has not been made available.
        
        handleInvalidSession();

        final SipApplicationSessionBindingEvent event = new SipApplicationSessionBindingEvent(this, name);

        // Some objects may require notification when they are placed into, or
        // removed from, an application session. This information can be
        // obtained by having the object implement the
        // SipApplicationSessionBindingListener interface.

        // The valueBound method must be called before the object is made
        // available via the getAttribute method of the SipApplicationSession
        // interface. 
        if (attribute instanceof SipApplicationSessionBindingListener) {
           // TODO: Ensure this call is asynchronous
           ((SipApplicationSessionBindingListener) attribute).valueBound(event);
        }
        
        final Object attributeBeingReplaced = attributes.put(name, attribute);
        final Set<SipApplicationSessionAttributeListener> listeners = SipListenerStore.getInstance().get(appName, SipApplicationSessionAttributeListener.class);
        if (listeners != null) {
            for (final SipApplicationSessionAttributeListener listener : listeners) {
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
                
                final SipApplicationSessionBindingEvent event = new SipApplicationSessionBindingEvent(this, name);

                // The valueUnbound method must be called after the object is no
                // longer available via the getAttribute method of the
                // SipApplicationSession interface.
                if (attribute instanceof SipApplicationSessionBindingListener) {
                    // TODO: Ensure this call is asynchronous
                    ((SipApplicationSessionBindingListener) attribute).valueUnbound(event);
                }
                
                final Set<SipApplicationSessionAttributeListener> listeners = SipListenerStore.getInstance().get(appName, SipApplicationSessionAttributeListener.class);
                if (listeners != null) {
                    for (final SipApplicationSessionAttributeListener listener : listeners) {
                        // TODO: Ensure this call is asynchronous
                        listener.attributeRemoved(event);
                    }
                }
            }
        }
    }

    @Override
    public Collection<ServletTimer> getTimers() {
        handleInvalidSession();
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ServletTimer getTimer(final String id) {
        handleInvalidSession();
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public URL encodeURL(final URL url) {
        handleInvalidSession();
        
        final String param = SipApplicationSessionImpl.class.getName() + "=" + id;
        
        String str = url.toString();
        if (str.indexOf('?') == -1) {
            str += "?" + param;
        } else {
            str += "&" + param;
        }
        
        try {
            return new URL(str);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return url;
        }
    }
}
