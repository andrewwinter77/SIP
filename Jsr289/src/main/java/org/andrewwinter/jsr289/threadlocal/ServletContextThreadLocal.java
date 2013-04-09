package org.andrewwinter.jsr289.threadlocal;

import javax.servlet.ServletContext;

/**
 *
 * @author andrew
 */
public class ServletContextThreadLocal {

    private static final ThreadLocal<ServletContext> tl = new ThreadLocal();

    public static void set(final ServletContext servletContext) {
        tl.set(servletContext);
    }

    public static void unset() {
        tl.remove();
    }

    public static ServletContext get() {
        return tl.get();
    }
}
