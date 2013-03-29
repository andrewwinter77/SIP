package org.andrewwinter.jsr289.jboss.deployment.phase;

import javax.servlet.sip.SipFactory;
import org.jboss.as.ee.component.InjectionSource;
import org.jboss.as.ee.component.deployers.EEResourceReferenceProcessor;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;

/**
 *
 * @author andrew
 */
public class SipFactoryResourceReferenceProcessor implements EEResourceReferenceProcessor {

    private final DeploymentUnit du;
    
    private final SipFactory sf;
    
    public SipFactoryResourceReferenceProcessor(final DeploymentUnit du, final SipFactory sf) {
        this.du = du;
        this.sf = sf;
    }
    
    @Override
    public String getResourceReferenceType() {
        return SipFactory.class.getName();
    }

    @Override
    public InjectionSource getResourceReferenceBindingSource() throws DeploymentUnitProcessingException {
        return new SipFactoryInjectionSource(du, sf);
    }
}
