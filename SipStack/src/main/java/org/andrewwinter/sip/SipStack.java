package org.andrewwinter.sip;

import java.io.IOException;
import org.andrewwinter.sip.transaction.server.noninvite.TimerJ;
import org.andrewwinter.sip.transport.ServerTransport;
import org.quartz.SchedulerException;

/**
 *
 * @author andrew
 */
public class SipStack {
    
    private ServerTransport transport;
    
    public void init(final SipRequestHandler sipListener, final int unsecurePort) {
        transport = ServerTransport.getInstance();
        transport.init(sipListener, unsecurePort);
        
        try {
            TimerJ.getInstance().init();
        } catch (SchedulerException e) {
            // TODO: Throw something like SipStackException here
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            TimerJ.getInstance().start();
        } catch (SchedulerException e) {
            // TODO: Throw something like SipStackException here
            e.printStackTrace();
        }
        
        transport.listen();
    }
    
    public void stop() throws IOException {
        try {
            TimerJ.getInstance().stop();
        } catch (SchedulerException e) {
            // TODO: Throw something like SipStackException here
            e.printStackTrace();
        }
        transport.stopListening();
    }
    
    /**
     *
     * @param args
     */
    public static void main(final String[] args) {
        // main() method required to run app cross-compiled by XMLVM. This is
        // most likely temporary.
    }
}
