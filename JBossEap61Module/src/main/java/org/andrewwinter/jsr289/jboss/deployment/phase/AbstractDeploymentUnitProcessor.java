package org.andrewwinter.jsr289.jboss.deployment.phase;

import org.andrewwinter.jsr289.jboss.SipServletService;
import org.andrewwinter.jsr289.jboss.deployment.attachment.CustomAttachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;

/**
 *
 * @author andrew
 */
public abstract class AbstractDeploymentUnitProcessor implements DeploymentUnitProcessor {

    protected boolean isSipApplication(final DeploymentUnit du) {
        return du.hasAttachment(CustomAttachments.SIP_SERVLET_APPLICATION_MARKER);
    }

    protected boolean isApplicationRouter(final DeploymentUnit du) {
        return du.hasAttachment(CustomAttachments.SIP_APPLICATION_ROUTER_MARKER);
    }

    protected SipServletService getSipServletService(final DeploymentPhaseContext context) throws DeploymentUnitProcessingException {
        return Util.getSipServletService(context.getServiceRegistry());
    }

    @Override
    public void undeploy(final DeploymentUnit du) {
    }
}
