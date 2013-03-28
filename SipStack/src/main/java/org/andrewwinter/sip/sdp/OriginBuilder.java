package org.andrewwinter.sip.sdp;

/**
 *
 * @author andrewwinter77
 */
public class OriginBuilder {

    private String username;
    
    private long id;
    
    private long version;
    
    private NetworkType netType;
    
    private AddressType addrType;
    
    private String unicastAddr;

    /**
     * Set some legal defaults.
     */
    public OriginBuilder() {
        username = "-";
        netType = NetworkType.IN;
        addrType = AddressType.IP4;
    }
    
    private static void nullCheck(final Object obj, final String desc) {
        if (obj == null) {
            throw new IllegalArgumentException(desc + " must not be null.");
        }
    }
    
    /**
     *
     * @param username
     * @return
     */
    public OriginBuilder username(final String username) {
        nullCheck(username, "username");
        if (username.indexOf(" ") >= 0) {
            throw new IllegalArgumentException("username must not contain spaces.");
        }
        this.username = username;
        return this;
    }
    
    /**
     *
     * @param id
     * @return
     */
    public OriginBuilder id(final long id) {
        this.id = id;
        return this;
    }
    
    /**
     *
     * @param version
     * @return
     */
    public OriginBuilder version(final long version) {
        this.version = version;
        return this;
    }
    
    /**
     *
     * @param netType
     * @return
     */
    public OriginBuilder netType(final NetworkType netType) {
        nullCheck(netType, "netType");
        this.netType = netType;
        return this;
    }
    
    /**
     *
     * @param addrType
     * @return
     */
    public OriginBuilder addrType(final AddressType addrType) {
        nullCheck(addrType, "addrType");
        this.addrType = addrType;
        return this;
    }
    
    /**
     *
     * @param unicastAddr
     * @return
     */
    public OriginBuilder unicastAddr(final String unicastAddr) {
        nullCheck(unicastAddr, "unicastAddr");
        this.unicastAddr = unicastAddr;
        return this;
    }

    /**
     *
     * @return
     */
    public Origin build() {
        nullCheck(addrType, "addrType");
        nullCheck(unicastAddr, "unicastAddr");
        return new Origin(username, id, version, netType, addrType, unicastAddr);
    }
    
    /**
     *
     * @param originLine
     * @return
     */
    public static Origin parse(final String originLine) {
        final String[] parts = originLine.split(" ");
        
        OriginBuilder builder = new OriginBuilder();
        builder = builder.username(parts[0]);
        
        try {
            builder = builder.id(Long.parseLong(parts[1]));
            builder = builder.version(Long.parseLong(parts[2]));
            builder = builder.netType(NetworkType.valueOf(parts[3]));
            builder = builder.addrType(AddressType.valueOf(parts[4]));
        } catch (NumberFormatException e) {
            // TODO: Handle this
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }

        builder = builder.unicastAddr(parts[5]);
        return builder.build();
    }
}
