package org.andrewwinter.jsr289.jboss.deployment.phase;

import javax.servlet.sip.SipFactory;
import org.jboss.as.naming.ManagedReference;
import org.jboss.as.server.deployment.DeploymentUnit;

/**
 *
 * @author andrew
 */
public class SipFactoryManagedReference implements ManagedReference {

    private final DeploymentUnit du;
    
    private final SipFactory sf;
    
    public SipFactoryManagedReference(final DeploymentUnit du, final SipFactory sf) {
        this.du = du;
        this.sf = sf;
    }
    
    @Override
    public void release() {
    }

    @Override
    public Object getInstance() {
        return sf;
    }
}
