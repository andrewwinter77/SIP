package org.andrewwinter.jsr289.jboss.deployment.phase;

import org.andrewwinter.jsr289.jboss.SipServletService;
import org.andrewwinter.jsr289.jboss.deployment.attachment.CustomAttachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.msc.service.ServiceController;

/**
 *
 * @author andrew
 */
public abstract class AbstractDeploymentUnitProcessor implements DeploymentUnitProcessor {

    /**
     * 
     * @param du
     * @return 
     */
    protected boolean isSipApplication(final DeploymentUnit du) {
        return du.hasAttachment(CustomAttachments.SIP_SERVLET_APPLICATION_MARKER);
    }

    /**
     * 
     * @param du
     * @return 
     */
    protected boolean isApplicationRouter(final DeploymentUnit du) {
        return du.hasAttachment(CustomAttachments.SIP_APPLICATION_ROUTER_MARKER);
    }

    /**
     * 
     * @param context
     * @return
     * @throws DeploymentUnitProcessingException 
     */
    protected SipServletService getSipServletService(final DeploymentPhaseContext context) throws DeploymentUnitProcessingException {
        final ServiceController<?> controller = context.getServiceRegistry().getRequiredService(SipServletService.SERVICE_NAME);
        if (controller == null) {
            throw new DeploymentUnitProcessingException("Failed to get service controller.");
        } 
        return (SipServletService) controller.getValue();
    }

    /**
     * 
     * @param du 
     */
    @Override
    public void undeploy(final DeploymentUnit du) {
    }
}
