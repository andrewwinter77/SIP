package org.andrewwinter.sip.webapp;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import org.andrewwinter.sip.model.Pbx;
import org.andrewwinter.sip.model.User;

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
        
        final Pbx pbx = new Pbx(domain);
        em.persist(pbx);
        
        final User user = new User(pbx, forename, surname, email, password, true);
        em.persist(user);
    }
}
