package org.andrewwinter.sip.webapp;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import org.andrewwinter.sip.kitchensink.model.Pbx;

/**
 *
 * @author andrew
 */
@Stateless
public class PbxManager {
    @PersistenceContext(type = PersistenceContextType.TRANSACTION, unitName = "sipappPersistenceUnit")
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void createPbx(
            final String domain,
            final String email,
            final String password) {
        
        final Pbx pbx = new Pbx(domain);
        put(pbx);
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void put(final Pbx pbx) {
        em.persist(pbx);
    }
}
