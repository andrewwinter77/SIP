package org.andrewwinter.jsr289.jboss.deployment.phase;

import javax.servlet.sip.Address;
import javax.servlet.sip.AuthInfo;
import javax.servlet.sip.Parameterable;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.URI;
import org.jboss.as.naming.ManagedReference;
import org.jboss.as.server.deployment.DeploymentUnit;

/**
 *
 * @author andrew
 */
public class SipFactoryManagedReference implements ManagedReference {

    private final DeploymentUnit du;
    
    public SipFactoryManagedReference(final DeploymentUnit du) {
        this.du = du;
    }
    
    @Override
    public void release() {
    }

    @Override
    public Object getInstance() {
        System.out.println("+++++++++++++++++++++++++++++++++ returning SipFactory instance");
        return new SipFactory() {

            @Override
            public URI createURI(String string) throws ServletParseException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public SipURI createSipURI(String string, String string1) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Address createAddress(String string) throws ServletParseException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Address createAddress(URI uri) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Address createAddress(URI uri, String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Parameterable createParameterable(String string) throws ServletParseException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public SipServletRequest createRequest(SipApplicationSession sas, String string, Address adrs, Address adrs1) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public SipServletRequest createRequest(SipApplicationSession sas, String string, URI uri, URI uri1) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public SipServletRequest createRequest(SipApplicationSession sas, String string, String string1, String string2) throws ServletParseException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public SipServletRequest createRequest(SipServletRequest ssr, boolean bln) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public SipApplicationSession createApplicationSession() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public SipApplicationSession createApplicationSessionByKey(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public AuthInfo createAuthInfo() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            
        };
        
        
//        return (SipFactory) du.getAttachment(CustomAttachments.SIP_FACTORY);
    }
}
