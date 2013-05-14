package org.andrewwinter.jsr289.jboss.converged;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
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
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.Session;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.connector.HttpEventImpl;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.Response;
import org.apache.catalina.core.ApplicationFilterChain;
import org.apache.tomcat.util.buf.MessageBytes;
import org.apache.tomcat.util.http.mapper.MappingData;

/**
 *
 * @see http://grepcode.com/file/repository.jboss.org/nexus/content/repositories/releases/org.jboss.web/jbossweb/7.0.3.Final/org/apache/catalina/connector/Request.java
 * @author andrew
 */
public class RequestDelegator extends Request {
    
    private final Request request;

    public RequestDelegator(final Request request) {
        this.request = request;
    }

    @Override
    public HttpServletRequest getRequest() {
        return new RequestFacadeDelegator(
                (RequestFacade) request.getRequest(),
                request);
    }

    @Override
    public HttpServletRequest getRequestFacade() {
        return new RequestFacadeDelegator(
                (RequestFacade) request.getRequestFacade(),
                request);
    }

    @Override
    public HttpSession getSession(boolean create) {
        HttpSession httpSession = request.getSession(create);
        if (httpSession != null) {
            // Wrap the session only if it is non-null
            httpSession = new ConvergedHttpSessionImpl(
                                httpSession,
                                null);  // TODO: Set the application session here
        }
        return httpSession;
    }

    @Override
    public HttpSession getSession() {
        HttpSession httpSession = request.getSession();
        if (httpSession != null) {
            // Wrap the session only if it is non-null
            httpSession = new ConvergedHttpSessionImpl(
                                httpSession,
                                null);  // TODO: Set the application session here
        }
        return httpSession;
    }

    public void setCoyoteRequest(org.apache.coyote.Request coyoteRequest) {
        request.setCoyoteRequest(coyoteRequest);
    }

    public org.apache.coyote.Request getCoyoteRequest() {
        return request.getCoyoteRequest();
    }

    public void recycle() {
        request.recycle();
    }

    public void clearEncoders() {
        request.clearEncoders();
    }

    public int read() throws IOException {
        return request.read();
    }

    public boolean isEof() throws IOException {
        return request.isEof();
    }

    public boolean isReadable() {
        return request.isReadable();
    }

    public Connector getConnector() {
        return request.getConnector();
    }

    public void setConnector(Connector connector) {
        request.setConnector(connector);
    }

    public Context getContext() {
        return request.getContext();
    }

    public void setContext(Context context) {
        request.setContext(context);
    }

    public Random getRandom() {
        return request.getRandom();
    }

    public ApplicationFilterChain getFilterChain() {
        return request.getFilterChain();
    }

    public void setFilterChain(ApplicationFilterChain filterChain) {
        request.setFilterChain(filterChain);
    }

    public void nextFilterChain() {
        request.nextFilterChain();
    }

    public void releaseFilterChain() {
        request.releaseFilterChain();
    }

    public Host getHost() {
        return request.getHost();
    }

    public void setHost(Host host) {
        request.setHost(host);
    }

    public String getInfo() {
        return request.getInfo();
    }

    public MappingData getMappingData() {
        return request.getMappingData();
    }

    public Response getResponse() {
        return request.getResponse();
    }

    public void setResponse(Response response) {
        request.setResponse(response);
    }

    public HttpServletResponse getResponseFacade() {
        return request.getResponseFacade();
    }

    public InputStream getStream() {
        return request.getStream();
    }

    public Wrapper getWrapper() {
        return request.getWrapper();
    }

    public void setWrapper(Wrapper wrapper) {
        request.setWrapper(wrapper);
    }

    public boolean getCanStartAsync() {
        return request.getCanStartAsync();
    }

    public void setCanStartAsync(boolean canStartAsync) {
        request.setCanStartAsync(canStartAsync);
    }

    public ServletInputStream createInputStream() throws IOException {
        return request.createInputStream();
    }

    public void finishRequest() throws IOException {
        request.finishRequest();
    }

    public Object getNote(String name) {
        return request.getNote(name);
    }

