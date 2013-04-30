package org.andrewwinter.jsr289.jboss;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.sip.ar.SipApplicationRouter;
import org.andrewwinter.jsr289.SipServletContainer;
import org.andrewwinter.jsr289.model.SipModuleInfo;
import org.andrewwinter.jsr289.util.ManagedClassInstantiator;
import org.andrewwinter.sip.transport.NettyServerTransport;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SipServletService implements Service<SipServletService> {

    public static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("sipservlet");
    
    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(SipServletService.class);

    private NettyServerTransport serverTransport;
    private SipServletContainer container;

    public SipServletService() {
        container = new SipServletContainer();
        serverTransport = new NettyServerTransport(container, 5060);
    }

    
    public void deployApplication(final SipModuleInfo metadata, final ManagedClassInstantiator managedClassInstantiator, final ServletContext servletContext)
            throws ClassNotFoundException, InstantiationException, IllegalStateException, IllegalAccessException {
        container.deployApplication(metadata, managedClassInstantiator, servletContext);
    }

    public void deployApplicationRouter(final SipApplicationRouter appRouter) {
        container.deployApplicationRouter(appRouter);
    }

    @Override
    public SipServletService getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    @Override
    public void start(final StartContext context) throws StartException {
        serverTransport.listen();
        container.start();
    }

    @Override
    public void stop(final StopContext context) {
        try {
            serverTransport.stopListening();
        } catch (IOException e) {
            LOG.error("Error while stopping listening for SIP traffic.", e);
        }
        
        container.stop();
    }
}
