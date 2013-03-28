package org.andrewwinter.jsr289.jboss.deployment.phase;

import java.io.Serializable;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.TimerService;
import org.jboss.as.naming.ManagedReference;
import org.jboss.as.server.deployment.DeploymentUnit;

/**
 *
 * @author andrew
 */
public class TimerServiceManagedReference implements ManagedReference {

    private final DeploymentUnit du;
    
    public TimerServiceManagedReference(final DeploymentUnit du) {
        this.du = du;
    }
    
    @Override
    public void release() {
    }

    @Override
    public Object getInstance() {
        System.out.println("+++++++++++++++++++++++++++++++++ returning TimerService instance");
        return new TimerService() {

            @Override
            public ServletTimer createTimer(SipApplicationSession sas, long l, boolean bln, Serializable srlzbl) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public ServletTimer createTimer(SipApplicationSession sas, long l, long l1, boolean bln, boolean bln1, Serializable srlzbl) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        
        
//        return (SipFactory) du.getAttachment(CustomAttachments.SIP_FACTORY);
    }
}
