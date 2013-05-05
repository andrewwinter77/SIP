package org.andrewwinter.jsr289.jboss;

import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.sip.ar.SipApplicationRouter;
import org.andrewwinter.jsr289.SipServletContainer;
import org.andrewwinter.jsr289.model.SipDeploymentUnit;
import org.andrewwinter.jsr289.util.ManagedClassInstantiator;
import org.andrewwinter.sip.transport.ServerTransport;
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
public class SipServletContainerService implements Service<SipServletContainerService> {

    /**
     * 
     */
    public static final ServiceName NAME = ServiceName.JBOSS.append("sipservlet");
    
    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(SipServletContainerService.class);

    /**
     * 
     */
    private ServerTransport serverTransport;

    /**
     * 
     */
    private SipServletContainer container;

    /**
     * 
     */
    public SipServletContainerService() {
        container = new SipServletContainer(getOurDomains());
        serverTransport = ServerTransport.getInstance();
        serverTransport.init(container, 5060);
    }

    /**
     * Returns the list of domains that resolve to this container. The list
     * is made available to us as a system property. The property's value must
     * be a comma-separated list of domains.
     * @return 
     */
    private static Set<String> getOurDomains() {
        final Set<String> result = new HashSet<>();
        final String property = System.getProperty("org.andrewwinter.sip.domains");
        if (property != null) {
            final String[] domains = property.split(",");
            for (final String domain : domains) {
                result.add(domain.toLowerCase(Locale.US));
            }
        }
        return result;
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
    public SipServletContainerService getValue() throws IllegalStateException, IllegalArgumentException {
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
