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
    @NamedQuery(name = "Pbx.findPbx", query = "SELECT p FROM Pbx p WHERE p.domainName=:domainName"),
    @NamedQuery(name = Queries.GET_MAX_PBX_USER_PREFIX, query = "SELECT MAX(p.userPartPrefix) FROM Pbx p")
})
@Entity
public class Pbx implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "domain_name", nullable = false, unique = false)
    private String domainName;

    @Column(name = "max_extensions", nullable = false, unique = false)
    private int maxExtensions;
    
    @Column(name = "user_part_prefix", nullable = false, unique = true)
    private int userPartPrefix;
    
    /**
     * The entity class must have a no-arg constructor. It may have other
     * constructors as well. The no-arg constructor must be public or protected.
     */
    public Pbx() {
    }

    public Pbx(final String domainName, final int maxExtensions, final int userPartPrefix) {
        this.domainName = domainName;
        this.maxExtensions = maxExtensions;
        this.userPartPrefix = userPartPrefix;
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

    public int getMaxExtensions() {
        return maxExtensions;
    }

    public void setMaxExtensions(final int maxExtensions) {
        this.maxExtensions = maxExtensions;
    }

    public int getUserPartPrefix() {
        return userPartPrefix;
    }

    public void setUserPartPrefix(final int prefix) {
        this.userPartPrefix = prefix;
    }
}
