package org.andrewwinter.sip.timer;

import org.andrewwinter.sip.transaction.server.invite.InviteServerTransaction;
import org.andrewwinter.sip.transaction.server.invite.TimerITask;
import org.andrewwinter.sip.transaction.server.noninvite.NonInviteServerTransaction;
import org.andrewwinter.sip.transaction.server.noninvite.TimerJTask;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
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
    
    /**
     * 
     */
    private static final String TIMER_GROUP_NAME = "SipTimer";
    
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

    private void scheduleTask(
            final JobDataMap map,
            final int millis,
            final Class jobClass,
            final String identity) throws SchedulerException {
        
        final Trigger trigger = TriggerBuilder
                .newTrigger()
                .startAt(DateBuilder.futureDate(millis, DateBuilder.IntervalUnit.MILLISECOND))
                .build();

        final JobDetail job = JobBuilder
                .newJob(jobClass)
                .usingJobData(map)
                .withIdentity(identity, TIMER_GROUP_NAME)
                .build();

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            // TODO: Handle SchedulerException
            e.printStackTrace();
        }
    }

    public void startTimerI(final InviteServerTransaction txn) {

        final JobDataMap map = new JobDataMap();
        map.put(TimerITask.TXN_KEY, txn);

        try {
            scheduleTask(map, TIMER_I, TimerITask.class, "TimerIJob");
        } catch (SchedulerException e) {
            // TODO: Handle SchedulerException
            e.printStackTrace();
        }
    }

    public void startTimerJ(final NonInviteServerTransaction txn) {

        final JobDataMap map = new JobDataMap();
        map.put(TimerJTask.TXN_KEY, txn);

        try {
            scheduleTask(map, TIMER_J, TimerJTask.class, "TimerJJob");
        } catch (SchedulerException e) {
            // TODO: Handle SchedulerException
            e.printStackTrace();
        }
    }
}
