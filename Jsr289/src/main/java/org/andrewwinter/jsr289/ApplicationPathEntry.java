package org.andrewwinter.jsr289;

import org.andrewwinter.jsr289.api.SipServletRequestImpl;

/**
 *
 * @author andrew
 */
public class ApplicationPathEntry {
    
    private final String appName;
    
    private final SipServletRequestImpl request;
    
    public ApplicationPathEntry(final String appName, final SipServletRequestImpl request) {
        this.appName = appName;
        this.request = request;
    }
}
