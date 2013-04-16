package org.andrewwinter.jsr289.jboss;

import java.util.List;
import org.andrewwinter.jsr289.jboss.deployment.phase.AnnotationProcessor;
import org.andrewwinter.jsr289.jboss.deployment.phase.Dependencies;
import org.andrewwinter.jsr289.jboss.deployment.phase.Install;
import org.andrewwinter.jsr289.jboss.deployment.phase.PostModule;
import org.andrewwinter.jsr289.jboss.deployment.phase.ServletContextAttributeProcessor;
import org.andrewwinter.jsr289.jboss.deployment.phase.SipXmlParser;
import org.andrewwinter.jsr289.jboss.deployment.phase.Structure;
import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.server.AbstractDeploymentChainStep;
import org.jboss.as.server.DeploymentProcessorTarget;
import org.jboss.as.server.deployment.Phase;
import org.jboss.as.web.deployment.ServletContainerInitializerDeploymentProcessor;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;

/**
 * Handler responsible for adding the subsystem resource to the model
 */
class SubsystemAddHandler extends AbstractBoottimeAddStepHandler {

    private static final Logger LOG = Logger.getLogger(Constants.MODULE_NAME);

    static final SubsystemAddHandler INSTANCE = new SubsystemAddHandler();

    private SubsystemAddHandler() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void populateModel(
            final ModelNode operation,
            final ModelNode model) throws OperationFailedException {
        
        model.setEmptyObject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performBoottime(
            final OperationContext context,
            final ModelNode operation,
            final ModelNode model,
            final ServiceVerificationHandler verificationHandler,
            final List<ServiceController<?>> newControllers) throws OperationFailedException {

        final SipServletService service = new SipServletService();
        
        ServiceController<SipServletService> controller = context.getServiceTarget()
                .addService(SipServletService.SERVICE_NAME, service)
                .addListener(verificationHandler)
                .setInitialMode(Mode.ACTIVE)
                .install();
        newControllers.add(controller);
        
        
        context.addStep(new AbstractDeploymentChainStep() {
            @Override
            public void execute(final DeploymentProcessorTarget processorTarget) {
                
                LOG.info("Activating SIP Servlet 1.1 Subsystem");
                
                processorTarget.addDeploymentProcessor(Phase.STRUCTURE, Phase.STRUCTURE_WAR_DEPLOYMENT_INIT-1, new Structure());
                processorTarget.addDeploymentProcessor(Phase.PARSE, Integer.MAX_VALUE, new SipXmlParser());
                processorTarget.addDeploymentProcessor(Phase.DEPENDENCIES, Integer.MAX_VALUE, new Dependencies());
                processorTarget.addDeploymentProcessor(Phase.POST_MODULE, 0, new AnnotationProcessor());
                processorTarget.addDeploymentProcessor(Phase.POST_MODULE, Integer.MAX_VALUE, new PostModule());
                processorTarget.addDeploymentProcessor(Phase.INSTALL, Phase.INSTALL_WAR_DEPLOYMENT-1, new ServletContextAttributeProcessor());
                processorTarget.addDeploymentProcessor(Phase.INSTALL, Integer.MAX_VALUE, new Install());
            }
        }, OperationContext.Stage.RUNTIME);
    }

    @Override
    protected boolean requiresRuntimeVerification() {
        return false;
    }
}
