package org.andrewwinter.sip.dialog;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author andrewwinter77
 */
public class DialogStore {
 
    private final Map<DialogId, Dialog> dialogMap;
    
    private static DialogStore INSTANCE = new DialogStore();
    
    /**
     *
     * @return
     */
    public static DialogStore getInstance() {
        return INSTANCE;
    }
    
    /**
     * 
     */
    private DialogStore() {
        dialogMap = new HashMap<DialogId, Dialog>();
    }

    /**
     * 
     * @param dialog 
     */
    public void put(final Dialog dialog) {
        dialogMap.put(dialog.getId(), dialog);
    }

    /**
     * 
     * @param id
     * @return 
     */
    public Dialog get(final DialogId id) {
        return dialogMap.get(id);
    }
}
