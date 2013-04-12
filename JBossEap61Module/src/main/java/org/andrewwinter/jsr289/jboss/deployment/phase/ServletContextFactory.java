package org.andrewwinter.jsr289.jboss.deployment.phase;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipURI;
import org.andrewwinter.jsr289.api.SipFactoryImpl;
import org.andrewwinter.jsr289.api.SipSessionsUtilImpl;
import org.andrewwinter.jsr289.api.TimerServiceImpl;
import org.andrewwinter.jsr289.jboss.Constants;
import org.andrewwinter.jsr289.jboss.deployment.attachment.CustomAttachments;
import org.andrewwinter.jsr289.jboss.metadata.SipModuleInfo;
import org.apache.catalina.core.StandardContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.web.ext.WebContextFactory;
import org.jboss.logging.Logger;

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
        final SipModuleInfo sipMetadata = du.getAttachment(CustomAttachments.SIP_MODULE_INFO);
        final String appName = sipMetadata.getAppName();
        
        final ServletContext context = sc.getServletContext();
        context.setAttribute(SipServlet.SIP_FACTORY, new SipFactoryImpl(
                sipMetadata.getAppName(),
                sipMetadata.getMainServletName(),
                sipMetadata.getServletContext()));
        context.setAttribute(SipServlet.SIP_SESSIONS_UTIL, new SipSessionsUtilImpl(appName));
        context.setAttribute(SipServlet.TIMER_SERVICE, new TimerServiceImpl());
        
        final List<SipURI> outboundInterfaces = new ArrayList<>();
        // TODO: Add outbound interfaces to this list
        context.setAttribute(SipServlet.OUTBOUND_INTERFACES, outboundInterfaces);
        
        // UasActiveServlet in the 289 TCK checks this is present.
        context.setAttribute(ServletContext.TEMPDIR, du.getAttachment(CustomAttachments.TEMP_DIRECTORY));
    }
}
