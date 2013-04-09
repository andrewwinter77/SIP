package org.andrewwinter.jsr289.api;

import java.io.Serializable;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.TimerService;

/**
 *
 * @author andrew
 */
public class TimerServiceImpl implements TimerService {

    @Override
    public ServletTimer createTimer(
            final SipApplicationSession appSession,
            final long delay,
            final boolean isPersistent,
            final Serializable info) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ServletTimer createTimer(
            final SipApplicationSession appSession,
            final long delay,
            final long period,
            final boolean fixedDelay,
            final boolean isPersistent,
            final Serializable info) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
