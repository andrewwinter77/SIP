package org.andrewwinter.jsr289.jboss;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.sip.ar.SipApplicationRouter;
import org.andrewwinter.jsr289.SipServletContainer;
import org.andrewwinter.jsr289.model.SipDeploymentUnit;
import org.andrewwinter.jsr289.util.ManagedClassInstantiator;
import org.andrewwinter.sip.transport.NettyServerTransport;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author andrew
 */
public class SipDeploymentUnitService implements Service<SipDeploymentUnitService> {

    /**
     * 
     */
    public static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("sipservlet");
    
    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(SipDeploymentUnitService.class);

    /**
     * 
     */
    private NettyServerTransport serverTransport;

    /**
     * 
     */
    private SipServletContainer container;

    /**
     * 
     */
    public SipDeploymentUnitService() {
        container = new SipServletContainer();
        serverTransport = new NettyServerTransport(container, 5060);
    }

    /**
     * 
     */
    public void deployApplication(final SipDeploymentUnit sdu, final ManagedClassInstantiator instantiator, final ServletContext context)
            throws ClassNotFoundException, InstantiationException, IllegalStateException, IllegalAccessException {
        container.deployApplication(sdu, instantiator, context);
    }

    /**
     * 
     */
    public void deployApplicationRouter(final SipApplicationRouter appRouter) {
        container.deployApplicationRouter(appRouter);
    }

    /**
     * 
     */
    @Override
    public SipDeploymentUnitService getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    /**
     * 
     */
    @Override
    public void start(final StartContext context) throws StartException {
        serverTransport.listen();
        container.start();
    }

    /**
     * 
     */
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
