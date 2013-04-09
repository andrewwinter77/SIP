package org.andrewwinter.jsr289.jboss.metadata;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.sip.SipServlet;
import org.andrewwinter.jsr289.util.ManagedClassInstantiator;
import org.andrewwinter.servlet.ServletConfigImpl;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.modules.ModuleClassLoader;

/**
 *
 * @author andrew
 */
public class SipServletInfo {

    private final String className;
    
    private final String name;
    
    private final String applicationName;
    
    private final String description;
    
    private final int loadOnStartup;
    
    private final String displayName;
    
    private ServletConfig servletConfig;
    
    private SipServlet servlet;
    
    private static boolean isSet(final String str) {
        return str != null && !str.isEmpty();
    }
    
    public SipServletInfo(
            final String className,
            final String name,
            final String loadOnStartup,
            final String applicationName,
            final String description) throws DeploymentUnitProcessingException {
        
        this.className = className;
        
        if (isSet(name)) {
            this.name = name;
        } else {
            this.name = className;
        }

        if (isSet(applicationName)) {
            displayName = applicationName;
        } else {
            displayName = this.name;
        }
        
        this.applicationName = Util.normalize(applicationName);
        this.description = Util.normalize(description);
        
        if (isSet(loadOnStartup)) {
            try {
                this.loadOnStartup = Integer.valueOf(loadOnStartup);
            } catch (NumberFormatException e) {
                throw new DeploymentUnitProcessingException("Illegal @SipServlet value (malformed integer).", e);
            }
        } else {
            this.loadOnStartup = -1;
        }
    }

    public String getName() {
        return name;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getDescription() {
        return description;
    }

    public int getLoadOnStartup() {
        return loadOnStartup;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return "SipServletMetadata{" + "className=" + className + ", name=" + name + ", applicationName=" + applicationName + ", description=" + description + ", loadOnStartup=" + loadOnStartup + ", displayName=" + displayName + '}';
    }

    SipServlet getServlet() {
        return servlet;
    }
    
    void prepare(
            final ServletContext servletContext,
            final ModuleClassLoader classLoader,
            ManagedClassInstantiator managedClassInstantiator) throws DeploymentUnitProcessingException {
        
        try {
            // TODO: Pass init params into ServletConfig.
            servletConfig = new ServletConfigImpl(name, servletContext);
            final Class<?> clazz = classLoader.loadClass(className, true);
            
            servlet = (SipServlet) managedClassInstantiator.instantiate(clazz);
            //servlet = (SipServlet) clazz.newInstance();
            
            // TODO: Honour loadOnStartup.
            servlet.init(servletConfig);
//        } catch (InstantiationException | IllegalAccessException e) {
//            throw new DeploymentUnitProcessingException(e);
        } catch (ClassNotFoundException | ServletException e) {
            throw new DeploymentUnitProcessingException(e);
        }
    }
}
