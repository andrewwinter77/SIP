package org.andrewwinter.sip.element;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.andrewwinter.sip.parser.SipUri;
import org.andrewwinter.sip.parser.Uri;

/**
 *
 * @author andrewwinter77
 */
public class LocationService {

    private static final LocationService INSTANCE  = new LocationService();
    
    private Map<String, Set<Binding>> allBindings;
    
    /**
     *
     * @return
     */
    public static LocationService getInstance() {
        return INSTANCE;
    }
    
    /**
     * Returns an unmodifiable copy of the set of bindings for the URI
     * or null if there are no bindings.
     * @param canonicalizedUri
     * @return 
     */
    public Set<Binding> getBindings(final String canonicalizedUri) {
        Set<Binding> set = allBindings.get(canonicalizedUri);
        if (set != null) {
            set = Collections.unmodifiableSet(set);
        }
        return set;
    }
    
//    public void removeBinding(final String canonicalizedUri, final Binding binding) {
//        final Set<Binding> set = allBindings.get(canonicalizedUri);
//        if (set != null) {
//            set.remove(binding);
//        }
//    }
    
    /**
     *
     * @param canonicalizedUri
     * @param bindings
     */
    public void addBindings(final String canonicalizedUri, final Set<Binding> bindings) {
        
        // The binding updates MUST be committed (that is, made visible to the
        // proxy or redirect server) if and only if all binding updates and
        // additions succeed. If any one of them fails (for example, because the
        // back-end database commit failed), the request MUST fail with a 500
        // (Server Error) response and all tentative binding updates MUST be
        // removed.
        
        Set<Binding> existingBindings = allBindings.get(canonicalizedUri);
        if (existingBindings == null) {
            existingBindings = new HashSet<Binding>();
            allBindings.put(canonicalizedUri, existingBindings);
        }
        existingBindings.addAll(bindings);
    }

    /**
     *
     * @param canonicalizedUri
     * @param bindings
     */
    public void removeBindings(final String canonicalizedUri, final Set<Binding> bindings) {
        
        // The binding updates MUST be committed (that is, made visible to the
        // proxy or redirect server) if and only if all binding updates and
        // additions succeed. If any one of them fails (for example, because the
        // back-end database commit failed), the request MUST fail with a 500
        // (Server Error) response and all tentative binding updates MUST be
        // removed.
        
        final Set<Binding> set = allBindings.get(canonicalizedUri);
        if (set == null) {
            System.out.println("Removing bindings when no bindings exist.");
        } else {
            for (final Binding binding : bindings) {
                set.remove(binding);
            }
        }
    }

    /**
     *
     * @param canonicalizedUri
     * @param contact
     * @return
     */
    public Binding getBinding(final String canonicalizedUri, final Uri contact) {
        
        Binding result = null;
        
        // the registrar then searches the list of current bindings using the
        // URI comparison rules.
        
        final Set<Binding> bindings = allBindings.get(canonicalizedUri);
        if (bindings != null) {
            for (final Binding binding : bindings) {
                
                // TODO: Support non-SIP URIs
                final SipUri storedUri = (SipUri) binding.getUri();
                if (storedUri.equalsUsingComparisonRules((SipUri) contact)) {
                    result = binding;
                    break;
                }
            }
        }
        
        return result;
    }

    /**
     * Assumes all arguments are non-null. Bindings to removed are processed first
     * followed by bindings to add. To update a binding, add it to the set to be
     * removed and an updated instance to the set to be added.
     * @param canonicalizedUri
     * @param bindingsToAdd
     * @param bindingsToRemove 
     */
    public void applyBindingsChanges(
            final String canonicalizedUri,
            final Set<Binding> bindingsToAdd,
            final Set<Binding> bindingsToRemove) {
        
        // The binding updates MUST be committed (that is, made visible to the
        // proxy or redirect server) if and only if all binding updates and
        // additions succeed. If any one of them fails (for example, because the
        // back-end database commit failed), the request MUST fail with a 500
        // (Server Error) response and all tentative binding updates MUST be
        // removed.
        
        Set<Binding> bindings = allBindings.get(canonicalizedUri);
        if (bindings == null) {
            bindings = new HashSet<Binding>();
            allBindings.put(canonicalizedUri, bindings);
        }
        
        
        final Iterator<Binding> iter = bindings.iterator();
        while (iter.hasNext()) {
            
            final Binding existingBinding = iter.next();
            
            for (final Binding binding : bindingsToRemove) {
                if (((SipUri) binding.getUri()).equalsUsingComparisonRules((SipUri) existingBinding.getUri())) {
                    iter.remove();
                }
            }
        }
        
        bindings.addAll(bindingsToAdd);
        if (bindings.isEmpty()) {
            allBindings.remove(canonicalizedUri);
        }
    }
    
    
    private LocationService() {
        this.allBindings = new HashMap<String, Set<Binding>>();
    }
}
