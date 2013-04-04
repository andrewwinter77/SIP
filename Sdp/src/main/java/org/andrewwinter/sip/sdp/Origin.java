package org.andrewwinter.sip.sdp;

/**
 *
 * @author andrewwinter77
 */
public class Origin {

    private final String username;
    
    private final long id;
    
    private final long version;
    
    private final NetworkType netType;
    
    private final AddressType addrType;
    
    private final String unicastAddr;
    
    /**
     * 
     * @param username
     * @param id
     * @param version
     * @param netType
     * @param addrType
     * @param unicastAddr 
     */
    Origin(
            final String username,
            final long id,
            final long version,
            final NetworkType netType,
            final AddressType addrType,
            final String unicastAddr) {
        
        this.username = username;
        this.id = id;
        this.version = version;
        this.netType = netType;
        this.addrType = addrType;
        this.unicastAddr = unicastAddr;
    }
            
    
    /**
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public long getVersion() {
        return version;
    }

    /**
     *
     * @return
     */
    public NetworkType getNetType() {
        return netType;
    }

    /**
     *
     * @return
     */
    public AddressType getAddrType() {
        return addrType;
    }

    /**
     *
     * @return
     */
    public String getUnicastAddr() {
        return unicastAddr;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(username).append(" ");
        sb.append(id).append(" ");
        sb.append(version).append(" ");
        sb.append(netType).append(" ");
        sb.append(addrType).append(" ");
        sb.append(unicastAddr);
        return sb.toString();
    }
}
