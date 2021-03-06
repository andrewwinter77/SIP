package org.andrewwinter.jsr289.util;

import java.io.Serializable;
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
import javax.servlet.sip.ar.SipApplicationRoutingDirective;
import org.andrewwinter.jsr289.api.OutboundSipServletResponseImpl;
import org.andrewwinter.jsr289.api.SipServletRequestImpl;
import org.andrewwinter.jsr289.api.SipServletResponseImpl;
import org.andrewwinter.jsr289.model.SipServletDelegate;
import org.andrewwinter.sip.parser.HeaderName;
import org.andrewwinter.sip.parser.SipRequest;

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

    /**
     * Send request back into the container so we can continue the application
     * sequencing.
     */
    public static void pushRoute(final SipRequest request, final Serializable stateInfo, final SipApplicationRoutingDirective directive) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<sip:127.0.0.1;lr");
        if (stateInfo != null) {
            sb.append(";si=").append(String.valueOf((Integer) stateInfo));
        }
        if (directive != null) {
            sb.append(";directive=").append(directive);
        }
        sb.append(">");
        request.pushHeader(HeaderName.ROUTE, sb.toString());
    }
    
    
    public static void invokeServlet(
            final SipServletDelegate servlet,
            final SipServletRequestImpl request,
            final SipServletResponseImpl response,
            final ServletContext context,
            final String appName,
            final String mainServlet) throws Exception {

        if (servlet == null || context == null || appName == null || mainServlet == null) {
            throw new IllegalArgumentException("Null argument.");
        }
        ServletNameThreadLocal.set(servlet.getServletName());
        ServletContextThreadLocal.set(context);
        AppNameThreadLocal.set(appName);
        MainServletNameThreadLocal.set(mainServlet);
        
        if (request != null) {
            request.setServletContext(context);
        } else if (response != null) {
            response.setServletContext(context);
        }

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
