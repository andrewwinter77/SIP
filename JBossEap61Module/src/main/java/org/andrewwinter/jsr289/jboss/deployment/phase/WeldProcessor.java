package org.andrewwinter.jsr289.jboss.deployment.phase;

import java.util.HashSet;
import java.util.Set;
import org.jboss.as.server.deployment.AttachmentList;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.SubDeploymentMarker;
import org.jboss.as.server.deployment.module.ModuleRootMarker;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.as.weld.WeldDeploymentMarker;
import org.jboss.as.weld.deployment.BeanArchiveMetadata;
import org.jboss.as.weld.deployment.WeldDeploymentMetadata;
import org.jboss.weld.bootstrap.spi.BeansXml;

/**
 * 
 * @author andrew
 */
public class WeldProcessor extends AbstractDeploymentUnitProcessor {

    @Override
    public void deploy(DeploymentPhaseContext dpc) throws DeploymentUnitProcessingException {

        final DeploymentUnit du = dpc.getDeploymentUnit();
        
        // IMPORTANT NOTE:
        // WeldDeploymentMarker.isPartOfWeldDeployment() currently always returns TRUE
        // since we have 
        
        if (isSipApplication(du) && !WeldDeploymentMarker.isPartOfWeldDeployment(du)) {

            // See BeansXmlProcessor
            WeldDeploymentMarker.mark(du);

            AttachmentList<ResourceRoot> structure = du.getAttachment(Attachments.RESOURCE_ROOTS);

            if (structure != null) {
                for (final ResourceRoot resourceRoot : structure) {
                    if (ModuleRootMarker.isModuleRoot(resourceRoot) && !SubDeploymentMarker.isSubDeployment(resourceRoot)) {
                        if (resourceRoot.getRootName().equals("classes")) {

                            final Set<BeanArchiveMetadata> beanArchiveMetadataSet = new HashSet<>();

                            final BeanArchiveMetadata beanArchiveMetadata = new BeanArchiveMetadata(null, resourceRoot, BeansXml.EMPTY_BEANS_XML, true);
                            beanArchiveMetadataSet.add(beanArchiveMetadata);

                            WeldDeploymentMetadata deploymentMetadata = new WeldDeploymentMetadata(beanArchiveMetadataSet);
                            du.putAttachment(WeldDeploymentMetadata.ATTACHMENT_KEY, deploymentMetadata);
                        }
                    }
                }
            }
        }
    }
}
