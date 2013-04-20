package org.andrewwinter.jsr289.jboss;

import java.io.IOException;
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
import org.andrewwinter.jsr289.SipServletRequestHandler;
import org.andrewwinter.jsr289.api.InboundSipServletRequestImpl;
import org.andrewwinter.jsr289.api.OutboundSipServletRequestImpl;
import org.andrewwinter.jsr289.api.SipFactoryImpl;
import org.andrewwinter.jsr289.api.SipServletRequestImpl;
import org.andrewwinter.jsr289.store.SipListenerStore;
import org.andrewwinter.jsr289.store.SipServletStore;
import org.andrewwinter.jsr289.api.SipSessionImpl;
import org.andrewwinter.jsr289.store.SipSessionStore;
import org.andrewwinter.jsr289.jboss.metadata.SipListenerInfo;
import org.andrewwinter.jsr289.jboss.metadata.SipModuleInfo;
import org.andrewwinter.jsr289.model.SipServletDelegate;
import org.andrewwinter.sip.SipRequestHandler;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.message.InboundSipRequest;
import org.andrewwinter.sip.transport.NettyServerTransport;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.modules.ModuleClassLoader;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SipServletService implements SipRequestHandler, SipServletRequestHandler, Service<SipServletService> {

    public static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("sipservlet");
    
    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(SipServletService.class);

    /**
     * Map from application name to SIP metadata.
     */
    private static Map<String, SipModuleInfo> APP_NAME_TO_MODULE_INFO = new HashMap<>();
    private SipApplicationRouter appRouter;
    private NettyServerTransport serverTransport;
    private Set<String> sipInterfaces;

    public SipServletService() {
        sipInterfaces = createLocalInterfaceList();
        serverTransport = new NettyServerTransport(this, 5060);
    }

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
    
    
    public void deployApplication(final SipModuleInfo metadata) throws DeploymentUnitProcessingException {
        if (appRouter == null) {
            // TODO: Handle case where 
        } else {
            final List<String> appNames = new ArrayList<>();
            final String appName = metadata.getAppName();
            appNames.add(appName);
            appRouter.applicationDeployed(appNames);
            APP_NAME_TO_MODULE_INFO.put(appName, metadata);
            
            final Collection<SipServletDelegate> servlets = metadata.getSipServlets();
            for (final SipServletDelegate servlet : servlets) {
                SipServletStore.getInstance().put(appName, servlet.getServletName(), servlet);
            }
            
            for (final Class clazz : org.andrewwinter.jsr289.util.Util.LISTENER_CLASSES) {
                final Set<SipListenerInfo> listeners = metadata.getSipListeners(clazz);
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

    @Override
    public SipServletService getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    public void doRequest(final SipServletRequestImpl request, final SipModuleInfo moduleInfo, final SipServletDelegate sipServlet) {

        try {
            final ModuleClassLoader cl = moduleInfo.getClassLoader();
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
            SipServletResponse response = request.createResponse(status, reasonPhrase);
            response.send();
        } catch (final Exception e) {
            LOG.debug("Exception generating/sending " + status + ".", e);
        }
    }
    
    private boolean isRequestFromExternalEntity(final SipServletRequest request) {
        return !sipInterfaces.contains(request.getRemoteAddr());
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
        if (true) { // (isRequestFromExternalEntity(sipServletRequest)) {
            
            // If request is received from an external SIP entity, directive is
            // set to NEW.
            
            directive = SipApplicationRoutingDirective.NEW;
            stateInfo = null;
        } else {

            // Request is received from an application, directive is set
            // either implicitly or explicitly by the application.

            directive = request.getRoutingDirective();

            if (directive == SipApplicationRoutingDirective.CONTINUE || directive == SipApplicationRoutingDirective.REVERSE) {
                
                // If request is received from an application, and directive
                // is CONTINUE or REVERSE, stateInfo is set to that of the
                // original request that this request is associated with.

                stateInfo = request.getStateInfo();

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
            
            
        } else if (result.getRouteModifier() == SipRouteModifier.ROUTE_BACK) {

            // TODO: DO THIS
            
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
            
            final URI subscriberUri;
            try {
                subscriberUri = new SipFactoryImpl(null, null, null).createURI(result.getSubscriberURI());
            } catch (ServletParseException e) {
                sendErrorResponse(request, SipServletResponse.SC_SERVER_INTERNAL_ERROR, "App router generated illegal subscriber");
                return;
            }
            
            final SipSessionImpl session = (SipSessionImpl) request.getSession();
            session.setSubscriberURI(subscriberUri);
            session.setStateInfo(result.getStateInfo());
            session.setRegion(result.getRoutingRegion());
            
            request.setSubscriberURI(subscriberUri);
            request.setStateInfo(result.getStateInfo());
            request.setRegion(result.getRoutingRegion());

            // - follow the procedures of Chapter 16 to select a servlet from
            // the application.
            
            final SipModuleInfo moduleInfo = APP_NAME_TO_MODULE_INFO.get(appName);
            if (moduleInfo == null) {
                sendErrorResponse(request, SipServletResponse.SC_SERVER_INTERNAL_ERROR, "No such application " + appName);
                LOG.error("No such application " + appName);
            } else {
                final SipServletDelegate servlet = moduleInfo.getMainServlet();
                doRequest(request, moduleInfo, servlet);
            }
        } else {
            
            final SipURI requestUri = (SipURI) request.getRequestURI();
            if (sipInterfaces.contains(requestUri.getHost()) && request.getHeaders("Route").hasNext()) {
                
                // If the Request-URI does not point to another domain, and there is
                // no Route header, the container should not send the request as it
                // will cause a loop. Instead, the container must reject the request
                // with 404 Not Found final response with no Retry-After header.

                sendErrorResponse(request, SipServletResponse.SC_NOT_FOUND, "No such application " + appName);
                
            } else {
                
                // If the Request-URI points to a different domain, or if there are
                // one or more Route headers, send the request externally according
                // to standard SIP mechanism.
                
                // TOOD: Handle this case
            }
        }
    }

    /**
     *
     * @param isr
     */
    @Override
    public void doRequest(final InboundSipRequest isr) {
        final InboundSipServletRequestImpl sipServletRequest = new InboundSipServletRequestImpl(isr);
        if (sipServletRequest.isInitial()) {
            routeInitialRequest(sipServletRequest);
        } else {
            final Dialog dialog = isr.getServerTransaction().getDialog();
            if (dialog == null) {
                // TODO: Why don't we have a dialog? Maybe we forgot to set one in this scenario.
                LOG.error("_____________________________________________ subsequent request but we don't have a dialog?");
            } else {
                final SipSessionImpl session = SipSessionStore.getInstance().getUsingDialogId(dialog.getId());
                if (session == null) {
                    // TODO: We probably haven't put the session in the session store. Find where the dialog is being created and investigate.
                    
                    // TODO: In such a case, if a subsequent request or response belonging to the corresponding dialog is received, the container is free to handle it in either of the following ways
                    
                    
                    LOG.error("_____________________________________________ subsequent request but we don't have a session?");
                } else {
                    session.doRequest(sipServletRequest);
                }
            }
        }
    }

    @Override
    public void start(final StartContext context) throws StartException {
        serverTransport.listen();
    }

    @Override
    public void stop(final StopContext context) {
        try {
            serverTransport.stopListening();
        } catch (IOException e) {
            LOG.error("Error while stopping listening for SIP traffic.", e);
        }
        
        if (appRouter != null) {
            appRouter.destroy();
        }
    }

    @Override
    public void doRequest(final SipServletRequestImpl request) {
        System.out.println("SipServletService processing request");
    }
}
