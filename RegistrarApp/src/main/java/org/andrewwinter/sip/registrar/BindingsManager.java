package org.andrewwinter.sip.registrar;

import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.URI;

/**
 *
 * @author andrew
 */
@Stateless
public class BindingsManager {

    @PersistenceContext(type = PersistenceContextType.TRANSACTION, unitName = "sipappPersistenceUnit")
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<Binding> getBindings(final String publicAddress) {
        final TypedQuery<Binding> query = em.createNamedQuery("Binding.findBindings", Binding.class);
        query.setParameter("publicAddress", publicAddress);
        return query.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void put(final String publicAddress, final List<Binding> bindings) {
        for (final Binding b : bindings) {
            em.persist(b);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeBindings(final String publicAddress) {
        final TypedQuery<Binding> query = em.createNamedQuery("Binding.deleteBindings", Binding.class);
        query.setParameter("publicAddress", publicAddress);
        query.executeUpdate();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeBinding(final String publicAddress, final String contactAddress) {
        final Query query = em.createNamedQuery("Binding.deleteBinding");
        query.setParameter("publicAddress", publicAddress);
        query.setParameter("contactAddress", contactAddress);
        query.executeUpdate();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addAndRemoveBindings(
            final String canonicalizedUri,
            final List<Binding> bindingsToAdd,
            final Set<String> contactAddressesToRemove,
            final SipFactory sf) {

        // The binding updates MUST be committed (that is, made visible to the
        // proxy or redirect server) if and only if all binding updates and
        // additions succeed. If any one of them fails (for example, because the
        // back-end database commit failed), the request MUST fail with a 500
        // (Server Error) response and all tentative binding updates MUST be
        // removed.

        final List<Binding> allBindings = getBindings(canonicalizedUri);

        for (final Binding existingBinding : allBindings) {
        
            try {
                URI uriFromExistingBinding = sf.createURI(existingBinding.getContactAddress());

                for (final String contactAddressToRemove : contactAddressesToRemove) {

                    final URI uriToRemove = sf.createURI(contactAddressToRemove);
                    if (Util.equalsUsingComparisonRules((SipURI) uriToRemove, (SipURI) uriFromExistingBinding)) {
                        removeBinding(canonicalizedUri, existingBinding.getContactAddress());
                    }
                }

            } catch (ServletParseException e) {
                // Should never happen
            }
        }

        put(canonicalizedUri, bindingsToAdd);
    }
    
}
