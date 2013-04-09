package org.andrewwinter.jsr289.jboss;

import java.util.List;
import org.andrewwinter.jsr289.jboss.deployment.phase.AnnotationProcessor;
import org.andrewwinter.jsr289.jboss.deployment.phase.CleanUp;
import org.andrewwinter.jsr289.jboss.deployment.phase.ConfigureModule;
import org.andrewwinter.jsr289.jboss.deployment.phase.Dependencies;
import org.andrewwinter.jsr289.jboss.deployment.phase.Install;
import org.andrewwinter.jsr289.jboss.deployment.phase.PostModule;
import org.andrewwinter.jsr289.jboss.deployment.phase.SipXmlParser;
import org.andrewwinter.jsr289.jboss.deployment.phase.Structure;
import org.andrewwinter.jsr289.jboss.deployment.phase.WeldProcessor;
import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.server.AbstractDeploymentChainStep;
import org.jboss.as.server.DeploymentProcessorTarget;
import org.jboss.as.server.deployment.Phase;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;

/**
 * Handler responsible for adding the subsystem resource to the model
 */
class SubsystemAddHandler extends AbstractBoottimeAddStepHandler {

    private static final Logger log = Logger.getLogger(Constants.MODULE_NAME);

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
                
                log.info("Activating SIP Servlet 1.1 Subsystem");
                
                // Get in just ahead of the WAR deployer.
                processorTarget.addDeploymentProcessor(Phase.STRUCTURE, Phase.STRUCTURE_WAR_DEPLOYMENT_INIT-1 /*0x2000*/, new Structure());
                
                // IMPORTANT NOTE:
                // Although we set this here, WeldProcessor() effectively does nothing due to
                // having marked the module as a weld module in Structure(). Rethink this.
                processorTarget.addDeploymentProcessor(Phase.PARSE, Phase.PARSE_WELD_DEPLOYMENT+1, new WeldProcessor());

                processorTarget.addDeploymentProcessor(Phase.PARSE, 0x4000, new SipXmlParser());
                processorTarget.addDeploymentProcessor(Phase.DEPENDENCIES, 0x3000, new Dependencies());
                processorTarget.addDeploymentProcessor(Phase.CONFIGURE_MODULE, 0x200, new ConfigureModule());

                processorTarget.addDeploymentProcessor(Phase.POST_MODULE, 0, new AnnotationProcessor());
                
                processorTarget.addDeploymentProcessor(Phase.POST_MODULE, 0x3000, new PostModule());
                processorTarget.addDeploymentProcessor(Phase.INSTALL, 0x3000, new Install());
                processorTarget.addDeploymentProcessor(Phase.CLEANUP, 0x400, new CleanUp());

            }
        }, OperationContext.Stage.RUNTIME);
    }

    @Override
    protected boolean requiresRuntimeVerification() {
        return false;
    }
}
