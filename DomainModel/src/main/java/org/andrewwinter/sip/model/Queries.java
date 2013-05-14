/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.sip.model;

/**
 *
 * @author andrew
 */
public class Queries {
    
    public static final String FIND_SUBSCRIBER_BY_EMAIL = "Subscriber.findSubscriberByEmail";
    
    public static final String FIND_SUBSCRIBER_BY_USER_PART = "Subscriber.findSubscriberBySipAddress";
    
    public static final String GET_SUBSCRIBERS_IN_PBX = "Subscriber.getUsersInPbx";
    
    public static final String GET_MAX_PBX_USER_PREFIX = "Pbx.findMaxUserPrefix";
    
    public static final String GET_MAX_EXTENSION_FOR_PBX = "Subscriber.findMaxUserPartForPbx";
    
    public static final String DELETE_EXPIRED_BINDINGS_FOR_SUBSCRIBER = "Binding.deleteExpiredBindingsForSubscriber";
    
    private Queries() {
    }
}
