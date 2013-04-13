package org.andrewwinter.jsr289.model;



import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import org.andrewwinter.jsr289.util.ManagedClassInstantiator;
import org.andrewwinter.servlet.ServletConfigImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andrew
 */
public class SipServletDelegate {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(SipServletDelegate.class);
    
    /**
     *
     */
    private final String className;

    /**
     *
     */
    private final String name;

    /**
     *
     */
    private final String applicationName;

    /**
     *
     */
    private final String description;

    /**
     *
     */
    private final int loadOnStartup;

    /**
     *
     */
    private final String displayName;

    /**
     *
     */
    private ServletConfig servletConfig;

    /**
     *
     */
    private SipServlet servlet;

    private ClassLoader classLoader;
    
    private ServletContext servletContext;
    
    private ManagedClassInstantiator instantiator;
    
    /**
     *
     * @param className
     * @param name
     * @param loadOnStartup
     * @param applicationName
     * @param description
     * @throws IllegalArgumentException
     */
    public SipServletDelegate(
            final String className,
            final String name,
            final String loadOnStartup,
            final String applicationName,
            final String description) throws IllegalArgumentException {

        if (className == null || className.isEmpty()) {
            throw new IllegalArgumentException("className must be provided.");
        }
        this.className = className;

        if (name == null || name.isEmpty()) {
            this.name = className;
        } else {
            this.name = name;
        }

        if (applicationName == null || applicationName.isEmpty()) {
            this.applicationName = null;
            displayName = this.name;
        } else {
            this.applicationName = applicationName;
            displayName = applicationName;
        }

        if (description == null || description.isEmpty()) {
            this.description = null;
        } else {
            this.description = description;
        }

        if (loadOnStartup == null || loadOnStartup.isEmpty()) {
            this.loadOnStartup = -1;
        } else {
            try {
                this.loadOnStartup = Integer.valueOf(loadOnStartup);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Illegal @SipServlet value (malformed integer).", e);
            }
        }
    }

    public void setClassLoader(final ClassLoader cl) {
        this.classLoader = cl;
    }
    
    public void setServletContext(final ServletContext context) {
        this.servletContext = context;
    }
    
    public void setManagedClassInstantiator(final ManagedClassInstantiator instantiator) {
        this.instantiator = instantiator;
    }
    
    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    public String getServletName() {
        return name;
    }
    
    /**
     *
     * @return
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return
     */
    public int getLoadOnStartup() {
        return loadOnStartup;
    }

    /**
     *
     * @return
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "SipServletMetadata{" + "className=" + className + ", name=" + name + ", applicationName=" + applicationName + ", description=" + description + ", loadOnStartup=" + loadOnStartup + ", displayName=" + displayName + '}';
    }

    public void service(final SipServletRequest request, final SipServletResponse response) throws ServletException, IOException, ClassNotFoundException {
        instantiator.bindContexts();
        try {
            if (servlet == null) {
                init();
            }
            servlet.service(request, response);
        } catch (ClassNotFoundException | ServletException | IOException | RuntimeException e) {
            throw e;
        } finally {
            instantiator.unbindContexts();
        }
    }
    
    public void init() throws ClassNotFoundException, ServletException {

        LOG.info("Initialising SipServlet {} ", name);
        
        // TODO: Pass init params into ServletConfig.
        servletConfig = new ServletConfigImpl(name, servletContext);
//            final Class<?> clazz = classLoader.loadClass(className, true);
        final Class<?> clazz = classLoader.loadClass(className);

        servlet = (SipServlet) instantiator.instantiate(clazz);

        // TODO: Honour loadOnStartup.
        servlet.init(servletConfig);
//        } catch (InstantiationException | IllegalAccessException e) {
//            throw new DeploymentUnitProcessingException(e);
    }
}
