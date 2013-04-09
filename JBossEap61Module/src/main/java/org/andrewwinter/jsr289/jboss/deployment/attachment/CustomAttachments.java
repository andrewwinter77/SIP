/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.jsr289.jboss.deployment.attachment;

import java.io.File;
import javax.servlet.sip.SipFactory;
import org.andrewwinter.jsr289.api.SipFactoryImpl;
import org.andrewwinter.jsr289.jboss.SipServletService;
import org.andrewwinter.jsr289.jboss.metadata.SipModuleInfo;
import org.jboss.as.server.deployment.AttachmentKey;
import org.jboss.as.server.deployment.DeploymentUnit;

/**
 *
 * @author andrew
 */
public class CustomAttachments {

    public static final AttachmentKey<Boolean> SIP_SERVLET_APPLICATION_MARKER = AttachmentKey.create(Boolean.class);
    
    public static final AttachmentKey<Boolean> SIP_APPLICATION_ROUTER_MARKER = AttachmentKey.create(Boolean.class);
    
    public static final AttachmentKey<SipModuleInfo> SIP_MODULE_INFO = AttachmentKey.create(SipModuleInfo.class);
    
    public static final AttachmentKey<SipServletService> SIP_SERVLET_SERVICE = AttachmentKey.create(SipServletService.class);
    
    public static final AttachmentKey<SipFactoryImpl> SIP_FACTORY = AttachmentKey.create(SipFactory.class);
    
    public static final AttachmentKey<File> TEMP_DIRECTORY = AttachmentKey.create(File.class);
    
    public static void setMarker(final AttachmentKey<Boolean> ak, final DeploymentUnit du) {
//        if (deployment.getParent() != null) {
//            deployment.getParent().putAttachment(ATTACHMENT_KEY, Boolean.TRUE);
//        } else {
            du.putAttachment(ak, Boolean.TRUE);
//        }
    }

//    public static boolean hasMarker(final AttachmentKey<Boolean> ak, final DeploymentUnit du) {
//        return du.hasAttachment(ak);
//    }
    
//    public static boolean isSipServletApplication(final DeploymentUnit deploymentUnit) {
////        DeploymentUnit deployment = deploymentUnit.getParent() == null ? deploymentUnit : deploymentUnit.getParent();
//        Boolean val = (Boolean) deploymentUnit.getAttachment(ATTACHMENT_KEY);
//        return (val != null) && (val.booleanValue());
//    }
    
}
