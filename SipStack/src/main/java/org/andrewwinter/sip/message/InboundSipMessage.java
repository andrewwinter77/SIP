package org.andrewwinter.sip.message;

import java.net.InetSocketAddress;
import org.andrewwinter.sip.parser.SipMessage;

/**
 *
 * @author andrew
 */
public abstract class InboundSipMessage {
    
    private final String initialRemoteAddrHost;
    
    private final int initialRemotePort;
    
    /**
     * The inbound SIP message.
     */
    private final SipMessage message;
    
    /**
     *
     * @param initialRemoteAddr
     * @param message
     */
    protected InboundSipMessage(final InetSocketAddress initialRemoteAddr, final SipMessage message) {
        this.initialRemoteAddrHost = initialRemoteAddr.getHostString();
        this.initialRemotePort = initialRemoteAddr.getPort();
        this.message = message;
    }
    
    /**
     *
     * @return
     */
    public String getInitialRemoteAddr() {
        return initialRemoteAddrHost;
    }
    
    /**
     *
     * @return
     */
    public int getInitialRemotePort() {
        return initialRemotePort;
    }
    
    /**
     *
     * @return
     */
    public SipMessage getMessage() {
        return message;
    }
}
