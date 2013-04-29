package org.andrewwinter.jsr289;

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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.andrewwinter.jsr289.api.ConvergedHttpSessionImpl;

/**
 *
 * @author andrew
 */
public class HttpServletRequestDelegator implements HttpServletRequest {

    private final HttpServletRequest request;
    
    public HttpServletRequestDelegator(final HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public HttpSession getSession(boolean create) {
        return new ConvergedHttpSessionImpl(
                request.getSession(create),
                null); // TODO: Set the application session here
    }

    @Override
    public HttpSession getSession() {
        return new ConvergedHttpSessionImpl(
                request.getSession(),
                null);  // TODO: Set the application session here
    }

    public String getAuthType() {
        return request.getAuthType();
    }

    public Cookie[] getCookies() {
        return request.getCookies();
    }

    public long getDateHeader(String name) {
        return request.getDateHeader(name);
    }

    public String getHeader(String name) {
        return request.getHeader(name);
    }

    public Enumeration<String> getHeaders(String arg0) {
        return request.getHeaders(arg0);
    }

    public Enumeration<String> getHeaderNames() {
        return request.getHeaderNames();
    }

    public int getIntHeader(String name) {
        return request.getIntHeader(name);
    }

    public String getMethod() {
        return request.getMethod();
    }

    public String getPathInfo() {
        return request.getPathInfo();
    }

    public String getPathTranslated() {
        return request.getPathTranslated();
    }

    public String getContextPath() {
        return request.getContextPath();
    }

    public String getQueryString() {
        return request.getQueryString();
    }

    public String getRemoteUser() {
        return request.getRemoteUser();
    }

    public boolean isUserInRole(String role) {
        return request.isUserInRole(role);
    }

    public Principal getUserPrincipal() {
        return request.getUserPrincipal();
    }

    public String getRequestedSessionId() {
        return request.getRequestedSessionId();
    }

    public String getRequestURI() {
        return request.getRequestURI();
    }

    public StringBuffer getRequestURL() {
        return request.getRequestURL();
    }

    public String getServletPath() {
        return request.getServletPath();
    }

    public boolean isRequestedSessionIdValid() {
        return request.isRequestedSessionIdValid();
    }

    public boolean isRequestedSessionIdFromCookie() {
        return request.isRequestedSessionIdFromCookie();
    }

    public boolean isRequestedSessionIdFromURL() {
        return request.isRequestedSessionIdFromURL();
    }

    public boolean isRequestedSessionIdFromUrl() {
        return request.isRequestedSessionIdFromUrl();
    }

    public boolean authenticate(HttpServletResponse arg0) throws IOException, ServletException {
        return request.authenticate(arg0);
    }

    public void login(String arg0, String arg1) throws ServletException {
        request.login(arg0, arg1);
    }

    public void logout() throws ServletException {
        request.logout();
    }

    public Collection<Part> getParts() throws IOException, ServletException {
        return request.getParts();
    }

    public Part getPart(String arg0) throws IOException, ServletException {
        return request.getPart(arg0);
    }

    public Object getAttribute(String name) {
        return request.getAttribute(name);
    }

    public Enumeration<String> getAttributeNames() {
        return request.getAttributeNames();
    }

    public String getCharacterEncoding() {
        return request.getCharacterEncoding();
    }

    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        request.setCharacterEncoding(env);
    }

    public int getContentLength() {
        return request.getContentLength();
    }

    public String getContentType() {
        return request.getContentType();
    }

    public ServletInputStream getInputStream() throws IOException {
        return request.getInputStream();
    }

    public String getParameter(String name) {
        return request.getParameter(name);
    }

    public Enumeration<String> getParameterNames() {
        return request.getParameterNames();
    }

    public String[] getParameterValues(String name) {
        return request.getParameterValues(name);
    }

    public Map<String, String[]> getParameterMap() {
        return request.getParameterMap();
    }

    public String getProtocol() {
        return request.getProtocol();
    }

    public String getScheme() {
        return request.getScheme();
    }

    public String getServerName() {
        return request.getServerName();
    }

    public int getServerPort() {
        return request.getServerPort();
    }

    public BufferedReader getReader() throws IOException {
        return request.getReader();
    }

    public String getRemoteAddr() {
        return request.getRemoteAddr();
    }

    public String getRemoteHost() {
        return request.getRemoteHost();
    }

    public void setAttribute(String name, Object o) {
        request.setAttribute(name, o);
    }

    public void removeAttribute(String name) {
        request.removeAttribute(name);
    }

    public Locale getLocale() {
        return request.getLocale();
    }

    public Enumeration<Locale> getLocales() {
        return request.getLocales();
    }

    public boolean isSecure() {
        return request.isSecure();
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        return request.getRequestDispatcher(path);
    }

    public String getRealPath(String path) {
        return request.getRealPath(path);
    }

    public int getRemotePort() {
        return request.getRemotePort();
    }

    public String getLocalName() {
        return request.getLocalName();
    }

    public String getLocalAddr() {
        return request.getLocalAddr();
    }

    public int getLocalPort() {
        return request.getLocalPort();
    }

    public ServletContext getServletContext() {
        return request.getServletContext();
    }

    public AsyncContext startAsync() throws IllegalStateException {
        return request.startAsync();
    }

    public AsyncContext startAsync(ServletRequest arg0, ServletResponse arg1) throws IllegalStateException {
        return request.startAsync(arg0, arg1);
    }

    public boolean isAsyncStarted() {
        return request.isAsyncStarted();
    }

    public boolean isAsyncSupported() {
        return request.isAsyncSupported();
    }

    public AsyncContext getAsyncContext() {
        return request.getAsyncContext();
    }

    public DispatcherType getDispatcherType() {
        return request.getDispatcherType();
    }

    public int hashCode() {
        return request.hashCode();
    }

    public boolean equals(Object obj) {
        return request.equals(obj);
    }

    public String toString() {
        return request.toString();
    }
}
