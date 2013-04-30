package org.andrewwinter.jsr289.jboss.deployment.phase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.andrewwinter.jsr289.jboss.Constants;
import org.andrewwinter.jsr289.jboss.converged.ConvergedHttpSessionValve;
import org.andrewwinter.jsr289.jboss.deployment.attachment.CustomAttachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.web.deployment.WarMetaData;
import org.jboss.as.web.ext.WebContextFactory;
import org.jboss.logging.Logger;
import org.jboss.metadata.web.jboss.JBossWebMetaData;
import org.jboss.metadata.web.jboss.ValveMetaData;

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

    /**
     * 
     */
    private static final Logger LOG = Logger.getLogger(Constants.MODULE_NAME);

    /**
     * 
     * @param dpc
     * @throws DeploymentUnitProcessingException 
     */
    private void addConvergedHttpSessionValve(final DeploymentPhaseContext dpc) throws DeploymentUnitProcessingException {
        final WarMetaData wmd = dpc.getDeploymentUnit().getAttachment(WarMetaData.ATTACHMENT_KEY);
        if (wmd == null) {
            throw new DeploymentUnitProcessingException("Missing WAR metadata.");
        } else {
            final JBossWebMetaData jwmd = wmd.getMergedJBossWebMetaData();
            if (jwmd == null) {
                throw new DeploymentUnitProcessingException("Mising JBossWeb metadata.");
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

    /**
     * The Web subsystem will create a ServletContext for us. This needs to be
     * done here in readiness for the deploy in the Install phase.
     * 
     * @param du 
     */
    private void attachContextFactory(final DeploymentUnit du) {
        du.putAttachment(WebContextFactory.ATTACHMENT, new ServletContextFactory());
    }

    /**
     * 
     * @param du
     * @throws DeploymentUnitProcessingException 
     */
    private void attachTemporaryDirectory(final DeploymentUnit du) throws DeploymentUnitProcessingException {
        try {
            final Path path = Files.createTempDirectory("sipservlet");
            final File tempDir = path.toFile();
            tempDir.deleteOnExit();
            du.putAttachment(CustomAttachments.TEMP_DIRECTORY, tempDir);
        } catch (final UnsupportedOperationException | IOException e) {
            throw new DeploymentUnitProcessingException("Unable to create TEMPDIR for use with ServletContext.", e);
        }
    }

    /**
     * 
     * @param dpc
     * @throws DeploymentUnitProcessingException 
     */
    @Override
    public void deploy(final DeploymentPhaseContext dpc) throws DeploymentUnitProcessingException {
        final DeploymentUnit du = dpc.getDeploymentUnit();
        if (isSipDeploymentUnit(du)) {
            attachContextFactory(du);
            addConvergedHttpSessionValve(dpc);
            attachTemporaryDirectory(du);
        }
    }
}
