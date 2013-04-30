package org.andrewwinter.jsr289.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import org.andrewwinter.jsr289.InboundSipServletRequestHandler;
import org.andrewwinter.jsr289.ServletContextDelegate;
import org.andrewwinter.jsr289.util.ManagedClassInstantiator;
import org.andrewwinter.jsr289.util.Util;

/**
 *
 * @author andrew
 */
public class SipDeploymentUnit {

    /**
     * 
     */
    private final Map<String, SipServletDelegate> sipServlets;
    
    /**
     * 
     */
    private final List<SipApplicationInfo> sipApplicationMetadataList;
    
    /**
     * 
     */
    private final Map<String, String> contextParams;
    
    /**
     * 
     */
    private ClassLoader classLoader;
    
    /**
     * 
     */
    private ServletContext servletContext;
    
    /**
     * 
     */
    private String mainServletName;
    
    /**
     * Map from one of the SipListener interfaces (say, SipApplicationSessionListener)
     * to a set of implementations of that interface.
     */
    private final Map<Class<? extends EventListener>, Set<SipListenerInfo>> sipListeners;
    
    /**
     *
     */
    public SipDeploymentUnit() {
        sipServlets = new HashMap<>();
        sipApplicationMetadataList = new ArrayList<>();
        contextParams = new HashMap<>();
        sipListeners = new HashMap<>();
    }
    
    /**
     *
     * @return
     */
    public Collection<SipServletDelegate> getSipServlets() {
        return Collections.unmodifiableCollection((Collection<SipServletDelegate>) sipServlets.values());
    }
    
    /**
     *
     * @param classLoader
     */
    public void setClassLoader(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
    /**
     *
     * @return
     */
    public ClassLoader getClassLoader() {
        return classLoader;
    }
    
    /**
     *
     * @return
     */
    public String getAppName() {
        return sipApplicationMetadataList.get(0).getAppName();
    }
    
    /**
     * 
     * @return 
     */
    public String getMainServletName() {
        for (final SipApplicationInfo sam : sipApplicationMetadataList) {
            if (sam.getMainServlet() != null) {
                return sam.getMainServlet();
            }
        }
        for (final SipServletDelegate ssi : sipServlets.values()) {
            return ssi.getName();
        }
        return null;
    }
    
    /**
     * 
     * @param name
     * @param value 
     */
    public void addContextParam(final String name, final String value) {
        final String origValue = contextParams.get(name);
        if (origValue != null && !origValue.equals(value)) {
            System.out.println("Context param '" + name + "' is being redefined.");
        }
        contextParams.put(name, value);
    }
    
    /**
     * 
     * @param sli 
     */
    public void add(final SipListenerInfo sli) {
        Set<SipListenerInfo> listeners = sipListeners.get(sli.getSipListenerInterface());
        if (listeners == null) {
            listeners = new HashSet<>();
            sipListeners.put(sli.getSipListenerInterface(), listeners);
        }
        listeners.add(sli);
    }
    
    /**
     * 
     * @param ssi 
     */
    public void add(final SipServletDelegate ssi) {
        sipServlets.put(ssi.getName(), ssi);
    }
    
    /**
     * 
     * @param metadata 
     */
    public void add(final SipApplicationInfo metadata) {
        sipApplicationMetadataList.add(metadata);
    }

    /**
     * 
     * @param name
     * @return 
     */
    public SipServletDelegate getServlet(final String name) {
        return sipServlets.get(name);
    }
    
    /**
     * 
     * @return 
     */
    public SipServletDelegate getMainServlet() {
        return getServlet(mainServletName);
    }
    
    /**
     * 
     * @param <T>
     * @param iface
     * @return 
     */
    public <T extends EventListener> Set<SipListenerInfo> getSipListeners(final Class<T> iface) {
        return sipListeners.get(iface);
    }
    
    /**
     * 
     * @return 
     */
    public ServletContext getServletContext() {
        return servletContext;
    }
    
    /**
     * 
     * @param instantiator 
     */
    private void initServlets(final ManagedClassInstantiator instantiator) {
        for (final SipServletDelegate servlet : sipServlets.values()) {
            servlet.setClassLoader(classLoader);
            servlet.setInstantiator(instantiator);
            servlet.setServletContext(servletContext);
        }
    }
    
    /**
     * 
     * @param <T>
     * @param iface
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException 
     */
    private <T extends EventListener> void initListeners(final Class<T> iface) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        final Set<SipListenerInfo> listeners = sipListeners.get(iface);
        if (listeners != null) {
            for (final SipListenerInfo listener : listeners) {
                listener.init(classLoader);
            }
        }
    }
    
    /**
     * Prepares this SipModule for use. This must be called before the module
     * is used.
     * @param handler 
     * @param instantiator 
     * @param context
     * @throws IllegalStateException
     * @throws ClassNotFoundException 
     * @throws InstantiationException
     * @throws IllegalAccessException  
     */
    public void init(
            final InboundSipServletRequestHandler handler,
            final ManagedClassInstantiator instantiator,
            final ServletContext context) 
                throws IllegalStateException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        
        final ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        
        servletContext = new ServletContextDelegate(context, handler, this);
        
        if ((mainServletName = getMainServletName()) == null) {
            throw new IllegalStateException("Unable to determine main servlet name");
        }

        try {
            
            initServlets(instantiator);
            
            for (final Class iface : Util.LISTENER_CLASSES) {
                initListeners(iface);
            }
            
        } catch (ClassNotFoundException | IllegalStateException | InstantiationException | IllegalAccessException e) {
            throw e;
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
    }
}
