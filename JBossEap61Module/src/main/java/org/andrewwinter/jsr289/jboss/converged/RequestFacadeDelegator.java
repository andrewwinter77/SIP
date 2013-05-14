package org.andrewwinter.jsr289.jboss.converged;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.andrewwinter.jsr289.api.ConvergedHttpSessionImpl;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.core.ApplicationFilterChain;

/**
 *
 * @author andrew
 */
public class RequestFacadeDelegator extends RequestFacade {
    
    private final RequestFacade facade;

    public RequestFacadeDelegator(RequestFacade facade, Request request) {
        super(request);
        this.facade = facade;
    }

    @Override
    public HttpSession getSession(boolean create) {
        HttpSession httpSession = facade.getSession(create);
        if (httpSession != null) {
            httpSession = new ConvergedHttpSessionImpl(
                                httpSession,
                                null);  // TODO: Set the application session here
        }
        return httpSession;
    }

    @Override
    public HttpSession getSession() {
        HttpSession httpSession = facade.getSession();
        if (httpSession != null) {
            httpSession = new ConvergedHttpSessionImpl(
                                httpSession,
                                null);  // TODO: Set the application session here
        }
        return httpSession;
    }

    public void clear() {
        facade.clear();
    }

    public Object getAttribute(String name) {
        return facade.getAttribute(name);
    }

    public Enumeration getAttributeNames() {
        return facade.getAttributeNames();
    }

    public String getCharacterEncoding() {
        return facade.getCharacterEncoding();
    }

    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        facade.setCharacterEncoding(env);
    }

    public int getContentLength() {
        return facade.getContentLength();
    }

    public String getContentType() {
        return facade.getContentType();
    }

    public ServletInputStream getInputStream() throws IOException {
        return facade.getInputStream();
    }

    public String getParameter(String name) {
        return facade.getParameter(name);
    }

    public Enumeration getParameterNames() {
        return facade.getParameterNames();
    }

    public String[] getParameterValues(String name) {
        return facade.getParameterValues(name);
    }

    public Map getParameterMap() {
        return facade.getParameterMap();
    }

    public String getProtocol() {
        return facade.getProtocol();
    }

    public String getScheme() {
        return facade.getScheme();
    }

    public String getServerName() {
        return facade.getServerName();
    }

    public int getServerPort() {
        return facade.getServerPort();
    }

    public BufferedReader getReader() throws IOException {
        return facade.getReader();
    }

    public String getRemoteAddr() {
        return facade.getRemoteAddr();
    }

    public String getRemoteHost() {
        return facade.getRemoteHost();
    }

    public void setAttribute(String name, Object o) {
        facade.setAttribute(name, o);
    }

    public void removeAttribute(String name) {
        facade.removeAttribute(name);
    }

    public Locale getLocale() {
        return facade.getLocale();
    }

    public Enumeration getLocales() {
        return facade.getLocales();
    }

    public boolean isSecure() {
        return facade.isSecure();
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        return facade.getRequestDispatcher(path);
    }

    public String getRealPath(String path) {
        return facade.getRealPath(path);
    }

    public String getAuthType() {
        return facade.getAuthType();
    }

    public Cookie[] getCookies() {
        return facade.getCookies();
    }

    public long getDateHeader(String name) {
        return facade.getDateHeader(name);
    }

    public String getHeader(String name) {
        return facade.getHeader(name);
    }

    public Enumeration getHeaders(String name) {
        return facade.getHeaders(name);
    }

    public Enumeration getHeaderNames() {
        return facade.getHeaderNames();
    }

    public int getIntHeader(String name) {
        return facade.getIntHeader(name);
    }

    public String getMethod() {
        return facade.getMethod();
    }

    public String getPathInfo() {
        return facade.getPathInfo();
    }

    public String getPathTranslated() {
        return facade.getPathTranslated();
    }

    public String getContextPath() {
        return facade.getContextPath();
    }

    public String getQueryString() {
        return facade.getQueryString();
    }

    public String getRemoteUser() {
        return facade.getRemoteUser();
    }

    public boolean isUserInRole(String role) {
        return facade.isUserInRole(role);
    }

    public Principal getUserPrincipal() {
        return facade.getUserPrincipal();
    }

    public String getRequestedSessionId() {
        return facade.getRequestedSessionId();
    }

    public String getRequestURI() {
        return facade.getRequestURI();
    }

    public StringBuffer getRequestURL() {
        return facade.getRequestURL();
    }

    public String getServletPath() {
        return facade.getServletPath();
    }

    public boolean isRequestedSessionIdValid() {
        return facade.isRequestedSessionIdValid();
    }

    public boolean isRequestedSessionIdFromCookie() {
        return facade.isRequestedSessionIdFromCookie();
    }

    public boolean isRequestedSessionIdFromURL() {
        return facade.isRequestedSessionIdFromURL();
    }

    public boolean isRequestedSessionIdFromUrl() {
        return facade.isRequestedSessionIdFromUrl();
    }

    public String getLocalAddr() {
        return facade.getLocalAddr();
    }

    public String getLocalName() {
        return facade.getLocalName();
    }

    public int getLocalPort() {
        return facade.getLocalPort();
    }

    public int getRemotePort() {
        return facade.getRemotePort();
    }

    public AsyncContext getAsyncContext() {
        return facade.getAsyncContext();
    }

    public ServletContext getServletContext() {
        return facade.getServletContext();
    }

    public boolean isAsyncStarted() {
        return facade.isAsyncStarted();
    }

    public boolean isAsyncSupported() {
        return facade.isAsyncSupported();
    }

    public AsyncContext startAsync() throws IllegalStateException {
        return facade.startAsync();
    }

    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return facade.startAsync(servletRequest, servletResponse);
    }

    public DispatcherType getDispatcherType() {
        return facade.getDispatcherType();
    }

    public ApplicationFilterChain getFilterChain() {
        return facade.getFilterChain();
    }

    public void setFilterChain(ApplicationFilterChain filterChain) {
        facade.setFilterChain(filterChain);
    }

    public void nextFilterChain() {
        facade.nextFilterChain();
    }

    public void releaseFilterChain() {
        facade.releaseFilterChain();
    }

    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return facade.authenticate(response);
    }

    public void login(String username, String password) throws ServletException {
        facade.login(username, password);
    }

    public void logout() throws ServletException {
        facade.logout();
    }

    public Part getPart(String name) throws IOException, ServletException {
        return facade.getPart(name);
    }

    public Collection<Part> getParts() throws IOException, ServletException {
        return facade.getParts();
    }

    public boolean hasSendfile() {
        return facade.hasSendfile();
    }

    public int hashCode() {
        return facade.hashCode();
    }

    public boolean equals(Object obj) {
        return facade.equals(obj);
    }

    public String toString() {
        return facade.toString();
    }
    
    
}
