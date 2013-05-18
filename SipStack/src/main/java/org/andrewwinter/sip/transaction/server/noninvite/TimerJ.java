package org.andrewwinter.sip.transaction.server.noninvite;

import java.util.ArrayList;
import java.util.List;
import org.andrewwinter.sip.timer.Timer;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author andrew
 */
public class TimerJ {

    private static final TimerJ INSTANCE = new TimerJ();
    private Scheduler scheduler;
    private static List<NonInviteServerTransaction> txnListA;
    private static List<NonInviteServerTransaction> txnListB;
    private static List<NonInviteServerTransaction> txnListCurrent;
    private static final Object lock = new Object();

    public static class TimerTaskJ implements Job {

        public void execute(JobExecutionContext jec) throws JobExecutionException {
            System.out.println("Executing Timer J");
            final List<NonInviteServerTransaction> txnsToOperateOn;
            synchronized (lock) {
                if (txnListCurrent == txnListA) {
                    txnsToOperateOn = txnListB;
                    txnListB = new ArrayList<NonInviteServerTransaction>();
                    txnListCurrent = txnListB;
                } else {
                    txnsToOperateOn = txnListA;
                    txnListA = new ArrayList<NonInviteServerTransaction>();
                    txnListCurrent = txnListA;
                }
            }
            for (final NonInviteServerTransaction txn : txnsToOperateOn) {
                txn.changeState(new Terminated(txn));
            }
        }
    }

    public TimerJ() {
        txnListA = new ArrayList<NonInviteServerTransaction>();
        txnListB = new ArrayList<NonInviteServerTransaction>();
        txnListCurrent = txnListA;
    }

    public static TimerJ getInstance() {
        return INSTANCE;
    }

    public void init() throws SchedulerException {
        final Trigger trigger = TriggerBuilder.newTrigger().withIdentity("TimerJ").withSchedule(
                SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(Timer.TIMER_J).repeatForever()).build();

        JobDetail job = JobBuilder.newJob(TimerTaskJ.class).withIdentity("TimerJJob", "Timer").build();

        scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.scheduleJob(job, trigger);
    }

    public void start() throws SchedulerException {
        scheduler.start();
    }

    public void stop() throws SchedulerException {
        scheduler.shutdown();
    }

    public void submit(final NonInviteServerTransaction txn) {
        synchronized (lock) {
            txnListCurrent.add(txn);
        }
    }
}
