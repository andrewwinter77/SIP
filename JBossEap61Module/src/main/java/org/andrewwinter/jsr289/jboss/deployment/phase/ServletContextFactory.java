package org.andrewwinter.jsr289.jboss.deployment.phase;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipURI;
import org.andrewwinter.jsr289.SipServletRequestHandler;
import org.andrewwinter.jsr289.api.ServletContextProvider;
import org.andrewwinter.jsr289.api.SipFactoryImpl;
import org.andrewwinter.jsr289.api.SipSessionsUtilImpl;
import org.andrewwinter.jsr289.api.TimerServiceImpl;
import org.andrewwinter.jsr289.jboss.deployment.attachment.CustomAttachments;
import org.andrewwinter.jsr289.jboss.metadata.SipModuleInfo;
import org.apache.catalina.core.StandardContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.web.deployment.ServletContextAttribute;
import org.jboss.as.web.ext.WebContextFactory;

/**
 *
 * @author andrew
 */
public class ServletContextFactory implements WebContextFactory {

    @Override
    public StandardContext createContext(DeploymentUnit du) throws DeploymentUnitProcessingException {
        final StandardContext context = new StandardContext();
        final SipModuleInfo sipMetadata = du.getAttachment(CustomAttachments.SIP_MODULE_INFO);
        sipMetadata.setStandardContext(context);
        return context;
    }

    @Override
    public void postProcessContext(DeploymentUnit du, StandardContext sc) {
        final ServletContext context = sc.getServletContext();
        
        final ServletContextProvider scp = du.getAttachment(CustomAttachments.SERVLET_CONTEXT_PROVIDER);
        scp.setServletContext(context);

        final List<ServletContextAttribute> attributes = du.getAttachmentList(org.jboss.as.web.deployment.ServletContextAttribute.ATTACHMENT_KEY);
        for (ServletContextAttribute attribute : attributes) {
            context.setAttribute(attribute.getName(), attribute.getValue());
        }
        
        // UasActiveServlet in the 289 TCK checks this is present.
        context.setAttribute(ServletContext.TEMPDIR, du.getAttachment(CustomAttachments.TEMP_DIRECTORY));
    }
    
    private static List<SipURI> createOutboundInterfaceList(final SipFactory sf) {
        final List<SipURI> result = new ArrayList<>();
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getCanonicalHostName());
            if (allMyIps != null) {
                for (final InetAddress addr : allMyIps) {
                    result.add(sf.createSipURI(null, addr.getHostAddress()));
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Exception enumerating IPs.");
        }
        
        return result;
    }
}
