package org.andrewwinter.jsr289.store;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.sip.SipServlet;
import org.andrewwinter.jsr289.model.SipServletDelegate;

/**
 *
 * @author andrew
 */
public class SipServletStore {
    
    private static final SipServletStore INSTANCE = new SipServletStore();
    
    /**
     * Map from
     *  [app-name -> [servlet-name -> SipServlet]]
     */
    private Map<String, Map<String, SipServletDelegate>> map;
    
    private SipServletStore() {
        map = new HashMap<>();
    }
    
    public static SipServletStore getInstance() {
        return INSTANCE;
    }
    
    public void put(final String appName, final String servletName, final SipServletDelegate servlet) {
        
        if (appName == null || servletName == null || servlet == null) {
            throw new IllegalArgumentException("Tried to put servlet using null app name, servlet name or servlet.");
        }
        
        Map<String, SipServletDelegate> servletNameToServletMap = map.get(appName);
        if (servletNameToServletMap == null) {
            servletNameToServletMap = new HashMap<>();
            map.put(appName, servletNameToServletMap);
        }
        servletNameToServletMap.put(servletName, servlet);
    }
    
    public SipServletDelegate get(final String appName, final String servletName) {
        
        if (appName == null || servletName == null) {
            throw new IllegalArgumentException("Tried to get servlet using null app name or servlet name.");
        }
        
        final Map<String, SipServletDelegate> servletNameToServletMap = map.get(appName);
        if (servletNameToServletMap == null) {
            return null;
        } else {
            return servletNameToServletMap.get(servletName);
        }
    }
}
