package org.andrewwinter.jsr289.jboss.deployment.phase;

import org.jboss.as.naming.ManagedReference;
import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.as.server.deployment.DeploymentUnit;

/**
 *
 * @author andrew
 */
public class SipFactoryManagedReferenceFactory implements ManagedReferenceFactory {

    private final DeploymentUnit du;
    
    public SipFactoryManagedReferenceFactory(final DeploymentUnit du) {
        this.du = du;
    }
    
    @Override
    public ManagedReference getReference() {
        return new SipFactoryManagedReference(du);
    }
}
