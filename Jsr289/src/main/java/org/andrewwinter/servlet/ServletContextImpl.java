package org.andrewwinter.servlet;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
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
import org.andrewwinter.jsr289.SipFactoryImpl;
import org.andrewwinter.jsr289.SipSessionsUtilImpl;

/**
 *
 * @author andrew
 */
public class ServletContextImpl implements ServletContext {

    private final Map<String, Object> attributes;
    
    public ServletContextImpl() {
        attributes = new HashMap<>();
        attributes.put("javax.servlet.sip.SipFactory", new SipFactoryImpl(null, null, null));
        attributes.put("javax.servlet.sip.SipSessionsUtil", new SipSessionsUtilImpl(null));
        
    }
    
    @Override
    public String getContextPath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ServletContext getContext(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getMajorVersion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getMinorVersion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getMimeType(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set getResourcePaths(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public URL getResource(String string) throws MalformedURLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public InputStream getResourceAsStream(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Servlet getServlet(String string) throws ServletException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Enumeration getServlets() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Enumeration getServletNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void log(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void log(Exception excptn, String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void log(String string, Throwable thrwbl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRealPath(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getServerInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getInitParameter(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Enumeration getInitParameterNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getAttribute(String string) {
        return attributes.get(string);
    }

    @Override
    public Enumeration getAttributeNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAttribute(String string, Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeAttribute(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getServletContextName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getEffectiveMajorVersion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getEffectiveMinorVersion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setInitParameter(String arg0, String arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Dynamic addServlet(String arg0, String arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Dynamic addServlet(String arg0, Servlet arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Dynamic addServlet(String arg0, Class<? extends Servlet> arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends Servlet> T createServlet(Class<T> arg0) throws ServletException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ServletRegistration getServletRegistration(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String arg0, String arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String arg0, Filter arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String arg0, Class<? extends Filter> arg1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends Filter> T createFilter(Class<T> arg0) throws ServletException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FilterRegistration getFilterRegistration(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addListener(String arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends EventListener> void addListener(T arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addListener(Class<? extends EventListener> arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends EventListener> T createListener(Class<T> arg0) throws ServletException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ClassLoader getClassLoader() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void declareRoles(String... arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
