/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.sip.gui;

import java.io.File;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.Player;
import javax.media.Time;

/**
 *
 * @author andrewwinter77
 */
public class AudioManager implements ControllerListener {
    
    private File ringingWav;
    
    private Player player;
    
    private static AudioManager INSTANCE = new AudioManager();
    
    private boolean isRinging;
    
    /**
     *
     * @return
     */
    public static AudioManager getInstance() {
        return INSTANCE;
    }
    
    private AudioManager() {
        isRinging = false;
    }
    
    /**
     *
     */
    public void startRinging() {
        isRinging = true;
        player.setMediaTime(new Time(0));
        player.start();
    }
    
    /**
     *
     */
    public void stopRinging() {
        isRinging = false;
        player.stop();
    }
    
    /**
     *
     * @throws Exception
     */
    public void initialize() throws Exception {
        ringingWav = new File("audio/alerting.wav");
        player = Manager.createRealizedPlayer(ringingWav.toURI().toURL());
        player.addControllerListener(this);
        
        // Processes as much data as necessary to reduce the Controller's start
        // latency to the shortest possible time. This typically involves
        // examining media data and takes some time to complete.
        player.prefetch();
    }

    /**
     *
     * @param ce
     */
    @Override
    public void controllerUpdate(ControllerEvent ce) {
        if (ce instanceof EndOfMediaEvent && isRinging) {
            startRinging();
        }
    }
}
