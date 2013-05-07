package org.andrewwinter.sip.webapp;

import java.util.List;
import java.util.Locale;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import org.andrewwinter.sip.model.Extension;
import org.andrewwinter.sip.model.Pbx;
import org.andrewwinter.sip.model.Queries;
import org.andrewwinter.sip.model.Subscriber;

/**
 *
 * @author andrew
 */
@Stateless
public class DataManager {
    @PersistenceContext(type = PersistenceContextType.TRANSACTION, unitName = "sipappPersistenceUnit")
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void createPbx(
            final String domain,
            final String email,
            final String forename,
            final String surname,
            final String password) {
        
        final Pbx pbx = new Pbx(domain, 5);
        em.persist(pbx);
        
        final Subscriber user = new Subscriber(pbx, forename, surname, email, password, true);
        em.persist(user);
    }
    
    public Subscriber getUserByEmail(final String email) {
        final TypedQuery<Subscriber> query = em.createNamedQuery(Queries.FIND_SUBSCRIBER_BY_EMAIL, Subscriber.class);
        query.setParameter("email", email.toLowerCase(Locale.US));
        final List<Subscriber> users = query.getResultList();
        if (users != null && !users.isEmpty()) {
            return users.get(0);
        } else {
            return null;
        }
    }
    
    public List<Extension> findExtensionsInUse(final Pbx pbx) {
        final TypedQuery<Extension> query = em.createNamedQuery(Queries.FIND_EXTENSIONS_IN_USE, Extension.class);
        query.setParameter("domainName", pbx.getDomainName().toLowerCase(Locale.US));
        return query.getResultList();
    }
}
