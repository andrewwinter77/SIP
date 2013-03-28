package org.andrewwinter.jsr289.jboss.deployment.phase;

import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;

/**
 * Creates the modular class loader for the deployment. No attempt should be
 * made to load classes from the deployment until after this phase.
 * 
 * @author andrew
 */
public class ConfigureModule extends AbstractDeploymentUnitProcessor {

    @Override
    public void deploy(DeploymentPhaseContext dpc) throws DeploymentUnitProcessingException {
    }
}
