package org.andrewwinter.sip.timer;

import java.util.List;
import org.andrewwinter.sip.transaction.Transaction;
import org.andrewwinter.sip.transaction.client.invite.InviteClientTransaction;
import org.andrewwinter.sip.transaction.client.invite.TimerATask;
import org.andrewwinter.sip.transaction.client.invite.TimerBTask;
import org.andrewwinter.sip.transaction.server.invite.InviteServerTransaction;
import org.andrewwinter.sip.transaction.server.invite.TimerITask;
import org.andrewwinter.sip.transaction.server.noninvite.NonInviteServerTransaction;
import org.andrewwinter.sip.transaction.server.noninvite.TimerJTask;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author andrew
 */
public class TimerService {

    /**
     * RTT estimate.
     */
    public static final int T1 = 500;
    /**
     * The maximum retransmit interval for non-INVITE requests and INVITE
     * responses.
     */
    public static final int T2 = 4000;
    /**
     * Maximum duration a message will remain in the network.
     */
    public static final int T4 = 5000;
    /**
     * 
     */
    public static final int[] TIMER_A = new int[] {
        T1, 2*T1, 4*T1, 8*T1, 16*T1, 32*T1  
    };
    /**
     *
     */
    public static final int TIMER_B = 64 * T1;
    /**
     *
     */
    public static final int TIMER_C = 181000;
    /**
     *
     */
    public static final int TIMER_D = 33000;
    /**
     *
     */
    public static final int TIMER_F = 64 * T1;
    /**
     *
     */
    public static final int TIMER_H = 64 * T1;
    /**
     *
     */
    public static final int TIMER_I = T4;
    /**
     *
     */
    public static final int TIMER_J = 64 * T1;
    /**
     *
     */
    public static final int TIMER_K = T4;
    
    
    private Scheduler scheduler;
    private static TimerService INSTANCE = new TimerService();

    public static TimerService getInstance() {
        return INSTANCE;
    }

    public void start() throws SchedulerException {
        scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
    }

    public void stop() throws SchedulerException {
        scheduler.shutdown();
    }

    public void deleteTimers(final List<JobKey> timerJobs) {
        try {
            scheduler.deleteJobs(timerJobs);
        } catch (SchedulerException e) {
            // TODO: Handle SchedulerException
            e.printStackTrace();
        }
    }
    
    public void deleteTimer(final JobKey job) {
        try {
            scheduler.deleteJob(job);
        } catch (SchedulerException e) {
            // TODO: Handle SchedulerException
            e.printStackTrace();
        }
    }
    
    
    private JobKey createTimerForTxn(
            final JobDataMap map,
            final int millis,
            final Class jobClass,
            final Transaction txn) throws SchedulerException {
        
        final Trigger trigger = TriggerBuilder
                .newTrigger()
                .startAt(DateBuilder.futureDate(millis, DateBuilder.IntervalUnit.MILLISECOND))
                .build();

        final JobDetail job = JobBuilder
                .newJob(jobClass)
                .usingJobData(map)
                .build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            // TODO: Handle SchedulerException
            e.printStackTrace();
        }
        
        return job.getKey();
    }

    public void startTimerA(final InviteClientTransaction txn, int periodIndex) {

        final JobDataMap map = new JobDataMap();
        map.put(TimerATask.TXN_KEY, txn);
        map.put(TimerATask.PERIOD_KEY, periodIndex);

        try {
            final JobKey key = createTimerForTxn(map, TIMER_A[periodIndex], TimerATask.class, txn);
            txn.setTimer(TimerName.TimerA, key);
        } catch (SchedulerException e) {
            // TODO: Handle SchedulerException
            e.printStackTrace();
        }
    }

    public void startTimerB(final InviteClientTransaction txn) {

        final JobDataMap map = new JobDataMap();
        map.put(TimerBTask.TXN_KEY, txn);

        try {
            final JobKey key = createTimerForTxn(map, TIMER_B, TimerBTask.class, txn);
            txn.setTimer(TimerName.TimerB, key);
        } catch (SchedulerException e) {
            // TODO: Handle SchedulerException
            e.printStackTrace();
        }
    }

    public void startTimerI(final InviteServerTransaction txn) {

        final JobDataMap map = new JobDataMap();
        map.put(TimerITask.TXN_KEY, txn);

        try {
            final JobKey key = createTimerForTxn(map, TIMER_I, TimerITask.class, txn);
            txn.setTimer(TimerName.TimerI, key);
        } catch (SchedulerException e) {
            // TODO: Handle SchedulerException
            e.printStackTrace();
        }
    }

    public void startTimerJ(final NonInviteServerTransaction txn) {

        final JobDataMap map = new JobDataMap();
        map.put(TimerJTask.TXN_KEY, txn);

        try {
            final JobKey key = createTimerForTxn(map, TIMER_J, TimerJTask.class, txn);
            txn.setTimer(TimerName.TimerJ, key);
        } catch (SchedulerException e) {
            // TODO: Handle SchedulerException
            e.printStackTrace();
        }
    }
}
