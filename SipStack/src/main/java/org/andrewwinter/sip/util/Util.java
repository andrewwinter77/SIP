package org.andrewwinter.sip.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author andrewwinter77
 */
public class Util {
    
    private static String ipAddress = null;
    
    /**
     *
     * @return
     */
    public static String getIpAddress() {
        if (ipAddress == null) {
            try {
                ipAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                // TODO: Handle UnknownHostException
            }
        }
        return ipAddress;
    }
}
