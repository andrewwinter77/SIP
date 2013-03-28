package org.andrewwinter.jsr289.jboss.deployment.phase;

import org.andrewwinter.jsr289.jboss.deployment.attachment.CustomAttachments;
import org.jboss.as.ee.structure.DeploymentType;
import org.jboss.as.ee.structure.DeploymentTypeMarker;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.vfs.VirtualFile;

/**
 * Determines the structure of a deployment and looks for sub deployments and
 * metadata files.
 *
 * @author andrew
 */
public class Structure extends AbstractDeploymentUnitProcessor {

    private static final String WAR_EXTENSION = ".war";
    private static final String SAR_EXTENSION = ".sar";
    private static final String JAR_EXTENSION = ".jar";
    private static final String EAR_EXTENSION = ".ear";
    private static final String SIP_DEPLOYMENT_DESCRIPTOR = "WEB-INF/sip.xml";
    private static final String AR_SERVICE = "META-INF/services/javax.servlet.sip.ar.spi.SipApplicationRouterProvider";

    @Override
    public void deploy(final DeploymentPhaseContext dpc) throws DeploymentUnitProcessingException {
        final DeploymentUnit du = dpc.getDeploymentUnit();

        final String name = du.getName();

        final ResourceRoot root = du.getAttachment(Attachments.DEPLOYMENT_ROOT);
        final VirtualFile sipXml = root.getRoot().getChild(SIP_DEPLOYMENT_DESCRIPTOR);

        // If the application.xml deployment descriptor is not present, then all
        // files in the application package with a filename extension of .war or
        // .sar and which contain the sip.xml deployment descriptor are
        // considered converged SIP components.

        if ((name.endsWith(WAR_EXTENSION) || name.endsWith(SAR_EXTENSION)) && sipXml.exists()) {

            CustomAttachments.setMarker(
                    CustomAttachments.SIP_SERVLET_APPLICATION_MARKER, du);

            if (name.endsWith(SAR_EXTENSION)) {

                // Fool web subsystem into thinking this is a WAR because
                // the web subsystem looks specifically for DUs ending
                // .war. See: 
                //
                // http://grepcode.com/file/repository.jboss.org/nexus/content/repositories/releases/org.jboss.as/jboss-as-web/7.1.1.Final/org/jboss/as/web/deployment/WarDeploymentInitializingProcessor.java

                DeploymentTypeMarker.setType(DeploymentType.WAR, du);
            }

//                final DeploymentUnit parent = deploymentUnit.getParent();
//                System.out.println("!!!!!!!!!!!!!!! HAS PARENT CALLED " + parent.getName());

//                deploymentUnit.putAttachment(null, root)

        } else if (name.endsWith(EAR_EXTENSION)) {
        } else if (name.endsWith(JAR_EXTENSION)) {
            final VirtualFile providerConfigFile = root.getRoot().getChild(AR_SERVICE);
            if (providerConfigFile.exists()) {
                CustomAttachments.setMarker(
                        CustomAttachments.SIP_APPLICATION_ROUTER_MARKER, du);
            }
        }
    }
}
