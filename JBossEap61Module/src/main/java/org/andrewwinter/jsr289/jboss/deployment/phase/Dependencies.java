package org.andrewwinter.jsr289.jboss.deployment.phase;

import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.module.ModuleDependency;
import org.jboss.as.server.deployment.module.ModuleSpecification;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;

/**
 * Extra class path dependencies are added. For example if deploying a war file
 * the commonly needed dependencies for a web application are added.
 * 
 * @author andrew
 */
public class Dependencies extends AbstractDeploymentUnitProcessor {

    private static final ModuleIdentifier JAVAX_SIP_SERVLET_API = ModuleIdentifier.create("javax.servlet.sip.api");
    
    @Override
    public void deploy(final DeploymentPhaseContext dpc) throws DeploymentUnitProcessingException {
        
        final DeploymentUnit du = dpc.getDeploymentUnit();

        if (!isSipApplication(du) && !isApplicationRouter(du)) {
            return;
        }
        
        final ModuleSpecification moduleSpecification = du.getAttachment(Attachments.MODULE_SPECIFICATION);
        final ModuleLoader moduleLoader = Module.getBootModuleLoader();

        // Note from Andrew:
        // addSystemDependency() doesn't work but I don't know why. I discovered
        // we need to use addLocalDependency() by using JConsole and happened
        // to notice that "MBeans > jboss.modules > ModuleLoader > 
        // LocalModuleLoader > Operations > queryLoadedModuleNames" includes
        // "javax.servlet.sip.api:main".
        
        moduleSpecification.addLocalDependency(new ModuleDependency(
                moduleLoader,
                JAVAX_SIP_SERVLET_API,
                false, // is optional?
                false, // export?
                false, // import services?
                false)); // user specified?

        
        
        // Add module dependencies on Java EE apis
//        moduleSpecification.addSystemDependency(new ModuleDependency(moduleLoader, JAVAX_EE_API, false, false, false, false));
//
//        moduleSpecification.addSystemDependency(new ModuleDependency(moduleLoader, JSTL, false, false, false, false));
//
//        // FIXME we need to revise the exports of the web module, so that we
//        // don't export our internals
//        moduleSpecification.addSystemDependency(new ModuleDependency(moduleLoader, JBOSS_WEB, false, false, true, false));
    }
}
