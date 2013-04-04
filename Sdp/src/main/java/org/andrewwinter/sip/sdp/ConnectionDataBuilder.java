package org.andrewwinter.sip.sdp;

/**
 *
 * @author andrewwinter77
 */
public class ConnectionDataBuilder {

    private NetworkType netType;
    
    private AddressType addrType;
    
    private String address;

    /**
     * Set some legal defaults.
     */
    public ConnectionDataBuilder() {
        netType = NetworkType.IN;
    }
    
    private static void nullCheck(final Object obj, final String desc) {
        if (obj == null) {
            throw new IllegalArgumentException(desc + " must not be null.");
        }
    }
    
    /**
     *
     * @param netType
     * @return
     */
    public ConnectionDataBuilder netType(final NetworkType netType) {
        nullCheck(netType, "netType");
        this.netType = netType;
        return this;
    }
    
    /**
     *
     * @param addrType
     * @return
     */
    public ConnectionDataBuilder addrType(final AddressType addrType) {
        nullCheck(addrType, "addrType");
        this.addrType = addrType;
        return this;
    }
    
    /**
     *
     * @param address
     * @return
     */
    public ConnectionDataBuilder address(final String address) {
        nullCheck(address, "address");
        this.address = address;
        return this;
    }

    /**
     *
     * @return
     */
    public ConnectionData build() {
        nullCheck(addrType, "addrType");
        nullCheck(address, "address");
        return new ConnectionData(netType, addrType, address);
    }
    
    /**
     *
     * @param originLine
     * @return
     */
    public static ConnectionData parse(final String originLine) {
        final String[] parts = originLine.split(" ");
        
        ConnectionDataBuilder builder = new ConnectionDataBuilder();
        
        try {
            builder = builder.netType(NetworkType.valueOf(parts[0]));
            builder = builder.addrType(AddressType.valueOf(parts[1]));
        } catch (NumberFormatException e) {
            // TODO: Handle this
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }

        builder = builder.address(parts[2]);
        return builder.build();
    }
}
