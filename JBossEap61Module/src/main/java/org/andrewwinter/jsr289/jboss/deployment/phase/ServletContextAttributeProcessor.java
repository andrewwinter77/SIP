package org.andrewwinter.jsr289.jboss.deployment.phase;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipURI;
import org.andrewwinter.jsr289.SipServletRequestHandler;
import org.andrewwinter.jsr289.api.ServletContextProvider;
import org.andrewwinter.jsr289.api.SipFactoryImpl;
import org.andrewwinter.jsr289.api.SipSessionsUtilImpl;
import org.andrewwinter.jsr289.api.TimerServiceImpl;
import org.andrewwinter.jsr289.api.URIImpl;
import org.andrewwinter.jsr289.jboss.deployment.attachment.CustomAttachments;
import org.andrewwinter.jsr289.model.SipDeploymentUnit;
import org.andrewwinter.sip.parser.Uri;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.web.deployment.ServletContextAttribute;

/**
 * Installs new services coming from the deployment.
 *
 * @author andrew
 */
public class ServletContextAttributeProcessor extends AbstractDeploymentUnitProcessor {

    @Override
    public void deploy(DeploymentPhaseContext dpc) throws DeploymentUnitProcessingException {

        final DeploymentUnit du = dpc.getDeploymentUnit();
        
        if (!isSipDeploymentUnit(du)) {
            return;
        }
        
        final SipDeploymentUnit sdu = du.getAttachment(CustomAttachments.SIP_DEPLOYMENT_UNIT);
        final String appName = sdu.getAppName();
        
        ServletContextProvider scp = du.getAttachment(CustomAttachments.SERVLET_CONTEXT_PROVIDER);
        if (scp == null) {
            scp = new ServletContextProvider();
            du.putAttachment(CustomAttachments.SERVLET_CONTEXT_PROVIDER, scp);
        }
        
        final SipFactory sf = new SipFactoryImpl(
                sdu.getAppName(),
                sdu.getMainServletName(),
                scp);

        du.addToAttachmentList(ServletContextAttribute.ATTACHMENT_KEY, new ServletContextAttribute(
                SipServlet.SIP_FACTORY, sf));
        
        du.addToAttachmentList(ServletContextAttribute.ATTACHMENT_KEY, new ServletContextAttribute(
                SipServlet.SIP_SESSIONS_UTIL, new SipSessionsUtilImpl(appName)));
        
        
        du.addToAttachmentList(ServletContextAttribute.ATTACHMENT_KEY, new ServletContextAttribute(
                SipServlet.TIMER_SERVICE, new TimerServiceImpl()));
        
        
        du.addToAttachmentList(ServletContextAttribute.ATTACHMENT_KEY, new ServletContextAttribute(
                SipServlet.OUTBOUND_INTERFACES, createOutboundInterfaceList()));
        
        du.addToAttachmentList(ServletContextAttribute.ATTACHMENT_KEY, new ServletContextAttribute(
                SipServletRequestHandler.ATTRIBUTE_NAME, getSipServletContainerService(dpc)));
        
        // UasActiveServlet in the 289 TCK checks this is present.
//        context.setAttribute(ServletContext.TEMPDIR, du.getAttachment(CustomAttachments.TEMP_DIRECTORY));
    }

    private static List<SipURI> createOutboundInterfaceList() {
        final List<SipURI> result = new ArrayList<>();
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getCanonicalHostName());
            if (allMyIps != null) {
                for (final InetAddress addr : allMyIps) {
                    final SipURI uri = (SipURI) URIImpl.create(Uri.parse("sip:" + addr.getHostAddress()));
                    result.add(uri);
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Exception enumerating IPs.");
        }
        
        return result;
    }
}
