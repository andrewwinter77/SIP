package org.andrewwinter.jsr289.jboss.deployment.attachment;

import java.io.File;
import javax.servlet.sip.SipFactory;
import org.andrewwinter.jsr289.api.ServletContextProvider;
import org.andrewwinter.jsr289.api.SipFactoryImpl;
import org.andrewwinter.jsr289.jboss.SipServletService;
import org.andrewwinter.jsr289.model.SipDeploymentUnit;
import org.apache.catalina.core.StandardContext;
import org.jboss.as.server.deployment.AttachmentKey;
import org.jboss.as.server.deployment.DeploymentUnit;

/**
 *
 * @author andrew
 */
public class CustomAttachments {

    /**
     *
     */
    public static final AttachmentKey<Boolean> SIP_SERVLET_APPLICATION_MARKER = AttachmentKey.create(Boolean.class);
    
    /**
     *
     */
    public static final AttachmentKey<Boolean> SIP_APPLICATION_ROUTER_MARKER = AttachmentKey.create(Boolean.class);
    
    /**
     *
     */
    public static final AttachmentKey<SipDeploymentUnit> SIP_DEPLOYMENT_UNIT = AttachmentKey.create(SipDeploymentUnit.class);
    
    /**
     *
     */
    public static final AttachmentKey<SipServletService> SIP_SERVLET_SERVICE = AttachmentKey.create(SipServletService.class);
    
    /**
     *
     */
    public static final AttachmentKey<SipFactoryImpl> SIP_FACTORY = AttachmentKey.create(SipFactory.class);
    
    /**
     *
     */
    public static final AttachmentKey<File> TEMP_DIRECTORY = AttachmentKey.create(File.class);
    
    /**
     *
     */
    public static final AttachmentKey<ServletContextProvider> SERVLET_CONTEXT_PROVIDER = AttachmentKey.create(ServletContextProvider.class);
    
    /**
     *
     */
    public static final AttachmentKey<StandardContext> STANDARD_CONTEXT = AttachmentKey.create(StandardContext.class);

    /**
     *
     * @param ak
     * @param du
     */
    public static void setMarker(final AttachmentKey<Boolean> ak, final DeploymentUnit du) {
        du.putAttachment(ak, Boolean.TRUE);
    }
}
