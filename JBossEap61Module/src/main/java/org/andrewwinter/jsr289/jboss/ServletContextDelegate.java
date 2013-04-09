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

    @Override
    public String getContextPath() {
        return context.getContextPath();
    }

    @Override
    public ServletContext getContext(String uripath) {
        return context.getContext(uripath);
    }

    @Override
    public int getMajorVersion() {
        return context.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
        return context.getMinorVersion();
    }

    @Override
    public int getEffectiveMajorVersion() {
        return context.getEffectiveMajorVersion();
    }

    @Override
    public int getEffectiveMinorVersion() {
        return context.getEffectiveMinorVersion();
    }

    @Override
    public String getMimeType(String file) {
        return context.getMimeType(file);
    }

    @Override
    public Set<String> getResourcePaths(String arg0) {
        return context.getResourcePaths(arg0);
    }

    @Override
    public URL getResource(String path) throws MalformedURLException {
        return context.getResource(path);
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        return context.getResourceAsStream(path);
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return context.getRequestDispatcher(path);
    }

    @Override
    @Deprecated
    public Servlet getServlet(String name) throws ServletException {
        return context.getServlet(name);
    }

    @Override
    @Deprecated
    public Enumeration<Servlet> getServlets() {
        return context.getServlets();
    }

    @Override
    @Deprecated
    public Enumeration<String> getServletNames() {
        return context.getServletNames();
    }

    @Override
    public void log(String msg) {
        context.log(msg);
    }

    @Override
    public void log(Exception exception, String msg) {
        context.log(exception, msg);
    }

    @Override
    public void log(String message, Throwable throwable) {
        context.log(message, throwable);
    }

    @Override
    public String getRealPath(String path) {
        return context.getRealPath(path);
    }

    @Override
    public String getServerInfo() {
        return context.getServerInfo();
    }

    @Override
    public String getInitParameter(String name) {
        return context.getInitParameter(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return context.getInitParameterNames();
    }

    @Override
    public boolean setInitParameter(String arg0, String arg1) {
        return context.setInitParameter(arg0, arg1);
    }

    @Override
    public Object getAttribute(String name) {
        return context.getAttribute(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return context.getAttributeNames();
    }

    @Override
    public void setAttribute(String name, Object object) {
        context.setAttribute(name, object);
    }

    @Override
    public void removeAttribute(String name) {
        context.removeAttribute(name);
    }

    @Override
    public String getServletContextName() {
        return context.getServletContextName();
    }

    @Override
    public Dynamic addServlet(String arg0, String arg1) {
        return context.addServlet(arg0, arg1);
    }

    @Override
    public Dynamic addServlet(String arg0, Servlet arg1) {
        return context.addServlet(arg0, arg1);
    }

    @Override
    public Dynamic addServlet(String arg0, Class<? extends Servlet> arg1) {
        return context.addServlet(arg0, arg1);
    }
    @Override

    public <T extends Servlet> T createServlet(Class<T> arg0) throws ServletException {
        return context.createServlet(arg0);
    }

    @Override
    public ServletRegistration getServletRegistration(String arg0) {
        return context.getServletRegistration(arg0);
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        return context.getServletRegistrations();
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String arg0, String arg1) {
        return context.addFilter(arg0, arg1);
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String arg0, Filter arg1) {
        return context.addFilter(arg0, arg1);
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String arg0, Class<? extends Filter> arg1) {
        return context.addFilter(arg0, arg1);
    }

    @Override
    public <T extends Filter> T createFilter(Class<T> arg0) throws ServletException {
        return context.createFilter(arg0);
    }

    @Override
    public FilterRegistration getFilterRegistration(String arg0) {
        return context.getFilterRegistration(arg0);
    }

    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        return context.getFilterRegistrations();
    }

    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        return context.getSessionCookieConfig();
    }

    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> arg0) {
        context.setSessionTrackingModes(arg0);
    }

    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return context.getDefaultSessionTrackingModes();
    }

    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        return context.getEffectiveSessionTrackingModes();
    }

    @Override
    public void addListener(String arg0) {
        context.addListener(arg0);
    }

    @Override
    public <T extends EventListener> void addListener(T arg0) {
        context.addListener(arg0);
    }

    @Override
    public void addListener(Class<? extends EventListener> arg0) {
        context.addListener(arg0);
    }

    @Override
    public <T extends EventListener> T createListener(Class<T> arg0) throws ServletException {
        return context.createListener(arg0);
    }

    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        return context.getJspConfigDescriptor();
    }

    @Override
    public ClassLoader getClassLoader() {
        return context.getClassLoader();
    }

    @Override
    public void declareRoles(String... arg0) {
        context.declareRoles(arg0);
    }

    @Override
    public int hashCode() {
        return context.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return context.equals(obj);
    }

    @Override
    public String toString() {
        return context.toString();
    }
}
