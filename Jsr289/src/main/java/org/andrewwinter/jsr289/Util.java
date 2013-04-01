package org.andrewwinter.jsr289;

import java.util.Collections;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.sip.SipApplicationSessionActivationListener;
import javax.servlet.sip.SipApplicationSessionAttributeListener;
import javax.servlet.sip.SipApplicationSessionBindingListener;
import javax.servlet.sip.SipApplicationSessionListener;
import javax.servlet.sip.SipErrorListener;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletListener;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSessionActivationListener;
import javax.servlet.sip.SipSessionAttributeListener;
import javax.servlet.sip.SipSessionBindingListener;
import javax.servlet.sip.SipSessionListener;
import javax.servlet.sip.TimerListener;
import javax.servlet.sip.TooManyHopsException;

/**
 *
 * @author andrew
 */
public class Util {

    public final static Set<Class<? extends EventListener>> LISTENER_CLASSES;

    static {
        final Set<Class<? extends EventListener>> mutable = new HashSet<>();
        mutable.add(SipApplicationSessionActivationListener.class);
        mutable.add(SipApplicationSessionAttributeListener.class);
        mutable.add(SipApplicationSessionBindingListener.class);
        mutable.add(SipApplicationSessionListener.class);
        mutable.add(SipErrorListener.class);
        mutable.add(SipServletListener.class);
        mutable.add(SipSessionActivationListener.class);
        mutable.add(SipSessionAttributeListener.class);
        mutable.add(SipSessionBindingListener.class);
        mutable.add(SipSessionListener.class);
        mutable.add(TimerListener.class);
        LISTENER_CLASSES = Collections.unmodifiableSet(mutable);
    }

    public static void invokeServlet(
            final SipServlet servlet,
            final SipServletRequestImpl request,
            SipServletResponseImpl response,
            final ServletContext context,
            final String appName,
            final String mainServlet) throws Exception {

        ServletNameThreadLocal.set(servlet.getServletName());
        ServletContextThreadLocal.set(context);
        AppNameThreadLocal.set(appName);
        MainServletNameThreadLocal.set(mainServlet);

        try {
            servlet.service(request, response);
        } catch (TooManyHopsException e) {
            if (request != null) {
                
                // If unhandled by the application, this will be caught by the
                // container which must then generate a 483 (Too many hops)
                // error response.
            
                response = (SipServletResponseImpl) request.createResponse(SipServletResponse.SC_TOO_MANY_HOPS);
                response.send();
            } else {
                throw e;
            }
        }
    }
}
