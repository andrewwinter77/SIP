package org.andrewwinter.jsr289.jboss.deployment.phase;

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
    
    public SipFactoryInjectionSource(final DeploymentUnit du) {
        this.du = du;
    }
    
    @Override
    public void getResourceValue(
            final ResolutionContext rc,
            final ServiceBuilder<?> sb,
            final DeploymentPhaseContext dpc,
            final Injector<ManagedReferenceFactory> injector) throws DeploymentUnitProcessingException {
        
        injector.inject(new SipFactoryManagedReferenceFactory(du));
    }
}
