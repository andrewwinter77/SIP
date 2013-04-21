package org.andrewwinter.jsr289.store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.andrewwinter.jsr289.ApplicationPathEntry;
import org.andrewwinter.sip.dialog.DialogId;

/**
 *
 * @author andrew
 */
public class ApplicationPathStore {
    
    private static final ApplicationPathStore INSTANCE = new ApplicationPathStore();
    
    private final Map<DialogId, List<ApplicationPathEntry>> paths;
    
    private ApplicationPathStore() {
        paths = new HashMap<>();
    }
    
    public static ApplicationPathStore getInstance() {
        return INSTANCE;
    }
}
