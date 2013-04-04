package org.andrewwinter.sip.sdp;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrewwinter77
 */
public class MediaBuilder {

    private MediaType type;
    
    private int port;
    
    private int numPorts;
    
    private TransportProtocol protocol;
    
    private List<String> formatDesc;
    
    /**
     * Set some legal defaults.
     */
    public MediaBuilder() {
        formatDesc = new ArrayList<String>();
        numPorts = 1;
    }

    private static void nullCheck(final Object obj, final String desc) {
        if (obj == null) {
            throw new IllegalArgumentException(desc + " must not be null.");
        }
    }

    /**
     *
     * @param type
     * @return
     */
    public MediaBuilder type(final MediaType type) {
        nullCheck(type, "type");
        this.type = type;
        return this;
    }

    /**
     *
     * @param port
     * @return
     */
    public MediaBuilder port(final int port) {
        this.port = port;
        return this;
    }
    
    /**
     *
     * @param numPorts
     * @return
     */
    public MediaBuilder numPorts(final int numPorts) {
        this.numPorts = numPorts;
        return this;
    }

    /**
     *
     * @param protocol
     * @return
     */
    public MediaBuilder protocol(final TransportProtocol protocol) {
        nullCheck(protocol, "protocol");
        this.protocol = protocol;
        return this;
    }
    
    
    /**
     *
     * @param fd
     * @return
     */
    public MediaBuilder formatDesc(final String fd) {
        nullCheck(fd, "fd");
        this.formatDesc.add(fd);
        return this;
    }
    
    /**
     *
     * @return
     */
    public Media build() {
        nullCheck(type, "type");
        nullCheck(protocol, "protocol");
        
        if (port == 0) {
            throw new IllegalArgumentException("A non-zero port number must be specified, even for black hole SDP.");
        }
        
        if (protocol == TransportProtocol.UDP) {
            if (formatDesc.isEmpty()) {
                throw new IllegalArgumentException("Format description MUST be present for UDP protocol.");
            }
        } if (protocol == TransportProtocol.RTPAVP) {
            if (formatDesc.isEmpty()) {
                
                // This is completely defined in the RTP Audio/Video profile as
                // payload type 0, so there is no need for an "a=rtpmap:"
                // attribute
                
                formatDesc.add("0");
            }
        }
        
        return new Media(type, port, numPorts, protocol, formatDesc);
    }
    
    /**
     *
     * @param str
     * @return
     */
    public static Media parse(final String str) {
        final String[] parts = str.split(" ");
        
        MediaBuilder builder = new MediaBuilder();
        builder = builder.type(MediaType.fromString(parts[0]));
        
        final String[] portParts = parts[1].split("/");
        builder = builder.port(Integer.parseInt(portParts[0]));
        if (portParts.length > 1) {
            builder =  builder.numPorts(Integer.parseInt(portParts[2]));
        }
        
        builder = builder.protocol(TransportProtocol.fromString(parts[2]));
        for (int i=3; i<parts.length; ++i) {
            builder = builder.formatDesc(parts[i]);
        }
        
        return builder.build();
    }
}
