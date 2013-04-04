package org.andrewwinter.sip.sdp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author andrewwinter77
 */
public class Media {

    private final MediaType type;
    
    private final int port;
    
    private final int numPorts;
    
    private final TransportProtocol protocol;
    
    private final List<String> formatDesc;
    
    Media(
            final MediaType type,
            final int port,
            final int numPorts,
            final TransportProtocol protocol,
            final List<String> formatDesc) {
        this.type = type;
        this.port = port;
        this.numPorts = numPorts;
        this.protocol = protocol;
        this.formatDesc = new ArrayList<String>(formatDesc);
    }
            
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(type).append(" ");
        sb.append(port);
        if (numPorts > 1) {
            sb.append("/").append(numPorts);
        }
        sb.append(" ");
        sb.append(protocol);
        for (final String fd : formatDesc) {
            sb.append(" ").append(fd);
        }
        return sb.toString();
    }

    /**
     *
     * @return
     */
    public MediaType getType() {
        return type;
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
    public int getNumPorts() {
        return numPorts;
    }

    /**
     *
     * @return
     */
    public TransportProtocol getProtocol() {
        return protocol;
    }

    /**
     *
     * @return
     */
    public List<String> getFormatDesc() {
        return Collections.unmodifiableList(formatDesc);
    }
}
