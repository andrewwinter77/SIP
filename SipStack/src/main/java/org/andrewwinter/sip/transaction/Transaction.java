package org.andrewwinter.sip.transaction;

import java.util.ArrayList;
import java.util.List;
import org.andrewwinter.sip.timer.TimerService;
import org.quartz.JobKey;

/**
 *
 * @author andrew
 */
public abstract class Transaction {

    private JobKey timerA;
    
    private JobKey timerB;
    
    private JobKey timerI;

    private final List<JobKey> timers;

    private boolean terminated;
    
    public void setTimerA(final JobKey job) {
        timerA = job;
    }
    
    public void setTimerB(final JobKey job) {
        timerB = job;
    }
    
    public void setTimerI(final JobKey job) {
        timerI = job;
    }

    public void cancelTimerA() {
        if (timerA != null) {
            TimerService.getInstance().deleteTimer(timerA);
        }
    }

    public void cancelTimerB() {
        if (timerB != null) {
            TimerService.getInstance().deleteTimer(timerB);
        }
    }
    
    public void cancelTimerI() {
        if (timerI != null) {
            TimerService.getInstance().deleteTimer(timerI);
        }
    }

    public void addTimer(final JobKey job) {
        synchronized (timers) {
            timers.add(job);
        }
    }
    
    public void destroy() {
        terminated = true;
        synchronized (timers) {
            TimerService.getInstance().deleteTimers(timers);
        }
    }
    
    /**
     * 
     * @return 
     */
    public boolean isTerminated() {
        return terminated;
    }
    
    /**
     *
     */
    protected void setTerminated() {
        terminated = true;
    }
    
    protected Transaction() {
        timers = new ArrayList<JobKey>();
        terminated = false;
    }
}
