package org.andrewwinter.jsr289;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.URI;
import javax.servlet.sip.ar.SipApplicationRouter;
import javax.servlet.sip.ar.SipApplicationRouterInfo;
import javax.servlet.sip.ar.SipApplicationRoutingDirective;
import javax.servlet.sip.ar.SipRouteModifier;
import javax.servlet.sip.ar.SipTargetedRequestInfo;
import org.andrewwinter.jsr289.api.AddressImpl;
import org.andrewwinter.jsr289.api.InboundSipServletRequestImpl;
import org.andrewwinter.jsr289.api.SipFactoryImpl;
import org.andrewwinter.jsr289.api.SipServletRequestImpl;
import org.andrewwinter.jsr289.api.SipSessionImpl;
import org.andrewwinter.jsr289.model.SipListenerInfo;
import org.andrewwinter.jsr289.model.SipDeploymentUnit;
import org.andrewwinter.jsr289.model.SipServletDelegate;
import org.andrewwinter.jsr289.store.SipListenerStore;
import org.andrewwinter.jsr289.store.SipServletStore;
import org.andrewwinter.jsr289.store.SipSessionStore;
import org.andrewwinter.jsr289.threadlocal.AppNameThreadLocal;
import org.andrewwinter.jsr289.threadlocal.MainServletNameThreadLocal;
import org.andrewwinter.jsr289.threadlocal.ServletContextThreadLocal;
import org.andrewwinter.jsr289.util.ManagedClassInstantiator;
import org.andrewwinter.jsr289.util.Util;
import org.andrewwinter.sip.SipRequestHandler;
import org.andrewwinter.sip.message.InboundSipRequest;
import org.andrewwinter.sip.parser.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andrew
 */
public class SipServletContainer implements InboundSipServletRequestHandler, SipRequestHandler {

    /**
     * 
     */
    private static final Logger LOG = LoggerFactory.getLogger(SipServletContainer.class);
    
    /**
     * 
     */
    private final Set<String> sipInterfaces;

    /**
     * 
     */
    private SipApplicationRouter appRouter;

    /**
     * Map from application name to SIP metadata.
     */
    private final Map<String, SipDeploymentUnit> modules;
    
    /**
     * Constructor.
     */
    public SipServletContainer() {
        modules = new HashMap<>();
        sipInterfaces = createLocalInterfaceList();
    }
    
    public void start() {
    }
    
    public void stop() {
        if (appRouter != null) {
            appRouter.destroy();
        }
        // TODO: Destroy all servlets?
    }
    
