package org.andrewwinter.sip.approuter;

import javax.servlet.sip.ar.SipApplicationRouter;
import javax.servlet.sip.ar.spi.SipApplicationRouterProvider;

/**
 *
 * @author andrew
 */
public class DefaultApplicationRouterProvider extends SipApplicationRouterProvider {

    private static final SipApplicationRouter router = new DefaultApplicationRouter();
    
    @Override
    public SipApplicationRouter getSipApplicationRouter() {
        return router;
    }
}
