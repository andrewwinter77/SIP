package org.andrewwinter.sip.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author andrewwinter77
 */
public final class Util {
    
    private static String ipAddress = null;

    /**
     * Prevents instantiation.
     */
    private Util() {
    }
    
    /**
     *
     * @return
     */
    public static String getIpAddress() {
        if (ipAddress == null) {
            try {
                ipAddress = System.getProperty("org.andrewwinter.sip.sentby");
                if (ipAddress == null) {
                    ipAddress = InetAddress.getLocalHost().getHostAddress();
                }
            } catch (UnknownHostException e) {
                // TODO: Handle UnknownHostException
            }
        }
        return ipAddress;
    }
}
