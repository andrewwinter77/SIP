package org.andrewwinter.jsr289.jboss.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.sip.SipServlet;
import org.andrewwinter.jsr289.util.ManagedClassInstantiator;
import org.andrewwinter.jsr289.jboss.ServletContextDelegate;
import org.andrewwinter.jsr289.jboss.SipServletService;
import org.apache.catalina.core.StandardContext;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.modules.ModuleClassLoader;

/**
 *
 * @author andrew
 */
public class SipModuleInfo {

    private final Map<String, SipServletInfo> sipServlets;
    
    private final List<SipApplicationInfo> sipApplicationMetadataList;
    
    private final Map<String, String> contextParams;
    
    private ModuleClassLoader classLoader;
    
    private StandardContext context;
    
    private ServletContext servletContext;
    
    private String mainServletName;
    
    /**
     * Map from one of the SipListener interfaces (say, SipApplicationSessionListener)
     * to a set of implementations of that interface.
     */
    private final Map<Class<? extends EventListener>, Set<SipListenerInfo>> sipListeners;
    
    public SipModuleInfo() {
        sipServlets = new HashMap<>();
        sipApplicationMetadataList = new ArrayList<>();
        contextParams = new HashMap<>();
        sipListeners = new HashMap<>();
    }
    
//    public void addSipListener(final Class iface, final Class impl) {
//        Set<Class> impls = sipListeners.get(iface);
//        if (impls == null) {
//            impls = new HashSet<>();
//            sipListeners.put(iface, impls);
//        }
//        impls.add(impl);
//    }
    
    public List<SipServlet> getSipServlets() {
        final List<SipServlet> list = new ArrayList<>();
        for (final SipServletInfo ssi : sipServlets.values()) {
            list.add(ssi.getServlet());
        }
        return Collections.unmodifiableList(list);
    }
    
    /**
     * 
     * @param iface
     * @return {@code null} if no classes implement the given interface.
     */
//    public Set<Class> getSipListeners(final Class iface) {
//        return sipListeners.get(iface);
//    }
    
    public void setStandardContext(final StandardContext context) {
        this.context = context;
    }
    
    public void setClassLoader(final ModuleClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
    public ModuleClassLoader getClassLoader() {
        return classLoader;
    }
    
    public String getAppName() {
        return sipApplicationMetadataList.get(0).getAppName();
    }
    
    public String getMainServletName() {
        for (final SipApplicationInfo sam : sipApplicationMetadataList) {
            if (sam.getMainServlet() != null) {
                return sam.getMainServlet();
            }
        }
        for (final SipServletInfo ssi : sipServlets.values()) {
            return ssi.getName();
        }
        return null;
    }
    
    public void addContextParam(final String name, final String value) {
        if (contextParams.containsKey(name)) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!! Context param already set.");
        }
        contextParams.put(name, value);
    }
    
    public void add(final SipListenerInfo sli) {
        Set<SipListenerInfo> listeners = sipListeners.get(sli.getSipListenerInterface());
        if (listeners == null) {
            listeners = new HashSet<>();
            sipListeners.put(sli.getSipListenerInterface(), listeners);
        }
        listeners.add(sli);
    }
    
    public void add(final SipServletInfo ssi) {
        sipServlets.put(ssi.getName(), ssi);
    }
    
    public void add(final SipApplicationInfo metadata) {
        sipApplicationMetadataList.add(metadata);
    }

    public SipServlet getServlet(final String name) {
        return sipServlets.get(name).getServlet();
    }
    
    public SipServlet getMainServlet() {
        return getServlet(mainServletName);
    }
    
    public <T extends EventListener> Set<SipListenerInfo> getSipListeners(final Class<T> iface) {
        return sipListeners.get(iface);
    }
    
    public ServletContext getServletContext() {
        return servletContext;
    }
    
    private void instantiateSipServlets(final ManagedClassInstantiator managedClassInstantiator) throws Exception {
        for (final SipServletInfo servlet : sipServlets.values()) {
            servlet.prepare(servletContext, classLoader, managedClassInstantiator);
        }
    }
    
    private <T extends EventListener> void instantiateSipListeners(final Class<T> iface) throws Exception {
        final Set<SipListenerInfo> listeners = sipListeners.get(iface);
        if (listeners != null) {
            for (final SipListenerInfo listener : listeners) {
                listener.prepare(classLoader);
            }
        }
    }
    
    /**
     * Prepares this SipModule for use. This must be called before the module
     * is used.
     */
    public void prepare(final SipServletService service, final ManagedClassInstantiator managedClassInstantiator) throws DeploymentUnitProcessingException {
        
        final ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            servletContext = new ServletContextDelegate(
                    context.getServletContext(),
                    service,
                    this);

            if ((mainServletName = getMainServletName()) == null) {
                throw new DeploymentUnitProcessingException("Unable to determine main servlet name");
            }
            
            instantiateSipServlets(managedClassInstantiator);
            
            for (final Class clazz : org.andrewwinter.jsr289.util.Util.LISTENER_CLASSES) {
                instantiateSipListeners(clazz);
            }
            
        } catch (Exception e) {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
            throw new DeploymentUnitProcessingException(e);
        }
        Thread.currentThread().setContextClassLoader(oldClassLoader);
    }
}
