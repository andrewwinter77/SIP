package org.andrewwinter.jsr289.jboss.deployment.phase;

import org.andrewwinter.jsr289.jboss.SipServletService;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceRegistry;

/**
 *
 * @author andrew
 */
final class Util {
    
    private Util() {
    }
    
    static SipServletService getSipServletService(final ServiceRegistry registry) throws DeploymentUnitProcessingException {
        final ServiceController<?> controller = registry.getRequiredService(SipServletService.SERVICE_NAME);
        if (controller == null) {
            throw new DeploymentUnitProcessingException("Failed to get service controller.");
        } 
        return (SipServletService) controller.getValue();
    }
}
