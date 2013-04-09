package org.andrewwinter.jsr289.model;



import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.sip.SipServlet;
import org.andrewwinter.jsr289.util.ManagedClassInstantiator;
import org.andrewwinter.servlet.ServletConfigImpl;

/**
 *
 * @author andrew
 */
public class SipServletManager {

    /**
     *      */
    private final String className;
    /**
     *      */
    private final String name;
    /**
     *      */
    private final String applicationName;
    /**
     *      */
    private final String description;
    /**
     *      */
    private final int loadOnStartup;
    /**
     *      */
    private final String displayName;
    /**
     *      */
    private ServletConfig servletConfig;
    /**
     *      */
    private SipServlet servlet;

    /**
     *
     * @param className
     * @param name
     * @param loadOnStartup
     * @param applicationName
     * @param description
     * @throws IllegalArgumentException
     */
    public SipServletManager(
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

    /**
     *
     * @return
     */
    public String getName() {
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

    /**
     *
     * @return
     */
    public SipServlet getServlet() {
        return servlet;
    }

    public void init(
            final ServletContext servletContext,
            final ClassLoader classLoader,
            ManagedClassInstantiator managedClassInstantiator) throws ClassNotFoundException, ServletException {

        // TODO: Pass init params into ServletConfig.
        servletConfig = new ServletConfigImpl(name, servletContext);
//            final Class<?> clazz = classLoader.loadClass(className, true);
        final Class<?> clazz = classLoader.loadClass(className);


        servlet = (SipServlet) managedClassInstantiator.instantiate(clazz);
        //servlet = (SipServlet) clazz.newInstance();

        // TODO: Honour loadOnStartup.
        servlet.init(servletConfig);
//        } catch (InstantiationException | IllegalAccessException e) {
//            throw new DeploymentUnitProcessingException(e);
    }
}
