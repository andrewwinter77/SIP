package org.andrewwinter.jsr289.jboss.deployment.phase;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.sip.SipURI;
import org.andrewwinter.jsr289.SipFactoryImpl;
import org.andrewwinter.jsr289.SipSessionsUtilImpl;
import org.andrewwinter.jsr289.TimerServiceImpl;
import org.andrewwinter.jsr289.jboss.SipServletService;
import org.andrewwinter.jsr289.jboss.deployment.attachment.CustomAttachments;
import org.andrewwinter.jsr289.jboss.metadata.SipModuleInfo;
import org.apache.catalina.core.StandardContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
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
        SipServletService service;
        try {
            service = Util.getSipServletService(du.getServiceRegistry());
        } catch (DeploymentUnitProcessingException e) {
            System.out.println("Error getting service.");
            e.printStackTrace();
        }
        
        final SipModuleInfo sipMetadata = du.getAttachment(CustomAttachments.SIP_MODULE_INFO);
        final String appName = sipMetadata.getAppName();
        
        final ServletContext context = sc.getServletContext();
        context.setAttribute("javax.servlet.sip.SipFactory", new SipFactoryImpl(appName, sipMetadata.getMainServletName()));
        context.setAttribute("javax.servlet.sip.SipSessionsUtil", new SipSessionsUtilImpl(appName));
        context.setAttribute("javax.servlet.sip.TimerService", new TimerServiceImpl());
        
        final List<SipURI> outboundInterfaces = new ArrayList<>();
        // TODO: Add outbound interfaces to this list
        context.setAttribute("javax.servlet.sip.outboundInterfaces", outboundInterfaces);
        
        // UasActiveServlet in the 289 TCK checks this is present.
        context.setAttribute("javax.servlet.context.tempdir", du.getAttachment(CustomAttachments.TEMP_DIRECTORY));
    }
}
