/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.sip.timer;

/**
 *
 * @author andrew
 */
public class Timer {
    
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
}
