package org.andrewwinter.jsr289.util;

/**
 * Implementations of this class are used to instantiate SipServlets,
 * SIP listeners etc. Implementations must take care of dependency injection
 * by way of @Resource annotations, etc.
 * 
 * @author andrew
 */
public interface ManagedClassInstantiator {
    
    Object instantiate(Class clazz);
}
