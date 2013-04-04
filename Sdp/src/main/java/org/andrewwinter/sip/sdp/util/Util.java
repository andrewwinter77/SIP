package org.andrewwinter.sip.sdp.util;

/**
 *
 * @author andrewwinter77
 */
public final class Util {
        
    /**
     *
     */
    public static final String CRLF = "\r\n";

    /**
     *
     */
    public static final String NEWLINE = "\n";
    
    /**
     * 
     * @param source
     * @return 
     */
    public static String replaceCRLFwithLF(final String source) {
        return source.replaceAll(Util.CRLF, Util.NEWLINE);
    }
}
