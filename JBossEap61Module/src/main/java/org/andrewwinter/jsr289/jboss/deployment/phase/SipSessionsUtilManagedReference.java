package org.andrewwinter.jsr289.jboss.deployment.phase;

import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipSessionsUtil;
import org.andrewwinter.jsr289.jboss.Constants;
import org.jboss.as.naming.ManagedReference;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.logging.Logger;

/**
 *
 * @author andrew
 */
public class SipSessionsUtilManagedReference implements ManagedReference {

    private static final Logger LOG = Logger.getLogger(Constants.MODULE_NAME);

    private final DeploymentUnit du;
    
    public SipSessionsUtilManagedReference(final DeploymentUnit du) {
        this.du = du;
    }
    
    @Override
    public void release() {
    }

    @Override
    public Object getInstance() {
        LOG.info("+++++++++++++++++++++++++++++++++ returning SipSessionsUtil instance");
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
    }
}
