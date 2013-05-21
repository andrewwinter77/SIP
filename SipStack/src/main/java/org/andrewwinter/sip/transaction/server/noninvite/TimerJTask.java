package org.andrewwinter.sip.transaction.server.noninvite;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author andrew
 */
public class TimerJTask implements Job {

    public static final String TXN_KEY = "txn";

    public void execute(final JobExecutionContext context) throws JobExecutionException {
        final NonInviteServerTransaction txn = (NonInviteServerTransaction) context.getJobDetail().getJobDataMap().get(TXN_KEY);
        txn.timerJFired();
    }
}
