package org.andrewwinter.jsr289;

/**
 *
 * @author andrew
 */
public class ServletNameThreadLocal {

    private static final ThreadLocal<String> tl = new ThreadLocal();

    public static void set(final String servletName) {
        tl.set(servletName);
    }

    public static void unset() {
        tl.remove();
    }

    public static String get() {
        return tl.get();
    }
}
