package org.andrewwinter.jsr289.util;

import org.andrewwinter.jsr289.threadlocal.ServletNameThreadLocal;
import org.andrewwinter.jsr289.threadlocal.ServletContextThreadLocal;
import org.andrewwinter.jsr289.threadlocal.AppNameThreadLocal;
import org.andrewwinter.jsr289.threadlocal.MainServletNameThreadLocal;
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
import javax.servlet.sip.SipServletListener;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSessionActivationListener;
import javax.servlet.sip.SipSessionAttributeListener;
import javax.servlet.sip.SipSessionBindingListener;
import javax.servlet.sip.SipSessionListener;
import javax.servlet.sip.TimerListener;
import javax.servlet.sip.TooManyHopsException;
import org.andrewwinter.jsr289.api.InboundSipServletRequestImpl;
import org.andrewwinter.jsr289.api.InboundSipServletResponseImpl;
import org.andrewwinter.jsr289.api.OutboundSipServletResponseImpl;
import org.andrewwinter.jsr289.model.SipServletDelegate;

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
            final SipServletDelegate servlet,
            final InboundSipServletRequestImpl request,
            InboundSipServletResponseImpl response,
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
            
                final OutboundSipServletResponseImpl outboundResponse = (OutboundSipServletResponseImpl) request.createResponse(SipServletResponse.SC_TOO_MANY_HOPS);
                outboundResponse.send();
            } else {
                throw e;
            }
        }
    }
}
