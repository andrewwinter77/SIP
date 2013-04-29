package org.andrewwinter.jsr289.jboss.deployment.phase;

import java.util.List;
import javax.servlet.ServletContext;
import org.andrewwinter.jsr289.api.ServletContextProvider;
import org.andrewwinter.jsr289.jboss.deployment.attachment.CustomAttachments;
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
        du.putAttachment(CustomAttachments.STANDARD_CONTEXT, context);
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
}
