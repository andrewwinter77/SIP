package org.andrewwinter.sip.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author andrewwinter77
 */
@Table(name = "pbxs")
@NamedQueries(value = {
    @NamedQuery(name = "Pbx.findPbx", query = "SELECT p FROM Pbx p WHERE p.domainName=:domainName")
})
@Entity
public class Pbx implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "domain_name", nullable = false, unique = false)
    private String domainName;

    /**
     * The entity class must have a no-arg constructor. It may have other
     * constructors as well. The no-arg constructor must be public or protected.
     */
    public Pbx() {
    }

    public Pbx(final String domainName) {
        this.domainName = domainName;
    }

    /**
     *
     * @return
     */
    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(final String domainName) {
        this.domainName = domainName;
    }
}
