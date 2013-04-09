package org.andrewwinter.jsr289.jboss.deployment.phase;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.inject.spi.Extension;
import org.andrewwinter.jsr289.jboss.Constants;
import org.andrewwinter.jsr289.jboss.converged.ConvergedHttpSessionValve;
import org.andrewwinter.jsr289.jboss.deployment.attachment.CustomAttachments;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.reflect.DeploymentReflectionIndex;
import org.jboss.as.web.deployment.WarMetaData;
import org.jboss.as.web.ext.WebContextFactory;
import org.jboss.as.weld.deployment.WeldAttachments;
import org.jboss.metadata.web.jboss.JBossWebMetaData;
import org.jboss.metadata.web.jboss.ValveMetaData;
import org.jboss.weld.bootstrap.spi.Metadata;
import org.jboss.weld.metadata.MetadataImpl;

/**
 * Now that the class loader has been constructed we have access to the classes.
 * In this stage deployment processors may use the
 * {@link Attachments.REFLECTION_INDEX} attachment which is a deployment index
 * used to obtain members of classes in the deployment, and to invoke upon them,
 * bypassing the inefficiencies of using {@link java.lang.reflect} directly.
 *
 * @author andrew
 */
public class PostModule extends AbstractDeploymentUnitProcessor {

    private void addConvergedHttpSessionValve(final DeploymentPhaseContext dpc) {
        WarMetaData wmd = dpc.getDeploymentUnit().getAttachment(WarMetaData.ATTACHMENT_KEY);
        if (wmd == null) {
            System.out.println("=============== null WAR metadata");
        } else {

            JBossWebMetaData jwmd = wmd.getMergedJBossWebMetaData();
            if (jwmd == null) {
                System.out.println("=============== null JBossWebMetaData ");
            } else {
                List<ValveMetaData> valves = jwmd.getValves();

                if (valves == null) {
                    jwmd.setValves(valves = new ArrayList<>());
                }

                final ValveMetaData valve = new ValveMetaData();
                valve.setModule(Constants.MODULE_NAME);
                valve.setValveClass(ConvergedHttpSessionValve.class.getName());
                valve.setId(ConvergedHttpSessionValve.class.getName());
                valves.add(valve);
            }
        }
    }

    private void attachContextFactory(final DeploymentUnit du) {
        // The Web subsystem will create a ServletContext for us. This needs
        // to be done here in readiness for the deploy in the Install phase.
        final ServletContextFactory contextFactory = new ServletContextFactory();
        du.putAttachment(WebContextFactory.ATTACHMENT, contextFactory);
    }

    private void attachTemporaryDirectory(final DeploymentUnit du) throws DeploymentUnitProcessingException {
        try {
            final Path path = Files.createTempDirectory("sipservlet");
            final File tempDir = path.toFile();
            tempDir.deleteOnExit();
            du.putAttachment(CustomAttachments.TEMP_DIRECTORY, tempDir);
        } catch (final UnsupportedOperationException | IOException e) {
            throw new DeploymentUnitProcessingException("Unable to create TEMPDIR for use with ServletContext.");
        }
    }

    private void enableCdi(final DeploymentUnit du) throws DeploymentUnitProcessingException {
        final DeploymentReflectionIndex index = du.getAttachment(Attachments.REFLECTION_INDEX);
        final Constructor<SipServletCdiExtension> ctor = index.getClassIndex(SipServletCdiExtension.class).getConstructor(new String[] {});
        try {

            // This thread says it's not possible right now to have a CDI extension
            // module that is not part of the actual deployment. But it's possible
            // to instantiate the extension manually, as we do here.
            // https://community.jboss.org/thread/203725

            final SipServletCdiExtension extension = ctor.newInstance();
            final Metadata<Extension> metadata = new MetadataImpl<Extension>(extension, du.getName());
            du.addToAttachmentList(WeldAttachments.PORTABLE_EXTENSIONS, metadata);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new DeploymentUnitProcessingException("Failed to instantiate extension.", e);
        }
    }
    
    @Override
    public void deploy(final DeploymentPhaseContext dpc) throws DeploymentUnitProcessingException {
        final DeploymentUnit du = dpc.getDeploymentUnit();
        if (isSipApplication(du)) {

            attachContextFactory(du);

            addConvergedHttpSessionValve(dpc);

            attachTemporaryDirectory(du);

            enableCdi(du);
        }
    }
}
