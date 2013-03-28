package org.andrewwinter.sip.element;

/**
 *
 * @author andrewwinter77
 */
public class Destination {

    private final String address;
    
    private final int port;
    
    private final String transport;
    
    /**
     *
     * @param address
     * @param port
     * @param transport
     */
    public Destination(final String address, final int port, final String transport) {
        this.address = address;
        this.port = port;
        this.transport = transport;
    }

    /**
     *
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @return
     */
    public int getPort() {
        return port;
    }

    /**
     *
     * @return
     */
    public String getTransport() {
        return transport;
    }
}
