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

/**
 *
 * @author andrewwinter77
 */
@Table(name="users")
@NamedQueries(value = {
    @NamedQuery(name="User.findUserByEmail", query="SELECT u FROM User u WHERE u.email=:email"), // TODO: AND pbx.domainName = ...
//    @NamedQuery(name="User.deleteBindings", query="DELETE FROM User u WHERE b.publicAddress=:publicAddress"),
//    @NamedQuery(name="User.deleteBinding", query="DELETE FROM User u WHERE b.publicAddress=:publicAddress AND b.contactAddress=:contactAddress"),
//    @NamedQuery(name="User.deleteExpiredBindings", query="DELETE FROM User u WHERE b.expiryTime<:expiryTime"),
//    @NamedQuery(name="User.deleteExpiredBindingsForPublicAddress", query="DELETE FROM User u WHERE b.publicAddress=:publicAddress AND b.expiryTime<:expiryTime")
})
@Entity
public class User implements Serializable {
    
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
    public User() {
    }
    
    public User(final Pbx pbx, final String forename, final String surname, final String email, final String password, final boolean adminUser) {
        this.pbx = pbx;
        this.forename = forename;
        this.surname = surname;
        this.email = email.toLowerCase(Locale.US);
        this.password = password;
        this.adminUser = adminUser;
//        this.userPart = userPart;
    }
    
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
    public String getSurname() {
        return surname;
    }
    
    /**
     *
     * @return
     */
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

    public boolean isAdminUser() {
        return adminUser;
    }

    public void setAdminUser(boolean adminUser) {
        this.adminUser = adminUser;
    }
}
