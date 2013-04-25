package org.andrewwinter.jsr289.store;

import java.util.HashMap;
import java.util.Map;
import org.andrewwinter.jsr289.ApplicationPath;

/**
 *
 * @author andrew
 */
public class ApplicationPathStore {
    
    private static final ApplicationPathStore INSTANCE = new ApplicationPathStore();
    
    /**
     * ApplicationPath.getId() -> ApplicationPath
     */
    private final Map<String, ApplicationPath> paths;
    
    private ApplicationPathStore() {
        paths = new HashMap<>();
    }
    
    public static ApplicationPathStore getInstance() {
        return INSTANCE;
    }
    
    public void put(final ApplicationPath path) {
        paths.put(path.getId(), path);
    }
    
    public ApplicationPath get(final String id) {
        return paths.get(id);
    }
}
