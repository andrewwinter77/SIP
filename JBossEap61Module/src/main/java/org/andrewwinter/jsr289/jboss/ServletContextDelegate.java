package org.andrewwinter.jsr289.jboss;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.sip.SipServlet;
import org.andrewwinter.jsr289.jboss.metadata.SipModuleInfo;

/**
 *
 * @author andrew
 */
public class ServletContextDelegate implements ServletContext {
    
    private final ServletContext context;

    private final SipServletService service;
    
    private final SipModuleInfo moduleInfo;
    
    public ServletContextDelegate(final ServletContext context, final SipServletService service, final SipModuleInfo moduleInfo) {
        this.context = context;
        this.service = service;
        this.moduleInfo = moduleInfo;
    }

    @Override
    public RequestDispatcher getNamedDispatcher(final String sipServletName) {
        RequestDispatcher dispatcher = context.getNamedDispatcher(sipServletName);
        if (dispatcher == null) {
            
            final SipServlet servletInfo = moduleInfo.getServlet(sipServletName);
            if (servletInfo != null) {
                dispatcher = new RequestDispatcherImpl(service, moduleInfo, servletInfo);
            } else {
                // TODO: Think we should throw an exception here.
            }
        }
        return dispatcher;
    }

    
    public String getContextPath() {
        return context.getContextPath();
    }

    public ServletContext getContext(String uripath) {
        return context.getContext(uripath);
    }

    public int getMajorVersion() {
        return context.getMajorVersion();
    }

    public int getMinorVersion() {
        return context.getMinorVersion();
    }

    public int getEffectiveMajorVersion() {
        return context.getEffectiveMajorVersion();
    }

    public int getEffectiveMinorVersion() {
        return context.getEffectiveMinorVersion();
    }

    public String getMimeType(String file) {
        return context.getMimeType(file);
    }

    public Set<String> getResourcePaths(String arg0) {
        return context.getResourcePaths(arg0);
    }

    public URL getResource(String path) throws MalformedURLException {
        return context.getResource(path);
    }

    public InputStream getResourceAsStream(String path) {
        return context.getResourceAsStream(path);
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        return context.getRequestDispatcher(path);
    }

    public Servlet getServlet(String name) throws ServletException {
        return context.getServlet(name);
    }

    public Enumeration<Servlet> getServlets() {
        return context.getServlets();
    }

    public Enumeration<String> getServletNames() {
        return context.getServletNames();
    }

    public void log(String msg) {
        context.log(msg);
    }

    public void log(Exception exception, String msg) {
        context.log(exception, msg);
    }

    public void log(String message, Throwable throwable) {
        context.log(message, throwable);
    }

    public String getRealPath(String path) {
        return context.getRealPath(path);
    }

    public String getServerInfo() {
        return context.getServerInfo();
    }

    public String getInitParameter(String name) {
        return context.getInitParameter(name);
    }

    public Enumeration<String> getInitParameterNames() {
        return context.getInitParameterNames();
    }

    public boolean setInitParameter(String arg0, String arg1) {
        return context.setInitParameter(arg0, arg1);
    }

    public Object getAttribute(String name) {
        return context.getAttribute(name);
    }

    public Enumeration<String> getAttributeNames() {
        return context.getAttributeNames();
    }

    public void setAttribute(String name, Object object) {
        context.setAttribute(name, object);
    }

    public void removeAttribute(String name) {
        context.removeAttribute(name);
    }

    public String getServletContextName() {
        return context.getServletContextName();
    }

    public Dynamic addServlet(String arg0, String arg1) {
        return context.addServlet(arg0, arg1);
    }

    public Dynamic addServlet(String arg0, Servlet arg1) {
        return context.addServlet(arg0, arg1);
    }

    public Dynamic addServlet(String arg0, Class<? extends Servlet> arg1) {
        return context.addServlet(arg0, arg1);
    }

    public <T extends Servlet> T createServlet(Class<T> arg0) throws ServletException {
        return context.createServlet(arg0);
    }

    public ServletRegistration getServletRegistration(String arg0) {
        return context.getServletRegistration(arg0);
    }

    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        return context.getServletRegistrations();
    }

    public FilterRegistration.Dynamic addFilter(String arg0, String arg1) {
        return context.addFilter(arg0, arg1);
    }

    public FilterRegistration.Dynamic addFilter(String arg0, Filter arg1) {
        return context.addFilter(arg0, arg1);
    }

    public FilterRegistration.Dynamic addFilter(String arg0, Class<? extends Filter> arg1) {
        return context.addFilter(arg0, arg1);
    }

    public <T extends Filter> T createFilter(Class<T> arg0) throws ServletException {
        return context.createFilter(arg0);
    }

    public FilterRegistration getFilterRegistration(String arg0) {
        return context.getFilterRegistration(arg0);
    }

    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        return context.getFilterRegistrations();
    }

    public SessionCookieConfig getSessionCookieConfig() {
        return context.getSessionCookieConfig();
    }

    public void setSessionTrackingModes(Set<SessionTrackingMode> arg0) {
        context.setSessionTrackingModes(arg0);
    }

    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return context.getDefaultSessionTrackingModes();
    }

    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        return context.getEffectiveSessionTrackingModes();
    }

    public void addListener(String arg0) {
        context.addListener(arg0);
    }

    public <T extends EventListener> void addListener(T arg0) {
        context.addListener(arg0);
    }

    public void addListener(Class<? extends EventListener> arg0) {
        context.addListener(arg0);
    }

    public <T extends EventListener> T createListener(Class<T> arg0) throws ServletException {
        return context.createListener(arg0);
    }

    public JspConfigDescriptor getJspConfigDescriptor() {
        return context.getJspConfigDescriptor();
    }

    public ClassLoader getClassLoader() {
        return context.getClassLoader();
    }

    public void declareRoles(String... arg0) {
        context.declareRoles(arg0);
    }

    public int hashCode() {
        return context.hashCode();
    }

    public boolean equals(Object obj) {
        return context.equals(obj);
    }

    public String toString() {
        return context.toString();
    }
}
