package org.andrewwinter.jsr289;

import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipSessionsUtil;

/**
 *
 * @author andrew
 */
public class SipSessionsUtilImpl implements SipSessionsUtil {

    private final String appName;
    
    public SipSessionsUtilImpl(final String appName) {
        this.appName = appName;
    }
    
    @Override
    public SipApplicationSession getApplicationSessionById(final String applicationSessionId) {
        if (applicationSessionId == null) {
            throw new NullPointerException("ID must not be null.");
        }
        return ApplicationSessionStore.getInstance().get(applicationSessionId);
    }

    @Override
    public SipApplicationSession getApplicationSessionByKey(final String applicationSessionKey, final boolean create) {
        if (applicationSessionKey == null) {
            throw new NullPointerException("Key must not be null.");
        }
        SipApplicationSession session =
                ApplicationSessionStore.getInstance().getUsingKey(applicationSessionKey);
        if (session == null && create) {
            session = SipApplicationSessionImpl.create(appName);
            ApplicationSessionStore.getInstance().putUsingKey(applicationSessionKey, session);
        }
        return session;
    }

    @Override
    public SipSession getCorrespondingSipSession(
            final SipSession session,
            final String headerName) {
        
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
