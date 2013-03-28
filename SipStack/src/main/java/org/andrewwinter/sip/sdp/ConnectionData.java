package org.andrewwinter.sip.sdp;

/**
 *
 * @author andrewwinter77
 */
public class ConnectionData {

    private final NetworkType netType;
    
    private final AddressType addrType;
    
    private final String address;
    
    /**
     * 
     * @param netType
     * @param addrType
     * @param address 
     */
    ConnectionData(
            final NetworkType netType,
            final AddressType addrType,
            final String address) {
        
        this.netType = netType;
        this.addrType = addrType;
        this.address = address;
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
    public String getAddress() {
        return address;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(netType).append(" ");
        sb.append(addrType).append(" ");
        sb.append(address);
        return sb.toString();
    }
}
