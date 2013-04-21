package org.andrewwinter.jsr289;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrew
 */
public class ApplicationPath {
    
    private final List<ApplicationPathEntry> entries;
    
    public ApplicationPath() {
        entries = new ArrayList<>();
    }
    
    public void add(final ApplicationPathEntry entry) {
        entries.add(entry);
    }
}
