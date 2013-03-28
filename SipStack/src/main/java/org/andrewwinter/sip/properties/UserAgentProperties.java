package org.andrewwinter.sip.properties;

import org.andrewwinter.sip.parser.Address;
import org.andrewwinter.sip.parser.SipUri;

/**
 *
 * @author andrewwinter77
 */
public class UserAgentProperties {
    
    private final Address from;
    
    private final String username;
    
    /**
     * 
     * @param from {@code null} indicates that the identity of the client is to
     * remain anonymous.
     * @param username 
     */
    public UserAgentProperties(final Address from, final String username) {
        
        if (from == null)
        {
            
            // A UAC SHOULD use the display name "Anonymous", along with a
            // syntactically correct, but otherwise meaningless URI
            // (like sip:thisis@anonymous.invalid), if the identity of the
            // client is to remain hidden.            
            
            final SipUri uri = new SipUri("anonymous.invalid");
            uri.setUser("thisis");
            this.from = new Address(uri);
            this.from.setDisplayName("Anonymous");
        } else {
            this.from = from;
        }
        
        this.username = username;
    }
    
    /**
     *
     * @return
     */
    public Address getFrom() {
        return from;
    }
    
    /**
     *
     * @return
     */
    public Address getContact() {
        final SipUri uri = new SipUri(ServerProperties.getInstance().getDomain());
        uri.setScheme("sip");
        uri.setUser(username);
        uri.setPort(ServerProperties.getInstance().getUnsecurePort());
        return new Address(uri);
    }
}
