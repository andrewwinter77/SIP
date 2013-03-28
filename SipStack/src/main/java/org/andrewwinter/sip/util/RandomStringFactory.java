package org.andrewwinter.sip.util;

import java.util.Random;

/**
 *
 * @author andrewwinter77
 */
public final class RandomStringFactory {

    private static final char[] ALPHABET = new char[] {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
        'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
        's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '-', '.', '!', '_', '+'};
    
    // The 289 TCK tests fail when we generate a tag with a %. The tests (SIP Unit?)
    // strip off the % and everything after it. This is clearly a bug in the test
    // framework but we'll work around it here by not generating tags with a %.
    // , '%'};
    
    /**
     * Prevents instantiation.
     */
    private RandomStringFactory() {
    }
    
    /**
     *
     * @param length
     * @return
     */
    public static String generate(final int length) {
        final Random rnd = new Random();
        final char[] tag = new char[length];
        for (int i=0; i<length; ++i) {
            tag[i] = ALPHABET[rnd.nextInt(ALPHABET.length)];
        }
        return new String(tag);
    }
    
    /**
     *
     * @return
     */
    public static String generateTag() {
        return generate(10);
    }
    
    /**
     *
     * @return
     */
    public static String generateCallId() {
        return generate(20);
    }
}
