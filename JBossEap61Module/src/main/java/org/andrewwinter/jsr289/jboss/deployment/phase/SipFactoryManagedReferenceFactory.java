package org.andrewwinter.jsr289.jboss.deployment.phase;

import javax.servlet.sip.SipFactory;
import org.jboss.as.naming.ManagedReference;
import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.as.server.deployment.DeploymentUnit;

/**
 *
 * @author andrew
 */
public class SipFactoryManagedReferenceFactory implements ManagedReferenceFactory {

    private final DeploymentUnit du;
    
    private final SipFactory sf;
    
    public SipFactoryManagedReferenceFactory(final DeploymentUnit du, final SipFactory sf) {
        this.du = du;
        this.sf = sf;
    }
    
    @Override
    public ManagedReference getReference() {
        return new SipFactoryManagedReference(du, sf);
    }
}