    public Iterator getNoteNames() {
        return request.getNoteNames();
    }

    public void removeNote(String name) {
        request.removeNote(name);
    }

    public void setNote(String name, Object value) {
        request.setNote(name, value);
    }

    public void setContentLength(int length) {
        request.setContentLength(length);
    }

    public void setContentType(String type) {
        request.setContentType(type);
    }

    public void setProtocol(String protocol) {
        request.setProtocol(protocol);
    }

    public void setRemoteAddr(String remoteAddr) {
        request.setRemoteAddr(remoteAddr);
    }

    public void setRemoteHost(String remoteHost) {
        request.setRemoteHost(remoteHost);
    }

    public void setScheme(String scheme) {
        request.setScheme(scheme);
    }

    public void setSecure(boolean secure) {
        request.setSecure(secure);
    }

    public void setServerName(String name) {
        request.setServerName(name);
    }

    public void setServerPort(int port) {
        request.setServerPort(port);
    }

    public Object getAttribute(String name) {
        return request.getAttribute(name);
    }

    public X509Certificate[] getCertificateChain() {
        return request.getCertificateChain();
    }

    public Enumeration getAttributeNames() {
        return request.getAttributeNames();
    }

    public String getCharacterEncoding() {
        return request.getCharacterEncoding();
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

    public void setInputStream(ServletInputStream inputStream) {
        request.setInputStream(inputStream);
    }

    public Locale getLocale() {
        return request.getLocale();
    }

    public Enumeration getLocales() {
        return request.getLocales();
    }

    public String getParameter(String name) {
        return request.getParameter(name);
    }

    public Map getParameterMap() {
        return request.getParameterMap();
    }

    public Enumeration getParameterNames() {
        return request.getParameterNames();
    }

    public String[] getParameterValues(String name) {
        return request.getParameterValues(name);
    }

    public String getProtocol() {
        return request.getProtocol();
    }

    public BufferedReader getReader() throws IOException {
        return request.getReader();
    }

    public void setReader(BufferedReader reader) {
        request.setReader(reader);
    }

    public String getRealPath(String path) {
        return request.getRealPath(path);
    }

    public String getRemoteAddr() {
        return request.getRemoteAddr();
    }

    public String getRemoteHost() {
        return request.getRemoteHost();
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

    public RequestDispatcher getRequestDispatcher(String path) {
        return request.getRequestDispatcher(path);
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

    public boolean isSecure() {
        return request.isSecure();
    }

    public void removeAttribute(String name) {
        request.removeAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        request.setAttribute(name, value);
    }

    public void setCharacterEncoding(String enc) throws UnsupportedEncodingException {
        request.setCharacterEncoding(enc);
    }

    public void addCookie(Cookie cookie) {
        request.addCookie(cookie);
    }

    public void addHeader(String name, String value) {
        request.addHeader(name, value);
    }

    public void addLocale(Locale locale) {
        request.addLocale(locale);
    }

    public void addParameter(String name, String[] values) {
        request.addParameter(name, values);
    }

    public void changeSessionId(String newSessionId) {
        request.changeSessionId(newSessionId);
    }

    public void clearCookies() {
        request.clearCookies();
    }

    public void clearHeaders() {
        request.clearHeaders();
    }

    public void clearLocales() {
        request.clearLocales();
    }

    public void clearParameters() {
        request.clearParameters();
    }

    public void setAuthType(String type) {
        request.setAuthType(type);
    }

    public void setContextPath(String path) {
        request.setContextPath(path);
    }

    public void setMethod(String method) {
        request.setMethod(method);
    }

    public void setQueryString(String query) {
        request.setQueryString(query);
    }

    public void setPathInfo(String path) {
        request.setPathInfo(path);
    }

    public void setRequestedSessionCookie(boolean flag) {
        request.setRequestedSessionCookie(flag);
    }

    public void setRequestedSessionId(String id) {
        request.setRequestedSessionId(id);
    }

    public void setRequestedSessionURL(boolean flag) {
        request.setRequestedSessionURL(flag);
    }

    public void setRequestURI(String uri) {
        request.setRequestURI(uri);
    }

    public void setDecodedRequestURI(String uri) {
        request.setDecodedRequestURI(uri);
    }

    public String getDecodedRequestURI() {
        return request.getDecodedRequestURI();
    }

    public MessageBytes getDecodedRequestURIMB() {
        return request.getDecodedRequestURIMB();
    }

    public void setServletPath(String path) {
        request.setServletPath(path);
    }

    public void setUserPrincipal(Principal principal) {
        request.setUserPrincipal(principal);
    }

    public String getAuthType() {
        return request.getAuthType();
    }

    public String getContextPath() {
        return request.getContextPath();
    }

    public MessageBytes getContextPathMB() {
        return request.getContextPathMB();
    }

    public Cookie[] getCookies() {
        return request.getCookies();
    }

    public void setCookies(Cookie[] cookies) {
        request.setCookies(cookies);
    }

    public long getDateHeader(String name) {
        return request.getDateHeader(name);
    }

    public String getHeader(String name) {
        return request.getHeader(name);
    }

    public Enumeration getHeaders(String name) {
        return request.getHeaders(name);
    }

    public Enumeration getHeaderNames() {
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

    public MessageBytes getPathInfoMB() {
        return request.getPathInfoMB();
    }

    public String getPathTranslated() {
        return request.getPathTranslated();
    }

    public String getQueryString() {
        return request.getQueryString();
    }

    public String getRemoteUser() {
        return request.getRemoteUser();
    }

    public MessageBytes getRequestPathMB() {
        return request.getRequestPathMB();
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

    public ServletContext getServletContext() {
        return request.getServletContext();
    }

    public ServletContext getServletContext0() {
        return request.getServletContext0();
    }

    public String getServletPath() {
        return request.getServletPath();
    }

    public MessageBytes getServletPathMB() {
        return request.getServletPathMB();
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

    public boolean isRequestedSessionIdValid() {
        return request.isRequestedSessionIdValid();
    }

    public boolean isUserInRole(String role) {
        return request.isUserInRole(role);
    }

    public Principal getPrincipal() {
        return request.getPrincipal();
    }

    public Principal getUserPrincipal() {
        return request.getUserPrincipal();
    }

    public Session getSessionInternal() {
        return request.getSessionInternal();
    }

    public Session getSessionInternal(boolean create) {
        return request.getSessionInternal(create);
    }

    public HttpEventImpl getEvent() {
        return request.getEvent();
    }

    public boolean isEventMode() {
        return request.isEventMode();
    }

    public void setEventMode(boolean eventMode) {
        request.setEventMode(eventMode);
    }

    public boolean ready() {
        return request.ready();
    }

    public void setTimeout(int timeout) {
        request.setTimeout(timeout);
    }

    public void setTimeout0(int timeout) {
        request.setTimeout0(timeout);
    }

    public void resume() {
        request.resume();
    }

    public void suspend() {
        request.suspend();
    }

    public AsyncContext getAsyncContext() {
        return request.getAsyncContext();
    }

    public boolean isAsyncStarted() {
        return request.isAsyncStarted();
    }

    public boolean isAsyncSupported() {
        return request.isAsyncSupported();
    }

    public AsyncContext startAsync() throws IllegalStateException {
        return request.startAsync();
    }

    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return request.startAsync(servletRequest, servletResponse);
    }

    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return request.authenticate(response);
    }

    public void login(String username, String password) throws ServletException {
        request.login(username, password);
    }

    public void logout() throws ServletException {
        request.logout();
    }

    public DispatcherType getDispatcherType() {
        return request.getDispatcherType();
    }

    public Part getPart(String name) throws IOException, ServletException {
        return request.getPart(name);
    }

    public Collection<Part> getParts() throws IOException, ServletException {
        return request.getParts();
    }

    public boolean hasSendfile() {
        return request.hasSendfile();
    }

    public int hashCode() {
        return request.hashCode();
    }

    public boolean equals(Object obj) {
        return request.equals(obj);
    }
}
