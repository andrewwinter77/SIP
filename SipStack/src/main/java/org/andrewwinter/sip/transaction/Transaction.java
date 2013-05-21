package org.andrewwinter.sip.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.andrewwinter.sip.timer.TimerName;
import org.andrewwinter.sip.timer.TimerService;
import org.quartz.JobKey;

/**
 *
 * @author andrew
 */
public abstract class Transaction {

    private boolean terminated;
    
    private Map<TimerName, JobKey> timers;
    
    public void setTimer(final TimerName name, final JobKey key) {
        timers.put(name, key);
    }
    
    public void cancelTimer(final TimerName name) {
        final JobKey key = timers.get(name);
        if (key != null) {
            TimerService.getInstance().deleteTimer(key);
        }
    }
    
    public void destroy() {
        terminated = true;
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
        timers = new HashMap<TimerName, JobKey>();
        terminated = false;
    }
}
