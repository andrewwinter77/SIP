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
    
    private final Map<DialogId, ApplicationPath> paths;
    
    private ApplicationPathStore() {
        paths = new HashMap<>();
    }
    
    public static ApplicationPathStore getInstance() {
        return INSTANCE;
    }
}
