package org.andrewwinter.jsr289.threadlocal;

/**
 *
 * @author andrew
 */
public class AppNameThreadLocal {

    private static final ThreadLocal<String> tl = new ThreadLocal();

    public static void set(final String appName) {
        tl.set(appName);
    }

    public static void unset() {
        tl.remove();
    }

    public static String get() {
        return tl.get();
    }
}
