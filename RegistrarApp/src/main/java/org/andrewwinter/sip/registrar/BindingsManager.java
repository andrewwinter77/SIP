package org.andrewwinter.sip.registrar;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author andrew
 */
@Stateless
public class BindingsManager {

    @PersistenceContext(type=PersistenceContextType.TRANSACTION, unitName="sipappPersistenceUnit")
    private EntityManager em;
    
    
}
