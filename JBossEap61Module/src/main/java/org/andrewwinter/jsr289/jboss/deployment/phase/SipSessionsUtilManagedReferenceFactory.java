package org.andrewwinter.jsr289.jboss.deployment.phase;

import org.jboss.as.naming.ManagedReference;
import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.as.server.deployment.DeploymentUnit;

/**
 *
 * @author andrew
 */
public class SipSessionsUtilManagedReferenceFactory implements ManagedReferenceFactory {

    private final DeploymentUnit du;
    
    public SipSessionsUtilManagedReferenceFactory(final DeploymentUnit du) {
        this.du = du;
    }
    
    @Override
    public ManagedReference getReference() {
        return new SipSessionsUtilManagedReference(du);
    }
}
