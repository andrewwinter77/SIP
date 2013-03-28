package org.andrewwinter.jsr289;

/**
 *
 * @author andrew
 */
public class MainServletNameThreadLocal {

    private static final ThreadLocal<String> tl = new ThreadLocal();

    public static void set(final String mainServletName) {
        tl.set(mainServletName);
    }

    public static void unset() {
        tl.remove();
    }

    public static String get() {
        return tl.get();
    }
}
