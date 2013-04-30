package org.andrewwinter.jsr289.jboss.deployment.phase;

import com.sun.java.xml.ns.javaee.NonEmptyStringType;
import com.sun.java.xml.ns.javaee.ParamValueType;
import com.sun.java.xml.ns.javaee.ServletType;
import java.io.IOException;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.andrewwinter.jsr289.jboss.Constants;
import org.andrewwinter.jsr289.jboss.deployment.attachment.CustomAttachments;
import org.andrewwinter.jsr289.model.SipApplicationInfo;
import org.andrewwinter.jsr289.model.SipModuleInfo;
import org.andrewwinter.jsr289.model.SipServletDelegate;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.logging.Logger;
import org.jboss.vfs.VirtualFile;
import org.jcp.xml.ns.sipservlet.SipAppType;

/**
 * Parses the deployment descriptors and builds up the annotation index.
 * Class-Path entries from META-INF/MANIFEST.MF are added.
 *
 * @author andrew
 */
public class SipXmlParser extends AbstractDeploymentUnitProcessor {

    private static final Logger LOG = Logger.getLogger(Constants.MODULE_NAME);

    @Override
    public void deploy(final DeploymentPhaseContext context) throws DeploymentUnitProcessingException {

        final DeploymentUnit du = context.getDeploymentUnit();
        if (!isSipApplication(du)) {
            return;
        }

        final SipModuleInfo moduleInfo = new SipModuleInfo();
        
        final SipApplicationInfo sipAppInfo = new SipApplicationInfo();
        moduleInfo.add(sipAppInfo);

        final ResourceRoot root = du.getAttachment(Attachments.DEPLOYMENT_ROOT);
        final VirtualFile sipXml = root.getRoot().getChild("WEB-INF/sip.xml");

        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(SipAppType.class);

            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            // sip.xml gets unmarshalled into a SipAppType object.
            final JAXBElement element = (JAXBElement) unmarshaller.unmarshal(sipXml.getPhysicalFile());

            final SipAppType sipAppType = (SipAppType) element.getValue();

            List<JAXBElement<?>> elements = sipAppType.getAppNameOrDescriptionAndDisplayName();
            for (final JAXBElement el : elements) {
                processElement(el, moduleInfo, sipAppInfo);
            }

        } catch (JAXBException | IOException e) {
            e.printStackTrace();
            throw new DeploymentUnitProcessingException("Error parsing sip.xml.");
        }

        du.putAttachment(CustomAttachments.SIP_MODULE_INFO, moduleInfo);
    }
    
    private void processElement(
            final JAXBElement element,
            final SipModuleInfo sipDeploymentInfo,
            final SipApplicationInfo sipAppInfo) throws DeploymentUnitProcessingException {
        
        if (element.getDeclaredType().equals(NonEmptyStringType.class)) {
            final NonEmptyStringType type = (NonEmptyStringType) element.getValue();
            
            final String localPart = element.getName().getLocalPart();
            
            if ("app-name".equalsIgnoreCase(localPart)) {
                sipAppInfo.setAppName(type.getValue());
            } else {
                throw new DeploymentUnitProcessingException("Unknown String type in sip.xml: " + element.getName().getLocalPart() + ", id: " + type.getId() + ", value: " + type.getValue());
            }
            
            
        } else if (element.getDeclaredType().equals(ParamValueType.class)) {
            final ParamValueType type = (ParamValueType) element.getValue();
            
            final String localPart = element.getName().getLocalPart();
            
            if ("context-param".equalsIgnoreCase(localPart)) {
                
                sipDeploymentInfo.addContextParam(
                        type.getParamName().getValue(),
                        type.getParamValue().getValue());
                
            } else {
                throw new DeploymentUnitProcessingException("Unhandled attribute " + element.getName().getLocalPart() + ", id: " + type.getId() + ", param: " + type.getParamName().getValue() + ", value: " + type.getParamValue().getValue());
            }
            
        } else if (element.getDeclaredType().equals(ServletType.class)) {
            final ServletType type = (ServletType) element.getValue();
            sipDeploymentInfo.add(processServletType(type));
            
        } else {
            throw new DeploymentUnitProcessingException("Unknown type " + element.getDeclaredType().getName());
        }
    }
    
    private SipServletDelegate processServletType(final ServletType type) throws DeploymentUnitProcessingException {
        
        if (type.getServletClass() == null) {
            throw new DeploymentUnitProcessingException("Servlet class not specified.");
        }
        
        if (type.getServletName() == null) {
            throw new DeploymentUnitProcessingException("Servlet name not specified.");
        }
        
        final SipServletDelegate ssi = new SipServletDelegate(
                type.getServletClass().getValue(),
                type.getServletName().getValue(),
                type.getLoadOnStartup(),
                null, // Application name not set in servlet declaration in deployment descriptor.
                ""); // TODO: Do something with description

        LOG.error("TODO: Handle init-params");
        // TODO: Handle init-params
        
        return ssi;
    }
}
