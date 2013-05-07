package org.andrewwinter.sip.model;

import java.io.Serializable;
import java.util.Locale;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andrewwinter77
 */
@Table(name="subscribers")
@NamedQueries(value = {
    @NamedQuery(name=Queries.FIND_SUBSCRIBER_BY_EMAIL, query="SELECT s FROM Subscriber s WHERE s.email=:email"),
//    @NamedQuery(name="Subscriber.deleteBindings", query="DELETE FROM Subscriber u WHERE b.publicAddress=:publicAddress"),
//    @NamedQuery(name="Subscriber.deleteBinding", query="DELETE FROM Subscriber u WHERE b.publicAddress=:publicAddress AND b.contactAddress=:contactAddress"),
//    @NamedQuery(name="Subscriber.deleteExpiredBindings", query="DELETE FROM Subscriber u WHERE b.expiryTime<:expiryTime"),
//    @NamedQuery(name="Subscriber.deleteExpiredBindingsForPublicAddress", query="DELETE FROM Subscriber u WHERE b.publicAddress=:publicAddress AND b.expiryTime<:expiryTime")
})
@Entity
@XmlRootElement(name="subscriber")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Subscriber implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="pbx", nullable=false, unique=false)
    private Pbx pbx;

    @Column(name="forename", nullable=false, unique=false)
    private String forename;
    
    @Column(name="surname", nullable=false, unique=false)
    private String surname;
    
    @Column(name="email", nullable=false, unique=true)
    private String email;
    
//    @Column(name="userpart", nullable=false, unique=false)
//    private String userPart;

    @Column(name="password", nullable=false, unique=false)
    private String password;

    @Column(name="admin_user", nullable=false, unique=false)
    private boolean adminUser;

    /**
     * The entity class must have a no-arg constructor. It may have other
     * constructors as well. The no-arg constructor must be public or protected.
     */
    public Subscriber() {
    }
    
    public Subscriber(final Pbx pbx, final String forename, final String surname, final String email, final String password, final boolean adminUser) {
        this.pbx = pbx;
        this.forename = forename;
        this.surname = surname;
        this.email = email.toLowerCase(Locale.US);
        this.password = password;
        this.adminUser = adminUser;
//        this.userPart = userPart;
    }
    
    @XmlElement(name="email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     */
    @XmlElement(name="surname")
    public String getSurname() {
        return surname;
    }
    
    /**
     *
     * @return
     */
    @XmlElement(name="forename")
    public String getForename() {
        return forename;
    }
    
    public void setSurname(final String surname) {
        this.surname = surname;
    }

    public void setForename(final String forename) {
        this.forename = forename;
    }

//    public String getUserPart() {
//        return userPart;
//    }
//
//    public void setUserPart(final String publicAddress) {
//        this.userPart = publicAddress;
//    }
    
    @XmlElement(name="pbx")
    public Pbx getPbx() {
        return pbx;
    }
    
    public void setPbx(final Pbx pbx) {
        this.pbx = pbx;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return this.password;
    }

    // TODO: Why is XmlElement being ignored?
    @XmlElement(name="isAdminUser")
    public boolean isAdminUser() {
        return adminUser;
    }

    public void setAdminUser(boolean adminUser) {
        this.adminUser = adminUser;
    }
}
