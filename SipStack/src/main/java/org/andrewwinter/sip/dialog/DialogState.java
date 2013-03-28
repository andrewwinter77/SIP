package org.andrewwinter.sip.dialog;

/**
 * A dialog can be in the "early" state, which occurs when it is created with a
 * provisional response, and then transition to the "confirmed" state when a 2xx
 * final response arrives.
 */
public enum DialogState {
    
    /**
     *
     */
    EARLY,
    /**
     *
     */
    CONFIRMED
}
