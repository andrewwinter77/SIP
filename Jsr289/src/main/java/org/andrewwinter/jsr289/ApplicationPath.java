package org.andrewwinter.jsr289;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.andrewwinter.jsr289.api.SipServletRequestImpl;

/**
 *
 * @author andrew
 */
public class ApplicationPath {
    
    private final List<SipServletRequestImpl> requests;
    
    private final String id;
    
    public ApplicationPath() {
        requests = new ArrayList<>();
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }
    
    public void add(final SipServletRequestImpl request) {
        requests.add(request);
    }
    
    public SipServletRequestImpl getLastRequest() {
        return requests.get(requests.size()-1);
    }
}
