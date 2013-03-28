package org.andrewwinter.jsr289.jboss.deployment.phase;

import javax.servlet.sip.SipSessionsUtil;
import org.jboss.as.ee.component.InjectionSource;
import org.jboss.as.ee.component.deployers.EEResourceReferenceProcessor;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;

/**
 *
 * @author andrew
 */
public class SipSessionsUtilResourceReferenceProcessor implements EEResourceReferenceProcessor {

    private final DeploymentUnit du;
    
    public SipSessionsUtilResourceReferenceProcessor(final DeploymentUnit du) {
        this.du = du;
    }
    
    @Override
    public String getResourceReferenceType() {
        return SipSessionsUtil.class.getName();
    }

    @Override
    public InjectionSource getResourceReferenceBindingSource() throws DeploymentUnitProcessingException {
        return new SipSessionsUtilInjectionSource(du);
    }
}
