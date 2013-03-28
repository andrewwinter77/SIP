package org.andrewwinter.sip.properties;

import org.andrewwinter.sip.util.Util;

/**
 *
 * @author andrewwinter77
 */
public class ServerProperties {
    
    private static final String SOFTWARE_STRING = "Elaina";
    
    private String domain;

    private boolean statelessProxy;
    
    private boolean suppress100Trying;
    
    private String outboundProxy;
    
    private int unsecurePort;
    
    private int securePort;
    
    private boolean useCompactForm;
    
    private boolean includeUserAgentHeader;
    
    private boolean includeServerHeader;
    
    private boolean useRport;
    
    /**
     * Maximum transmission unit.
     */
    private int mtu;
    
    private static ServerProperties INSTANCE = new ServerProperties();
    
    /**
     *
     * @return
     */
    public static ServerProperties getInstance() {
        return INSTANCE;
    }
    
    private ServerProperties() {
        domain = Util.getIpAddress();
        mtu = 1500;
        statelessProxy = false;
        unsecurePort = 5060;
        securePort = 5061;
        suppress100Trying = false;
        useCompactForm = false;
        includeUserAgentHeader = true;
        includeServerHeader = true;
        useRport = false;
    }
    
    /**
     *
     * @param unsecurePort
     */
    public void setUnsecurePort(final int unsecurePort) {
        this.unsecurePort = unsecurePort;
    }
    
    /**
     *
     * @param securePort
     */
    public void setSecurePort(final int securePort) {
        this.securePort = securePort;
    }
    
    /**
     * 
     * @return 
     */
    public boolean useRport() {
        return useRport;
    }

    /**
     * 
     * @param useRport 
     */
    public void setUseRport(boolean useRport) {
        this.useRport = useRport;
    }
    
    /**
     * 
     * @param includeUserAgentHeader 
     */
    public void setIncludeUserAgentHeader(final boolean includeUserAgentHeader) {
        this.includeUserAgentHeader = includeUserAgentHeader;
    }
    
    /**
     * 
     * @param includeServerHeader 
     */
    public void setIncludeServerHeader(final boolean includeServerHeader) {
        this.includeServerHeader = includeServerHeader;
    }
    
    /**
     * 
     * @param useCompactForm 
     */
    public void setUseCompactForm(final boolean useCompactForm) {
        this.useCompactForm = useCompactForm;
    }
    
    /**
     * 
     * @return 
     */
    public String getSoftwareString() {
        return SOFTWARE_STRING;
    }
    
    /**
     * 
     * @return 
     */
    public int getUnsecurePort() {
        return unsecurePort;
    }
    
    /**
     * 
     * @return 
     */
    public int getSecurePort() {
        return securePort;
    }
    
    /**
     * 
     * @param outboundProxy 
     */
    public void setOutboundProxy(final String outboundProxy) {
        this.outboundProxy = outboundProxy;
    }
    
    /**
     * 
     * @return 
     */
    public String getOutboundProxy() {
        return outboundProxy;
    }
    
    
    /**
     *
     * @param domain
     */
    public void setDomain(final String domain) {
        this.domain = domain;
    }
    
    /**
     * 
     * @return 
     */
    public int getMTU() {
        return mtu;
    }
    
    /**
     * 
     * @param mtu 
     */
    public void setMTU(final int mtu) {
        this.mtu = mtu;
    }
    
    /**
     * 
     * @param suppress100Trying 
     */
    public void setSuppress100Trying(final boolean suppress100Trying) {
        this.suppress100Trying = suppress100Trying;
    }
    
    /**
     * 
     * @return 
     */
    public boolean getSuppress100Trying() {
        return suppress100Trying;
    }
    
    /**
     * 
     * @return 
     */
    public String getDomain() {
        return domain;
    }
    
    /**
     * 
     * @return 
     */
    public boolean isStatelessProxy() {
        return statelessProxy;
    }
    
    /**
     * 
     * @return 
     */
    public boolean includeServerHeader() {
        return includeServerHeader;
    }
    
    /**
     * 
     * @return 
     */
    public boolean includeUserAgentHeader() {
        return includeUserAgentHeader;
    }
    
    /**
     * 
     * @return 
     */
    public boolean useCompactForm() {
        return useCompactForm;
    }
}
