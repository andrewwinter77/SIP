package org.andrewwinter.jsr289.jboss.deployment.phase;

import java.io.Serializable;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.TimerService;
import org.andrewwinter.jsr289.jboss.Constants;
import org.jboss.as.naming.ManagedReference;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.logging.Logger;

/**
 *
 * @author andrew
 */
public class TimerServiceManagedReference implements ManagedReference {

    private static final Logger LOG = Logger.getLogger(Constants.MODULE_NAME);

    private final DeploymentUnit du;
    
    public TimerServiceManagedReference(final DeploymentUnit du) {
        this.du = du;
    }
    
    @Override
    public void release() {
    }

    @Override
    public Object getInstance() {
        LOG.info("+++++++++++++++++++++++++++++++++ returning TimerService instance");
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
    }
}
