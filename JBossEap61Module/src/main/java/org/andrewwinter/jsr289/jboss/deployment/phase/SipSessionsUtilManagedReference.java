package org.andrewwinter.jsr289.jboss.deployment.phase;

import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipSessionsUtil;
import org.jboss.as.naming.ManagedReference;
import org.jboss.as.server.deployment.DeploymentUnit;

/**
 *
 * @author andrew
 */
public class SipSessionsUtilManagedReference implements ManagedReference {

    private final DeploymentUnit du;
    
    public SipSessionsUtilManagedReference(final DeploymentUnit du) {
        this.du = du;
    }
    
    @Override
    public void release() {
    }

    @Override
    public Object getInstance() {
        System.out.println("+++++++++++++++++++++++++++++++++ returning SipSessionsUtil instance");
        return new SipSessionsUtil() {

            @Override
            public SipApplicationSession getApplicationSessionById(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public SipApplicationSession getApplicationSessionByKey(String string, boolean bln) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public SipSession getCorrespondingSipSession(SipSession ss, String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            
        };
        
        
//        return (SipFactory) du.getAttachment(CustomAttachments.SIP_FACTORY);
    }
}
