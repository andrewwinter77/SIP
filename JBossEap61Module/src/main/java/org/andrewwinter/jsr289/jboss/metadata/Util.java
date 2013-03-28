package org.andrewwinter.jsr289.jboss.metadata;

/**
 *
 * @author andrew
 */
class Util {

    static boolean isEmpty(final String str) {
        return str == null || str.isEmpty();
    }
    
    /**
     * Returns the string or {@code null} if the string is either {@code null}
     * or the empty string.
     * @param str
     * @return 
     */
    static String normalize(final String str) {
        if (isEmpty(str)) {
            return null;
        } else {
            return str;
        }
    }
}
