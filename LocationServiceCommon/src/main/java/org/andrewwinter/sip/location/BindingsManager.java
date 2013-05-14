package org.andrewwinter.sip.location;

import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.URI;
import org.andrewwinter.sip.model.Binding;
import org.andrewwinter.sip.model.Queries;
import org.andrewwinter.sip.model.Subscriber;

/**
 *
 * @author andrew
 */
@Stateless
public class BindingsManager {

    @PersistenceContext(type = PersistenceContextType.TRANSACTION, unitName = "sipappPersistenceUnit")
    private EntityManager em;

    /**
     * @param uri Canonicalized AOR of subscriber.
     * @return 
     */
    public Subscriber getSubscriber(final SipURI uri) {
        final TypedQuery<Subscriber> query = em.createNamedQuery(Queries.FIND_SUBSCRIBER_BY_USER_PART, Subscriber.class);
        
        query.setParameter("userPart", uri.getUser());
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<Binding> getBindings(final Subscriber subscriber) {
        final TypedQuery<Binding> query = em.createNamedQuery("Binding.findBindings", Binding.class);
        query.setParameter("subscriber", subscriber);
        return query.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void put(final List<Binding> bindings) {
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
    public void removeBinding(final Subscriber subscriber, final String contactAddress) {
        final Query query = em.createNamedQuery(Binding.DELETE_BINDING);
        query.setParameter("subscriber", subscriber);
        query.setParameter("contactAddress", contactAddress);
        query.executeUpdate();
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void removeExpiredBindingsForSubscriber(final Subscriber subscriber) {
        final Query query = em.createNamedQuery(Queries.DELETE_EXPIRED_BINDINGS_FOR_SUBSCRIBER);
        query.setParameter("subscriber", subscriber);
        query.setParameter("expiryTime", new Date());
        query.executeUpdate();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addAndRemoveBindings(
            final Subscriber subscriber,
            final List<Binding> bindingsToAdd,
            final Set<String> contactAddressesToRemove,
            final SipFactory sf) {

        // The binding updates MUST be committed (that is, made visible to the
        // proxy or redirect server) if and only if all binding updates and
        // additions succeed. If any one of them fails (for example, because the
        // back-end database commit failed), the request MUST fail with a 500
        // (Server Error) response and all tentative binding updates MUST be
        // removed.

        final List<Binding> allBindings = getBindings(subscriber);

        for (final Binding existingBinding : allBindings) {

            try {
                URI uriFromExistingBinding = sf.createURI(existingBinding.getContactAddress());

                for (final String contactAddressToRemove : contactAddressesToRemove) {

                    final URI uriToRemove = sf.createURI(contactAddressToRemove);
                    if (Util.equalsUsingComparisonRules((SipURI) uriToRemove, (SipURI) uriFromExistingBinding)) {
                        removeBinding(subscriber, existingBinding.getContactAddress());
                    }
                }

            } catch (ServletParseException e) {
                // Should never happen
            }
        }

        put(bindingsToAdd);
    }

    /**
     *
     * @param canonicalizedPublicUri
     * @param contact
     * @return
     */
    public Binding getBinding(final Subscriber subscriber, final URI contact, final SipFactory sf) {

        Binding result = null;

        // the registrar then searches the list of current bindings using the
        // URI comparison rules.

        final List<Binding> bindings = getBindings(subscriber);
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
}
