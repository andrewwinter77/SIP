/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.sip.gui;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author andrew
 */
public class WindowStore {
    
    private static WindowStore INSTANCE = new WindowStore();
    
    private final Map<String, CallWindow> callWindowMap;

    private final Map<String, AlertWindow> alertWindowMap;
    
    private WindowStore() {
        callWindowMap = new HashMap<String, CallWindow>();
        alertWindowMap = new HashMap<String, AlertWindow>();
    }
    
    /**
     *
     * @return
     */
    public static WindowStore getInstance() {
        return INSTANCE;
    }
    
    /**
     *
     * @param callId
     * @param window
     */
    public void put(final String callId, final CallWindow window) {
        callWindowMap.put(callId, window);
    }

    /**
     *
     * @param callId
     * @param window
     */
    public void put(final String callId, final AlertWindow window) {
        alertWindowMap.put(callId, window);
    }
    
    /**
     *
     * @param callId
     * @return
     */
    public CallWindow getCallWindow(final String callId) {
        return callWindowMap.get(callId);
    }

    /**
     *
     * @param callId
     * @return
     */
    public AlertWindow getAlertWindow(final String callId) {
        return alertWindowMap.get(callId);
    }
    
    /**
     *
     * @param callId
     */
    public void removeAlertWindow(final String callId) {
        alertWindowMap.remove(callId);
    }

    /**
     *
     * @param callId
     */
    public void removeCallWindow(final String callId) {
        callWindowMap.remove(callId);
    }
}
