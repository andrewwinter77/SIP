package org.andrewwinter.sip.registrar;

import java.io.Serializable;
import java.util.Date;
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


// TODO: Implement hashcode and equals - it's important we do this for this class
@Table(name="bindings")
@NamedQueries(value = {
    @NamedQuery(name="Binding.findBindings", query="SELECT b FROM Binding b WHERE b.publicAddress=:publicAddress"),
    @NamedQuery(name="Binding.deleteBindings", query="DELETE FROM Binding b WHERE b.publicAddress=:publicAddress"),
    @NamedQuery(name="Binding.deleteExpiredBindings", query="DELETE FROM Binding b WHERE b.expiryTime<:expiryTime")
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
    
    @Column(name="public_address", nullable=false, unique=false)
    private String publicAddress;

    @Column(name="contact_address", nullable=false, unique=false)
    private String contactAddress;
    
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
     * @param publicAddress
     */
    public Binding(final String callId, final int cseq, final String contactAddress, final Date expiryTime, final String publicAddress) {
        this.callId = callId;
        this.cseqValue = cseq;
        this.contactAddress = contactAddress;
        this.expiryTime = expiryTime;
        this.publicAddress = publicAddress;
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

    public String getPublicAddress() {
        return publicAddress;
    }

    public void setPublicAddress(final String publicAddress) {
        this.publicAddress = publicAddress;
    }
}
