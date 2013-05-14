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

        final TypedQuery<Integer> query = em.createNamedQuery(Queries.GET_MAX_PBX_USER_PREFIX, Integer.class);
        final Integer max = query.getSingleResult();
        final int userPartPrefix;
        if (max == null || max == 0) {
            userPartPrefix = 1000;
        } else {
            userPartPrefix = max + 1;
        }
        
        final Pbx pbx = new Pbx(domain, 5, userPartPrefix);
        em.persist(pbx);
        
        createSubscriber(pbx, forename, surname, email, password, true);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createSubscriber(
            final Pbx pbx,
            final String forename,
            final String surname,
            final String email,
            final String password,
            final boolean admin) {
        
        final TypedQuery<Integer> query = em.createNamedQuery(Queries.GET_MAX_EXTENSION_FOR_PBX, Integer.class);
        query.setParameter("pbx", pbx);
        final Integer max = query.getSingleResult();
        final int extension;
        if (max == null || max == 0) {
            extension = 1000;
        } else {
            extension = max + 1;
        }
        
        final Subscriber subscriber = new Subscriber(
                                        pbx,
                                        forename,
                                        surname,
                                        email,
                                        password,
                                        String.valueOf(pbx.getUserPartPrefix()) + String.valueOf(extension),
                                        extension,
                                        admin);
        em.persist(subscriber);
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
    
    public List<Subscriber> getSubscribersInPbx(final Pbx pbx) {
        final TypedQuery<Subscriber> query = em.createNamedQuery(Queries.GET_SUBSCRIBERS_IN_PBX, Subscriber.class);
        query.setParameter("pbx", pbx);
        return query.getResultList();
    }
}
