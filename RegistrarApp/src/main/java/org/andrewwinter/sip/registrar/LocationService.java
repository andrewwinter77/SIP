package org.andrewwinter.sip.registrar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.URI;

/**
 *
 * @author andrewwinter77
 */
public class LocationService {

    private static final LocationService INSTANCE = new LocationService();
    private BindingsManager bindingsManager;
//    private Map<String, Set<Binding>> allBindings;

    private BindingsManager getBindingsManager() {
        if (bindingsManager == null) {
            try {
                final InitialContext ic = new InitialContext();
                bindingsManager = (BindingsManager) ic.lookup("java:global/RegistrarApp-1.0-SNAPSHOT/BindingsManager!org.andrewwinter.sip.registrar.BindingsManager");
            } catch (NamingException e) {
            }
        }
        return bindingsManager;
    }

    /**
     *
     * @return
     */
    public static LocationService getInstance() {
        return INSTANCE;
    }

    /**
     * Looks up and returns the set of contact addresses registered to the given
     * public address.
     *
     * @param canonicalizedUri
     * @return Returns an unmodifiable copy of the set of bindings for the URI
     * or null if there are no bindings.
     */
    public List<Binding> getBindings(final String canonicalizedUri) {
        List<Binding> bindings = getBindingsManager().getBindings(canonicalizedUri);
        if (bindings != null) {
            bindings = Collections.unmodifiableList(bindings);
        }
        return bindings;
    }

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

        List<Binding> existingBindings = getBindingsManager().getBindings(canonicalizedUri);
        if (existingBindings == null) {
            existingBindings = new ArrayList<>();
            getBindingsManager().put(canonicalizedUri, existingBindings);
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

        final List<Binding> set = getBindingsManager().getBindings(canonicalizedUri);
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
     * @param canonicalizedPublicUri
     * @param contact
     * @return
     */
    public Binding getBinding(final String canonicalizedPublicUri, final URI contact, final SipFactory sf) {

        Binding result = null;

        // the registrar then searches the list of current bindings using the
        // URI comparison rules.

        final List<Binding> bindings = getBindingsManager().getBindings(canonicalizedPublicUri);
        if (bindings != null) {
            for (final Binding binding : bindings) {

                // TODO: Support non-SIP URIs

                try {
                    URI uriInBinding = sf.createURI(binding.getContactAddress());
                    if (Util.equalsUsingComparisonRules((SipURI) uriInBinding, (SipURI) contact)) {
                        result = binding;
                        break;
                    }
                } catch (ServletParseException e) {
                    // Should never happen
                }
            }
        }

        return result;
    }

    /**
     * Assumes all arguments are non-null. Bindings to removed are processed
     * first followed by bindings to add. To update a binding, add it to the set
     * to be removed and an updated instance to the set to be added.
     *
     * @param canonicalizedUri
     * @param bindingsToAdd
     * @param bindingsToRemove
     */
    public void applyBindingsChanges(
            final String canonicalizedUri,
            final List<Binding> bindingsToAdd,
            final Set<String> contactAddressesToRemove,
            final SipFactory sf) {

        getBindingsManager().addAndRemoveBindings(canonicalizedUri, bindingsToAdd, contactAddressesToRemove, sf);
    }

    private LocationService() {
    }
}
