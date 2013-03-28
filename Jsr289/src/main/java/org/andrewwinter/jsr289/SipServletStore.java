package org.andrewwinter.jsr289;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.sip.SipServlet;

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
    private Map<String, Map<String, SipServlet>> map;
    
    private SipServletStore() {
        map = new HashMap<>();
    }
    
    public static SipServletStore getInstance() {
        return INSTANCE;
    }
    
    public void put(final String appName, final String servletName, final SipServlet servlet) {
        Map<String, SipServlet> servletNameToServletMap = map.get(appName);
        if (servletNameToServletMap == null) {
            servletNameToServletMap = new HashMap<>();
            map.put(appName, servletNameToServletMap);
        }
        servletNameToServletMap.put(servletName, servlet);
    }
    
    public SipServlet get(final String appName, final String servletName) {
        final Map<String, SipServlet> servletNameToServletMap = map.get(appName);
        if (servletNameToServletMap == null) {
            return null;
        } else {
            return servletNameToServletMap.get(servletName);
        }
    }
}
