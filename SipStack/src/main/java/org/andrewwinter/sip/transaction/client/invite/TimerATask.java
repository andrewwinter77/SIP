package org.andrewwinter.sip.transaction.client.invite;

import org.andrewwinter.sip.timer.TimerService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author andrew
 */
public class TimerATask implements Job {

    public static final String TXN_KEY = "txn";
    public static final String PERIOD_KEY = "period";

    public void execute(final JobExecutionContext context) throws JobExecutionException {
        final JobDataMap map = context.getJobDetail().getJobDataMap();
        final int period = map.getIntValue(PERIOD_KEY);
        final InviteClientTransaction txn = (InviteClientTransaction) map.get(TXN_KEY);

        if (period < TimerService.TIMER_A.length - 1) {
            TimerService.getInstance().startTimerA(txn, period + 1);
            txn.timerAFired();
        }
    }
}
