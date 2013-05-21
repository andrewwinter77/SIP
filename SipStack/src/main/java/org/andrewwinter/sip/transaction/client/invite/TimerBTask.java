package org.andrewwinter.sip.transaction.client.invite;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author andrew
 */
public class TimerBTask implements Job {

    public static final String TXN_KEY = "txn";
    
    public void execute(final JobExecutionContext context) throws JobExecutionException {
        final InviteClientTransaction txn = (InviteClientTransaction) context.getJobDetail().getJobDataMap().get(TXN_KEY);
        if (txn == null) {
            System.out.println("No transaction in map. THIS IS A BUG!");
        } else {
            txn.timerBFired();
        }
    }
}
