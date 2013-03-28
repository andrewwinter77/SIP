package org.andrewwinter.jsr289.jboss.deployment.phase;

import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;

/**
 * Removes any attachments put in place earlier in the deployment unit
 * processor chain.
 * 
 * @author andrew
 */
public class CleanUp extends AbstractDeploymentUnitProcessor {

    @Override
    public void deploy(DeploymentPhaseContext dpc) throws DeploymentUnitProcessingException {
    }
}