    public void deployApplication(final SipDeploymentUnit sdu, final ManagedClassInstantiator instantiator, final ServletContext context)
        throws IllegalStateException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (appRouter == null) {
            // TODO: Handle case where 
        } else {
            
            sdu.init(this, instantiator, context);
            
            final List<String> appNames = new ArrayList<>();
            final String appName = sdu.getAppName();
            appNames.add(appName);
            appRouter.applicationDeployed(appNames);
            modules.put(appName, sdu);
            
            final Collection<SipServletDelegate> servlets = sdu.getSipServlets();
            for (final SipServletDelegate servlet : servlets) {
                SipServletStore.getInstance().put(appName, servlet.getServletName(), servlet);
            }
            
            for (final Class clazz : Util.LISTENER_CLASSES) {
                final Set<SipListenerInfo> listeners = sdu.getSipListeners(clazz);
                if (listeners != null) {
                    for (final SipListenerInfo listener : listeners) {
                        SipListenerStore.getInstance().put(appName, clazz, listener.getInstance());
                    }
                }
            }
        }
    }

    public void deployApplicationRouter(final SipApplicationRouter appRouter) {
        if (this.appRouter != null) {
            this.appRouter.destroy();
        }
        this.appRouter = appRouter;
        appRouter.init();
    }

    /**
     * 
     * @return 
     */
    private static Set<String> createLocalInterfaceList() {
        final Set<String> sipInterfaces = new HashSet<>();
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getCanonicalHostName());
            if (allMyIps != null) {
                for (final InetAddress addr : allMyIps) {
                    sipInterfaces.add(addr.getHostAddress());
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Exception enumerating IPs.");
        }
        return sipInterfaces;
    }
    
    
    @Override
    public void doRequest(final SipServletRequestImpl request, final SipDeploymentUnit moduleInfo, final SipServletDelegate sipServlet) {

        try {
            final ClassLoader cl = moduleInfo.getClassLoader();
            Thread.currentThread().setContextClassLoader(cl);

            final ServletContext context = moduleInfo.getServletContext();
            
            org.andrewwinter.jsr289.util.Util.invokeServlet(
                    sipServlet,
                    request,
                    null,
                    context,
                    moduleInfo.getAppName(),
                    moduleInfo.getMainServletName());
            
        } catch (final Exception e) {

            e.printStackTrace();

            // If a servlet throws an exception when invoked to process a
            // request other than ACK and CANCEL, the servlet container MUST
            // generate a 500 response to that request.

            if (!"CANCEL".equals(request.getMethod()) && !"ACK".equals(request.getMethod())) {

                try {
                    // Note this could (and does sometimes) throw an exception when the servlet
                    // threw an exception due to a final response already being sent. If a final
                    // response has already been sent then clearly we won't be able to send a
                    // 500.
                    final SipServletResponse response = request.createResponse(SipServletResponse.SC_SERVER_INTERNAL_ERROR);

                    response.send();
                } catch (final Exception ignore) {
                }
            }
        }
    }

    /**
     * 
     * @param request
     * @return 
     */
    private static SipTargetedRequestInfo getTargettedRequestInfo(final SipServletRequest request) {
        // TODO: Do this. See getNextApplication() Javadocs on SipApplicationRouter
        // for a starting point.
        return null;
    }
    
    /**
     * 
     * @param request
     * @param reasonPhrase 
     */
    private static void sendErrorResponse(final SipServletRequest request, final int status, final String reasonPhrase) {
        try {
            SipServletResponse response;
            if (reasonPhrase == null) {
                response = request.createResponse(status);
            } else {
                response = request.createResponse(status, reasonPhrase);
            }
            response.send();
        } catch (final Exception e) {
            LOG.debug("Exception generating/sending " + status + ".", e);
        }
    }
    
    /**
     * Determines if the request is from outside of an application path. If it
     * is then application sequencing will need to start from the beginning.
     * @param request
     * @return 
     */
    private boolean isRequestFromExternalEntity(final SipServletRequest request) {
        final AddressImpl route = (AddressImpl) request.getPoppedRoute();
        return route == null || route.getURI().getParameter("si") == null || route.getURI().getParameter("directive") == null; 
    }
    
    /**
     * Implements Section 15.4.1 of Sip Servlet 1.1.
     * @param request 
     */
    private void routeInitialRequest(final SipServletRequestImpl request) {
        if (appRouter == null) {
            // No app router deployed.
            sendErrorResponse(request, SipServletResponse.SC_SERVER_INTERNAL_ERROR, "No app router");
            return;
        }

        final Serializable stateInfo;
        final SipApplicationRoutingDirective directive;
        
        if (isRequestFromExternalEntity(request)) {
            
            // If request is received from an external SIP entity, directive is
            // set to NEW.
            
            directive = SipApplicationRoutingDirective.NEW;
            stateInfo = null;
            
        } else {
            
            // Request is received from an application, directive is set
            // either implicitly or explicitly by the application.

            directive = SipApplicationRoutingDirective.valueOf(request.getPoppedRoute().getURI().getParameter("directive"));

            if (directive == SipApplicationRoutingDirective.CONTINUE || directive == SipApplicationRoutingDirective.REVERSE) {
                
                // If request is received from an application, and directive
                // is CONTINUE or REVERSE, stateInfo is set to that of the
                // original request that this request is associated with.

                stateInfo = Integer.valueOf(request.getPoppedRoute().getURI().getParameter("si"));
                
            } else {

                // Otherwise, stateInfo is not set initially.
                
                stateInfo = null;
            }
        }
        
        // 1. Call the SipApplicationRouter.getNextApplication() method of the
        // Application Router object. The Application Router returns a
        // SipApplicationRouterInfo object, named 'result' for this discussion.
        
        final SipApplicationRouterInfo result;
        try {
            result = appRouter.getNextApplication(
                                    request,
                                    null, // Routing region not set initially
                                    directive,
                                    getTargettedRequestInfo(request),
                                    stateInfo);
        } catch (Exception e) {
            // If SipApplicationRouter.getNextApplication() throws an exception,
            // the container should send a 500 Server Internal Error final
            // response to the initial request.
            LOG.error("App router threw exception", e);
            sendErrorResponse(request, SipServletResponse.SC_SERVER_INTERNAL_ERROR, "App router failed");
            return;
        }

        if (result == null) {
            sendErrorResponse(request, SipServletResponse.SC_SERVER_INTERNAL_ERROR, "App router returned null");
            return;
        } 
        
        // 2. Check the result.getRouteModifier()
        
        if (result.getRouteModifier() == SipRouteModifier.ROUTE) {
            
            // If result.getRouteModifier() is ROUTE, then get the routes using
            // result.getRoutes().
        
            final String[] routes = result.getRoutes();
            
            // TODO: DO THIS
            throw new UnsupportedOperationException();
            
            
        } else if (result.getRouteModifier() == SipRouteModifier.ROUTE_BACK) {

            // TODO: DO THIS
            throw new UnsupportedOperationException();
            
        } else {
           
            // If result.getRouteModifier() is NO_ROUTE then disregard the
            // result.getRoutes() and proceed.
        }
        
        // 3. Check the result.getNextApplicationName()
        
        final String appName = result.getNextApplicationName();
        
        if (appName != null) {

            // If result.getNextApplicationName() is not null:
            // - set the application selection state on the SipSession:
            //     * stateInfo to result.getStateInfo(),
            //     * region to result.getRegion(), and
            //     * URI to result.getSubscriberURI().
            
            final SipDeploymentUnit moduleInfo = modules.get(appName);
            if (moduleInfo == null) {
                sendErrorResponse(request, SipServletResponse.SC_SERVER_INTERNAL_ERROR, "No such application " + appName);
                LOG.error("No such application " + appName);
                return;
            }
            
            final URI subscriberUri;
            try {
                subscriberUri = new SipFactoryImpl(null, null, null).createURI(result.getSubscriberURI());
            } catch (ServletParseException e) {
                sendErrorResponse(request, SipServletResponse.SC_SERVER_INTERNAL_ERROR, "App router generated illegal subscriber");
                return;
            }
            
            AppNameThreadLocal.set(appName);
            ServletContextThreadLocal.set(moduleInfo.getServletContext());
            MainServletNameThreadLocal.set(moduleInfo.getMainServletName());
            
            final SipSessionImpl session = (SipSessionImpl) request.getSession();
            session.setSubscriberURI(subscriberUri);
            session.setStateInfo(result.getStateInfo());
            session.setRegion(result.getRoutingRegion());
            
            request.setSubscriberURI(subscriberUri);
            request.setStateInfo(result.getStateInfo());
            request.setRegion(result.getRoutingRegion());
            request.setSipSession(session);

            // - follow the procedures of Chapter 16 to select a servlet from
            // the application.
            
            final SipServletDelegate servlet = moduleInfo.getMainServlet();
            doRequest(request, moduleInfo, servlet);
        } else {
            
            final SipURI requestUri = (SipURI) request.getRequestURI();
            if (sipInterfaces.contains(requestUri.getHost()) && !request.getHeaders("Route").hasNext()) {
                
                // If the Request-URI does not point to another domain, and there is
                // no Route header, the container should not send the request as it
                // will cause a loop. Instead, the container must reject the request
                // with 404 Not Found final response with no Retry-After header.

                sendErrorResponse(request, SipServletResponse.SC_NOT_FOUND, null);
                
            } else {
                
                // If the Request-URI points to a different domain, or if there are
                // one or more Route headers, send the request externally according
                // to standard SIP mechanism.
                
                // TODO: DO THIS
                throw new UnsupportedOperationException();
            }
        }
    }

    private void handleSubsequentRequest(final SipServletRequestImpl request) {
        final AddressImpl route = (AddressImpl) request.getPoppedRoute();
        if (route == null) {
            throw new UnsupportedOperationException();
        } else {
            final String ssid = route.getURI().getParameter("ssid");
            if (ssid == null) {
                throw new UnsupportedOperationException();
            } else {
                final SipSessionImpl ss = SipSessionStore.getInstance().get(ssid);
                ss.doRequest(request);
            }
        }
    }

    /**
     * Pops the topmost Route header and sets the popped route on the request.
     * @param request 
     */
    private void handleRouteHeader(final InboundSipServletRequestImpl request) {
        final Address route = request.getSipRequest().popRoute(); // TODO: Pop only if this request is for us
        if (route != null) {
            
            // TODO: Determine if the Route is for this container
            
            request.setPoppedRoute(new AddressImpl(route, null));
        }
    }
    
    /**
     *
     * @param isr
     */
    @Override
    public void doRequest(final InboundSipRequest isr) {
        final InboundSipServletRequestImpl sipServletRequest = new InboundSipServletRequestImpl(isr);
        
        handleRouteHeader(sipServletRequest);
        
        if (sipServletRequest.isInitial()) {
            routeInitialRequest(sipServletRequest);
        } else {
            
            // TODO: Set ClassLoader.
            
            try {
                handleSubsequentRequest(sipServletRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    
}
