package org.andrewwinter.jsr289.api;

import java.util.Enumeration;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import javax.servlet.sip.ConvergedHttpSession;
import javax.servlet.sip.SipApplicationSession;

/**
 *
 * @author andrew
 */
public class ConvergedHttpSessionImpl implements ConvergedHttpSession {
    
    private final HttpSession session;
    
    private SipApplicationSession appSession;
    
    public ConvergedHttpSessionImpl(final HttpSession session, final SipApplicationSession appSession) {
        this.session = session;
        this.appSession = appSession;
    }

    @Override
    public long getCreationTime() {
        return session.getCreationTime();
    }

    @Override
    public String getId() {
        return session.getId();
    }

    @Override
    public long getLastAccessedTime() {
        return session.getLastAccessedTime();
    }

    @Override
    public ServletContext getServletContext() {
        return session.getServletContext();
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        session.setMaxInactiveInterval(interval);
    }

    @Override
    public int getMaxInactiveInterval() {
        return session.getMaxInactiveInterval();
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return session.getSessionContext();
    }

    @Override
    public Object getAttribute(String name) {
        return session.getAttribute(name);
    }

    @Override
    public Object getValue(String name) {
        return session.getValue(name);
    }

    @Override
    public Enumeration getAttributeNames() {
        return session.getAttributeNames();
    }

    @Override
    public String[] getValueNames() {
        return session.getValueNames();
    }

    @Override
    public void setAttribute(String name, Object value) {
        session.setAttribute(name, value);
    }

    @Override
    public void putValue(String name, Object value) {
        session.putValue(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        session.removeAttribute(name);
    }

    @Override
    public void removeValue(String name) {
        session.removeValue(name);
    }

    @Override
    public void invalidate() {
        final String id = session.getId();
        session.invalidate();
        if (appSession != null) {
            ((SipApplicationSessionImpl) appSession).removeHttpSession(id);
        }
    }

    @Override
    public boolean isNew() {
        return session.isNew();
    }

    @Override
    public SipApplicationSession getApplicationSession() {
        if (appSession == null) {
            appSession = SipApplicationSessionImpl.create(this,
                    
                    // TODO: Set a proper app-name
                    "TODO: SET APP-NAME HERE");
            
            
        }
        return appSession;
    }

    private String encodeWithJSessionId(final String url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String encodeURL(final String url) {
        return encodeWithJSessionId(url);
    }

    @Override
    public String encodeURL(final String relativePath, final String scheme) {
        //session.getServletContext().getContextPath()
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
