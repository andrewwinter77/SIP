package org.andrewwinter.sip.registrar;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;

/**
 *
 * @author andrew
 */
@Stateless
public class BindingsManager {

    @PersistenceContext(type = PersistenceContextType.TRANSACTION, unitName = "sipappPersistenceUnit")
    private EntityManager em;

    public List<Binding> getBindings(final String publicAddress) {
        final TypedQuery<Binding> query = em.createNamedQuery("Binding.findBindings", Binding.class);
        query.setParameter("publicAddress", publicAddress);
        return query.getResultList();
    }

    public void put(final String publicAddress, final List<Binding> bindings) {
        em.getTransaction().begin();
        for (final Binding b : bindings) {
            em.persist(b);
        }
        em.getTransaction().commit();
    }

    public void removeBindings(final String publicAddress) {
        final TypedQuery<Binding> query = em.createNamedQuery("Binding.deleteBindings", Binding.class);
        query.setParameter("publicAddress", publicAddress);
        query.executeUpdate();
    }
}
