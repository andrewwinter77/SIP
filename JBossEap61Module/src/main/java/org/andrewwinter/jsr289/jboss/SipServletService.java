package org.andrewwinter.jsr289.jboss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.ar.SipApplicationRouter;
import javax.servlet.sip.ar.SipApplicationRouterInfo;
import javax.servlet.sip.ar.SipApplicationRoutingDirective;
import javax.servlet.sip.ar.SipApplicationRoutingRegion;
import org.andrewwinter.jsr289.SipListenerStore;
import org.andrewwinter.jsr289.SipServletRequestImpl;
import org.andrewwinter.jsr289.SipServletStore;
import org.andrewwinter.jsr289.SipSessionImpl;
import org.andrewwinter.jsr289.SipSessionStore;
import org.andrewwinter.jsr289.jboss.metadata.SipListenerInfo;
import org.andrewwinter.jsr289.jboss.metadata.SipModuleInfo;
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

public class SipServletService implements SipRequestHandler, Service<SipServletService> {

    public static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("sipservlet");
    /**
     * Map from application name to SIP metadata.
     */
    private static Map<String, SipModuleInfo> APP_NAME_TO_MODULE_INFO = new HashMap<>();
    private SipApplicationRouter appRouter;
    private NettyServerTransport serverTransport;

    public SipServletService() {
        serverTransport = new NettyServerTransport(this, 5060);
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
            
            final List<SipServlet> servlets = metadata.getSipServlets();
            for (final SipServlet servlet : servlets) {
                SipServletStore.getInstance().put(appName, servlet.getServletName(), servlet);
            }
            
            for (final Class clazz : org.andrewwinter.jsr289.Util.LISTENER_CLASSES) {
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

    public void doRequest(final SipServletRequestImpl request, final SipModuleInfo moduleInfo, final SipServlet sipServlet) {

        try {
            final ModuleClassLoader cl = moduleInfo.getClassLoader();
            Thread.currentThread().setContextClassLoader(cl);

            final ServletContext context = moduleInfo.getServletContext();
            
            org.andrewwinter.jsr289.Util.invokeServlet(
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

    private void doInitialRequest(final SipServletRequestImpl sipServletRequest) {
        if (appRouter == null) {
            // TODO: Handle case where no app router is deployed. Maybe send
            // a 500. Does the SIP Servlet spec say what to do?
        } else {
            final SipApplicationRouterInfo routerInfo = appRouter.getNextApplication(
                    sipServletRequest,
                    SipApplicationRoutingRegion.NEUTRAL_REGION, // TODO: Set region
                    SipApplicationRoutingDirective.NEW, // TODO: Set directive
                    null, // TODO: Set targetted request info
                    null); // TODO: Set state info

            if (routerInfo == null) {
                // TODO: Handle case where app router returns no app. This should
                // happen only when there are no apps deployed, but a misbehaving
                // app router might also return null.
            } else {
                final String appName = routerInfo.getNextApplicationName();
                System.out.println("Next application is: " + appName);

                final SipModuleInfo moduleInfo = APP_NAME_TO_MODULE_INFO.get(appName);
                if (moduleInfo == null) {
                    System.out.println("oh noooooooooooooooo no metadata for application " + appName);
                } else {
                    final SipServlet servlet = moduleInfo.getMainServlet();
                    doRequest(sipServletRequest, moduleInfo, servlet);
                }
            }
        }
    }

    /**
     *
     * @param isr
     */
    @Override
    public void doRequest(final InboundSipRequest isr) {
        final SipServletRequestImpl sipServletRequest = new SipServletRequestImpl(isr);
        if (sipServletRequest.isInitial()) {
            doInitialRequest(sipServletRequest);
        } else {
            final Dialog dialog = isr.getServerTransaction().getDialog();
            if (dialog == null) {
                // TODO: Why don't we have a dialog? Maybe we forgot to set one in this scenario.
                System.out.println("_____________________________________________ subsequent request but we don't have a dialog?");
            } else {
                final SipSessionImpl session = SipSessionStore.getInstance().getUsingDialogId(dialog.getId());
                if (session == null) {
                    // TODO: We probably haven't put the session in the session store. Find where the dialog is being created and investigate.
                    
                    // TODO: In such a case, if a subsequent request or response belonging to the corresponding dialog is received, the container is free to handle it in either of the following ways
                    
                    
                    System.out.println("_____________________________________________ subsequent request but we don't have a session?");
                } else {
                    session.doRequest(isr);
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
            e.printStackTrace();
        }
    }
}
