/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.sip.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andrew
 */
@Table(name="extensions")
@NamedQueries(value = {
//    @NamedQuery(name="Extension.findExtensionByNumber", query="SELECT e FROM Extension e WHERE e.number=:number"),
    @NamedQuery(name=Queries.FIND_EXTENSIONS_IN_USE, query="SELECT e FROM Extension e JOIN e.pbx p WHERE p.domainName=:domainName AND e.subscriber IS NOT NULL"),
//    @NamedQuery(name="Subscriber.deleteBinding", query="DELETE FROM Subscriber u WHERE b.publicAddress=:publicAddress AND b.contactAddress=:contactAddress"),
//    @NamedQuery(name="Subscriber.deleteExpiredBindings", query="DELETE FROM Subscriber u WHERE b.expiryTime<:expiryTime"),
//    @NamedQuery(name="Subscriber.deleteExpiredBindingsForPublicAddress", query="DELETE FROM Subscriber u WHERE b.publicAddress=:publicAddress AND b.expiryTime<:expiryTime")
})
@Entity
@XmlRootElement(name="extension")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Extension implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="number", nullable=false, unique=false)
    private int number;
    
    @OneToOne
    @JoinColumn(name="subscriber", nullable=true, unique=false)
    private Subscriber subscriber;
    
    @ManyToOne
    @JoinColumn(name="pbx", nullable=false, unique=false)
    private Pbx pbx;
    
    public Extension() {
    }
    
    public Extension(final int number, final Subscriber subscriber, final Pbx pbx) {
        this.number = number;
        this.subscriber = subscriber;
        this.pbx = pbx;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public Pbx getPbx() {
        return pbx;
    }

    public void setPbx(Pbx pbx) {
        this.pbx = pbx;
    }
}
