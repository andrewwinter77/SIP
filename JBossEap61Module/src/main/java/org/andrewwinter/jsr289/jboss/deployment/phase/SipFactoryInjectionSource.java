package org.andrewwinter.jsr289.jboss.deployment.phase;

import javax.servlet.sip.SipFactory;
import org.andrewwinter.jsr289.api.ServletContextProvider;
import org.andrewwinter.jsr289.api.SipFactoryImpl;
import org.andrewwinter.jsr289.jboss.deployment.attachment.CustomAttachments;
import org.andrewwinter.jsr289.model.SipDeploymentUnit;
import org.jboss.as.ee.component.InjectionSource;
import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.ServiceBuilder;

/**
 *
 * @author andrew
 */
public class SipFactoryInjectionSource extends InjectionSource {

    private final DeploymentUnit du;
    
    private SipFactory sf;
    
    public SipFactoryInjectionSource(final DeploymentUnit du) {
        this.du = du;
    }
    
    @Override
    public void getResourceValue(
            final ResolutionContext rc,
            final ServiceBuilder<?> sb,
            final DeploymentPhaseContext dpc,
            final Injector<ManagedReferenceFactory> injector) throws DeploymentUnitProcessingException {
        
        final SipDeploymentUnit sipMetadata = du.getAttachment(CustomAttachments.SIP_DEPLOYMENT_UNIT);

        ServletContextProvider scp = du.getAttachment(CustomAttachments.SERVLET_CONTEXT_PROVIDER);
        if (scp == null) {
            scp = new ServletContextProvider();
            du.putAttachment(CustomAttachments.SERVLET_CONTEXT_PROVIDER, scp);
        }
        
        if (sf == null) {
            sf = new SipFactoryImpl(
                sipMetadata.getAppName(),
                sipMetadata.getMainServletName(),
                scp);
        }

        injector.inject(new SipFactoryManagedReferenceFactory(du, sf));
    }
}
