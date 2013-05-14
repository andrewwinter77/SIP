package org.andrewwinter.sip.location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.sip.Proxy;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.URI;
import org.andrewwinter.sip.model.Binding;
import org.andrewwinter.sip.model.Subscriber;

@javax.servlet.sip.annotation.SipServlet
public class LocationSipServlet extends SipServlet {
    
    @Resource
    private SipFactory sf;
    
    private static BindingsManager getBindingsManager() {
        try {
            final InitialContext ic = new InitialContext();
            return (BindingsManager) ic.lookup("java:global/LocationService-1.0-SNAPSHOT/BindingsManager!org.andrewwinter.sip.location.BindingsManager");
        } catch (NamingException e) {
            return null;
        }
    }
    
    private static void deleteExpiredBindings(final Subscriber subscriber) {
        getBindingsManager().removeExpiredBindingsForSubscriber(subscriber);
    }

    @Override
    protected void doInvite(final SipServletRequest invite) throws ServletException, IOException {
        
        final List<URI> destUris = new ArrayList<>();
        
        final URI requestUri = invite.getRequestURI();
        
        if (requestUri instanceof SipURI) {
            final SipURI canonicalizedUri = Util.canonicalizeUri((SipURI) requestUri.clone());
            final Subscriber subscriber = getBindingsManager().getSubscriber(canonicalizedUri);
            
            deleteExpiredBindings(subscriber);
            
            final List<Binding> bindings = getBindingsManager().getBindings(subscriber);
            if (bindings != null) {
                for (final Binding binding : bindings) {
                    final URI destUri = sf.createURI(binding.getContactAddress());
                    destUris.add(destUri);
                }
            }
        }
        
        if (destUris.isEmpty()) {
            destUris.add(requestUri);
        }
        
        final Proxy proxy = invite.getProxy();
        proxy.setRecordRoute(true);
        proxy.proxyTo(destUris);
    }
}
