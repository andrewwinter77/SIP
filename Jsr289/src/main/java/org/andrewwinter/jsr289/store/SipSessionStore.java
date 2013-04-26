package org.andrewwinter.jsr289.store;

import org.andrewwinter.jsr289.api.SipSessionImpl;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author andrew
 */
public class SipSessionStore {
    
    private static final SipSessionStore INSTANCE = new SipSessionStore();
    
    /**
     * SipSession ID to SipSession.
     */
    private final Map<String, SipSessionImpl> map;

    private SipSessionStore() {
        map = new HashMap<>();
    }
    
    public static SipSessionStore getInstance() {
        return INSTANCE;
    }
    
    public void put(final SipSessionImpl sipSession) {
        map.put(sipSession.getId(), sipSession);
    }
    
    public SipSessionImpl get(final String id) {
        return map.get(id);
    }
    
    public void remove(final String id) {
        map.remove(id);
    }
}
