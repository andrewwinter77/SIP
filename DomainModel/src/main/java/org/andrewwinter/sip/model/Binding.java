package org.andrewwinter.sip.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author andrewwinter77
 */
@Table(name="bindingstemp")
@NamedQueries(value = {
//    @NamedQuery(name="Binding.findBindings", query="SELECT b FROM Binding b WHERE b.publicAddress=:publicAddress"),
//    @NamedQuery(name="Binding.deleteBindings", query="DELETE FROM Binding b WHERE b.publicAddress=:publicAddress"),
//    @NamedQuery(name="Binding.deleteBinding", query="DELETE FROM Binding b WHERE b.publicAddress=:publicAddress AND b.contactAddress=:contactAddress"),
//    @NamedQuery(name="Binding.deleteExpiredBindings", query="DELETE FROM Binding b WHERE b.expiryTime<:expiryTime"),
//    @NamedQuery(name="Binding.deleteExpiredBindingsForPublicAddress", query="DELETE FROM Binding b WHERE b.publicAddress=:publicAddress AND b.expiryTime<:expiryTime")
})
@Entity
public class Binding implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
  
    @Column(name="call_id", nullable=false, unique=false)
    private String callId;
    
    @Column(name="cseq", nullable=false, unique=false)
    private int cseqValue;
    
    @ManyToOne
    @JoinColumn(name="subscriber", nullable=false, unique=false)
    private Subscriber subscriber;

    @Column(name="contact_address", nullable=false, unique=false)
    private String contactAddress;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="expiry_time", nullable=false, unique=false)
    private Date expiryTime;

    /**
     * The entity class must have a no-arg constructor. It may have other
     * constructors as well. The no-arg constructor must be public or protected.
     */
    public Binding() {
    }
    
    /**
     * 
     * @param callId
     * @param cseq
     * @param contactAddress Contact URI.
     * @param expiryTime  
     * @param subscriber
     */
    public Binding(final String callId, final int cseq, final String contactAddress, final Date expiryTime, final Subscriber subscriber) {
        this.callId = callId;
        this.cseqValue = cseq;
        this.contactAddress = contactAddress;
        this.expiryTime = expiryTime;
        this.subscriber = subscriber;
    }
    
    /**
     *
     * @return
     */
    public String getCallId() {
        return callId;
    }
    
    /**
     *
     * @return
     */
    public int getCseqValue() {
        return cseqValue;
    }
    
    /**
     *
     * @return
     */
    public String getContactAddress() {
        return contactAddress;
    }
    
    /**
     *
     * @return
     */
    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setCallId(final String callId) {
        this.callId = callId;
    }

    public void setCseqValue(final int cseqValue) {
        this.cseqValue = cseqValue;
    }

    public void setContactAddress(final String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public void setExpiryTime(final Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setUser(final Subscriber pbxUser) {
        this.subscriber = pbxUser;
    }
}
