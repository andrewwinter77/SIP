package org.andrewwinter.sip.parser;

/**
 *
 * @author andrew
 */
public class Via extends Parameterable {
    
    /**
     *
     */
    public static final String MAGIC_COOKIE = "z9hG4bK";
    
    private String transport;
    
    private String host;
    
    private Integer port;
    
    private void setHost(final String host) {
        this.host = host;
    }
    
    /**
     *
     * @return
     */
    public String getHost() {
        return host;
    }
    
    /**
     *
     * @return
     */
    public Integer getPort() {
        return port;
    }
    
    /**
     *
     * @return
     */
    public String getSentBy() {
        if (port == null) {
            return host;
        } else {
            return host + ":" + port;
        }
    }
    
    /**
     * Null can be used to 'unset' the port.
     * @param port 
     */
    public void setPort(final Integer port) {
        this.port = port;
    }
    
    /**
     *
     * @return
     */
    public String getTransport() {
        return transport;
    }
    
    /**
     *
     * @param transport
     */
    public void setTransport(final String transport) {
        this.transport = transport;
    }
    
    /**
     *
     * @return
     */
    public String getBranch() {
        return getParameter("branch");
    }
    
    /**
     *
     * @param branch
     */
    public void setBranch(final String branch) {
        setParameter("branch", branch);
    }

    /**
     * Transport defaults to UDP.
     * @param host Host without a port.
     */
    public Via(final String host) {
        this.host = host;
        this.transport = "UDP";
    }
    
    /** 
     * Default constructor. Note that the Via created by this
     * is invalid until the transport and host are set.
     */
    private Via() {
    }

    /**
     * 
     * @param str
     * @return 
     */
    public static Via parse(final String str) {
        final Via via = new Via();
        
        String[] parts = str.split("/", 3);
        
        // Ignore the SIP version, for now.
        parts[2] = parts[2].trim();
        
        parts = parts[2].split("[;\\s]+", 2);
        
        via.setTransport(parts[0]);
        String restOfString = parts[1].trim();
        
        final String hostPort;
        
        int indexOfSemicolon = restOfString.indexOf(';');
        if (indexOfSemicolon == -1) {
            hostPort = restOfString;
        } else {
            hostPort = restOfString.substring(0, indexOfSemicolon);
            restOfString = restOfString.substring(indexOfSemicolon);
            via.parseParams(restOfString);
        }
        
        parts = hostPort.split(":", 2);
        via.setHost(parts[0].trim());
        if (parts.length == 2) {
            via.setPort(Integer.parseInt(parts[1]));
        }
        
        return via;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return getValue() + toParamString();
    }

    /**
     *
     * @return
     */
    @Override
    public String getValue() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SIP/2.0/").append(transport);
        sb.append(" ").append(host);
        if (port != null) {
            sb.append(":").append(port);
        }
        return sb.toString();
    }

    @Override
    public void setValue(final String value) throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Via clone() {
        return Via.parse(toString());
    }
}
