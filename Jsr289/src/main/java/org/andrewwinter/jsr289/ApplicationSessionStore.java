package org.andrewwinter.jsr289;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.sip.SipApplicationSession;

/**
 *
 * @author andrew
 */
public class ApplicationSessionStore {
    
    private static final ApplicationSessionStore INSTANCE = new ApplicationSessionStore();
    
    private final Map<String, SipApplicationSession> idMap;

    private final Map<String, SipApplicationSession> keyMap;
    
    private ApplicationSessionStore() {
        idMap = new HashMap<>();
        keyMap = new HashMap<>();
    }
    
    public static ApplicationSessionStore getInstance() {
        return INSTANCE;
    }
    
    public void put(final SipApplicationSession appSession) {
        idMap.put(appSession.getId(), appSession);
    }
    
    public void putUsingKey(final String key, final SipApplicationSession appSession) {
        keyMap.put(key, appSession);
    }
    
    public SipApplicationSession get(final String id) {
        return idMap.get(id);
    }
    
    public SipApplicationSession getUsingKey(final String key) {
        return keyMap.get(key);
    }
    
    public void remove(final String id) {
        idMap.remove(id);
    }
    
    // TODO: removeUsingKey()?
}
