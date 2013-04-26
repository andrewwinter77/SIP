package org.andrewwinter.jsr289.store;

import java.util.HashMap;
import java.util.Map;
import org.andrewwinter.jsr289.ApplicationPath;
import org.andrewwinter.sip.dialog.DialogId;

/**
 *
 * @author andrew
 */
public class ApplicationPathStore {
    
    private static final ApplicationPathStore INSTANCE = new ApplicationPathStore();
    
    /**
     * ApplicationPath.getId() -> ApplicationPath
     */
    private final Map<String, ApplicationPath> pathIdToPath;
    
    private final Map<DialogId, ApplicationPath> dialogIdToPath;
    
    private ApplicationPathStore() {
        pathIdToPath = new HashMap<>();
        dialogIdToPath = new HashMap<>();
    }
    
    public static ApplicationPathStore getInstance() {
        return INSTANCE;
    }
    
    public void put(final ApplicationPath path) {
        pathIdToPath.put(path.getId(), path);
    }
    
    public void put(final DialogId id, final ApplicationPath path) {
        if (path == null) {
            throw new IllegalArgumentException("Null path");
        }
        System.out.println("Storing path " + id + " -> " + path);
        dialogIdToPath.put(id, path);
    }
    
    public ApplicationPath get(final String pathId) {
        return pathIdToPath.get(pathId);
    }
    
    public ApplicationPath get(final DialogId dialogId) {
        System.out.println("Getting path " + dialogId + " -> " + dialogIdToPath.get(dialogId));
        return dialogIdToPath.get(dialogId);
    }
}
