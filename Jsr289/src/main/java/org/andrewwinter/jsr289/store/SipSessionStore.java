package org.andrewwinter.jsr289.store;

import org.andrewwinter.jsr289.api.SipSessionImpl;
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

    private final Map<DialogId, SipSessionImpl> dialogIdToSessionMap;
    
    private SipSessionStore() {
        map = new HashMap<>();
        dialogIdToSessionMap = new HashMap<>();
    }
    
    public static SipSessionStore getInstance() {
        return INSTANCE;
    }
    
    public void associateWithDialogId(final DialogId id, final SipSessionImpl session) {
        dialogIdToSessionMap.put(id, session);
    }
    
    public SipSessionImpl getFromDialogId(final DialogId id) {
        return dialogIdToSessionMap.get(id);
    }
    
    public void put(final SipSessionImpl sipSession) {
        map.put(sipSession.getId(), sipSession);
        System.out.println("SipSession count: " + map.keySet().size());
    }
    
    public SipSessionImpl get(final String id) {
        return map.get(id);
    }
    
    public void remove(final String id) {
        final SipSessionImpl session = map.remove(id);
        if (session.getDialog() != null) {
            dialogIdToSessionMap.remove(session.getDialog().getId());
        }
    }
}
