package org.andrewwinter.sip.transaction.server.invite;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author andrew
 */
public class TimerITask implements Job {

    public static final String TXN_KEY = "txn";
    
    public void execute(final JobExecutionContext context) throws JobExecutionException {
        final InviteServerTransaction txn = (InviteServerTransaction) context.getJobDetail().getJobDataMap().get(TXN_KEY);
        txn.timerIFired();
    }
}
