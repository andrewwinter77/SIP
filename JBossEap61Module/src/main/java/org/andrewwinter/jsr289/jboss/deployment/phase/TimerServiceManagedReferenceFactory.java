package org.andrewwinter.jsr289.jboss.deployment.phase;

import org.jboss.as.naming.ManagedReference;
import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.as.server.deployment.DeploymentUnit;

/**
 *
 * @author andrew
 */
public class TimerServiceManagedReferenceFactory implements ManagedReferenceFactory {

    private final DeploymentUnit du;
    
    public TimerServiceManagedReferenceFactory(final DeploymentUnit du) {
        this.du = du;
    }
    
    @Override
    public ManagedReference getReference() {
        return new TimerServiceManagedReference(du);
    }
}
