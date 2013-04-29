package org.andrewwinter.jsr289.model;

import java.util.EventListener;

/**
 *
 * @author andrew
 */
public class SipListenerInfo {

    /**
     * 
     */
    private final Class iface;
    
    /**
     * 
     */
    private final String className;
    
    /**
     * 
     */
    private EventListener instance;

    /**
     *
     * @param iface
     * @param className
     */
    public SipListenerInfo(final Class<? extends EventListener> iface, final String className) {
        this.iface = iface;
        this.className = className;
    }

    /**
     *
     * @return
     */
    public EventListener getInstance() {
        return instance;
    }

    /**
     *
     * @return
     */
    public Class getSipListenerInterface() {
        return iface;
    }

    /**
     *
     * @param classLoader
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void init(final ClassLoader classLoader) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        final Class<?> clazz = classLoader.loadClass(className);
        instance = (EventListener) clazz.newInstance();
    }
}
