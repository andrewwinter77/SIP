package org.andrewwinter.jsr289.jboss.metadata;

import java.util.EventListener;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.modules.ModuleClassLoader;

/**
 *
 * @author andrew
 */
public class SipListenerInfo {

    private final Class iface;
    
    private final String className;
    
    private EventListener instance;
    
    public SipListenerInfo(final Class<? extends EventListener> iface, final String className) {
        this.iface = iface;
        this.className = className;
    }
    
    public EventListener getInstance() {
        return instance;
    }
    
    public Class getSipListenerInterface() {
        return iface;
    }
    
    public void prepare(final ModuleClassLoader classLoader) throws Exception {
        try {
            final Class<?> clazz = classLoader.loadClass(className, true);
            instance = (EventListener) clazz.newInstance();
            
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new DeploymentUnitProcessingException(e);
        }
    }
}
