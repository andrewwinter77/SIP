package org.andrewwinter.jsr289;

import java.util.HashMap;
import java.util.Map;
import org.andrewwinter.sip.dialog.DialogId;

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

    /**
     * SIP Dialog ID to SipSession.
     */
    private final Map<DialogId, SipSessionImpl> dialogIdToSessionMap;
    
    private SipSessionStore() {
        map = new HashMap<>();
        dialogIdToSessionMap = new HashMap<>();
    }
    
    public static SipSessionStore getInstance() {
        return INSTANCE;
    }
    
    public void put(final SipSessionImpl sipSession) {
        map.put(sipSession.getId(), sipSession);
    }
    
    public void putUsingDialogId(final DialogId id, final SipSessionImpl session) {
        dialogIdToSessionMap.put(id, session);
    }
    
    public SipSessionImpl get(final String id) {
        return map.get(id);
    }
    
    public SipSessionImpl getUsingDialogId(final DialogId id) {
        return dialogIdToSessionMap.get(id);
    }
    
    public void remove(final String id) {
        map.remove(id);
    }
    
    public void removeUsingDialogId(final DialogId id) {
        dialogIdToSessionMap.remove(id);
    }
}
