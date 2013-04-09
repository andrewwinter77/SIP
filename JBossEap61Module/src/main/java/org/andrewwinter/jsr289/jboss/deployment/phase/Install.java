package org.andrewwinter.jsr289.jboss.deployment.phase;

import java.util.Iterator;
import java.util.ServiceLoader;
import javax.servlet.sip.ar.SipApplicationRouter;
import javax.servlet.sip.ar.spi.SipApplicationRouterProvider;
import org.andrewwinter.jsr289.jboss.SipDeploymentService;
import org.andrewwinter.jsr289.jboss.SipServletService;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.weld.services.BeanManagerService;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleClassLoader;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;

/**
 * Installs new services coming from the deployment.
 *
 * @author andrew
 */
public class Install extends AbstractDeploymentUnitProcessor {

    private static ModuleClassLoader getClassLoader(final DeploymentPhaseContext dpc) {

        // Andrew discovered the Module attachment by reading the source code
        // of the JBoss AS 7 class 'InstallReflectionIndexProcessor'.

        final Module module = (Module) dpc.getDeploymentUnit().getAttachment(Attachments.MODULE);
        return module.getClassLoader();
    }

    private void deployApplicationRouter(final DeploymentPhaseContext dpc) throws DeploymentUnitProcessingException {

        final ClassLoader classLoader = getClassLoader(dpc);
        final ServiceLoader<SipApplicationRouterProvider> loader = ServiceLoader.load(SipApplicationRouterProvider.class, classLoader);
        final Iterator<SipApplicationRouterProvider> iter = loader.iterator();
        if (iter.hasNext()) {
            final SipApplicationRouterProvider provider = iter.next();
            final SipApplicationRouter appRouter = provider.getSipApplicationRouter();

            final SipServletService service = getSipServletService(dpc);
            service.deployApplicationRouter(appRouter);
        } else {
            throw new DeploymentUnitProcessingException("No SipApplicationRouter implementation found.");
        }
    }

    private void deployApplication(final DeploymentPhaseContext dpc) throws DeploymentUnitProcessingException {

        final DeploymentUnit du = dpc.getDeploymentUnit();
        final ServiceTarget serviceTarget = dpc.getServiceTarget();
        final ServiceName serviceName = du.getServiceName().append(SipDeploymentService.NAME);
        final SipDeploymentService service = new SipDeploymentService(du);

        final ServiceName beanManagerServiceName = du.getServiceName().append(BeanManagerService.NAME);

        serviceTarget
                .addService(serviceName, service)
                .addDependency(beanManagerServiceName)
                .addDependency(serviceName)
                .addDependency(SipServletService.SERVICE_NAME)
                .setInitialMode(ServiceController.Mode.ACTIVE)
                .install();
    }

    @Override
    public void deploy(DeploymentPhaseContext dpc) throws DeploymentUnitProcessingException {
        if (isApplicationRouter(dpc.getDeploymentUnit())) {
            deployApplicationRouter(dpc);
        } else if (isSipApplication(dpc.getDeploymentUnit())) {
            
            deployApplication(dpc);
        }
    }
}
