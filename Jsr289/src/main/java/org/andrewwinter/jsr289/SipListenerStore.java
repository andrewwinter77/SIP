package org.andrewwinter.jsr289;

import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author andrew
 */
public class SipListenerStore {
    
    private static final SipListenerStore INSTANCE = new SipListenerStore();
    
    /**
     * Map from
     *  [app-name -> [iface -> Set<EventListener>]]
     */
    private Map<String, Map<Class, Set<EventListener>>> map;
    
    private SipListenerStore() {
        map = new HashMap<>();
    }
    
    public static SipListenerStore getInstance() {
        return INSTANCE;
    }
    
    public void put(final String appName, final Class<? extends EventListener> iface, final EventListener listener) {
        Map<Class, Set<EventListener>> listenerIfaceToInstanceMap = map.get(appName);
        if (listenerIfaceToInstanceMap == null) {
            listenerIfaceToInstanceMap = new HashMap<>();
            map.put(appName, listenerIfaceToInstanceMap);
        }
        Set<EventListener> listenerSet = listenerIfaceToInstanceMap.get(iface);
        if (listenerSet == null) {
            listenerSet = new HashSet<>();
            listenerIfaceToInstanceMap.put(iface, listenerSet);
        }
        listenerSet.add(listener);
    }
    
    public <T extends EventListener> Set<T> get(final String appName, final Class<T> iface) {
        final Map<Class, Set<EventListener>> listenerIfaceToInstanceMap = map.get(appName);
        if (listenerIfaceToInstanceMap == null) {
            return null;
        } else {
            final Set<EventListener> listeners = listenerIfaceToInstanceMap.get(iface);
            if (listeners == null || listeners.isEmpty()) {
                return null;
            } else {
                final Set<T> result = new HashSet<>();
                for (final EventListener listener : listeners) {
                    result.add((T) listener);
                }
                return result;
            }
        }
    }
}
