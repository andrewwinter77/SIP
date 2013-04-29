package org.andrewwinter.jsr289.model;

/**
 *
 * @author andrew
 */
public class SipApplicationInfo {

    /**
     * Mandatory.
     */
    private String appName;
    /**
     *
     */
    private String packageName;
    /**
     *
     */
    private String description;
    /**
     *
     */
    private String displayName;
    /**
     *
     */
    private boolean distributable;
    /**
     *
     */
    private String largeIcon;
    /**
     *
     */
    private String mainServlet;
    /**
     *
     */
    private int proxyTimeout;
    /**
     *
     */
    private int sessionTimeout;
    /**
     *
     */
    private String smallIcon;

    public SipApplicationInfo() {
        this.proxyTimeout = 180;
        this.sessionTimeout = 3;
    }

    /**
     * 
     * @param appName
     * @throws IllegalArgumentException 
     */
    public void setAppName(final String appName) throws IllegalArgumentException {
        if (appName == null || appName.isEmpty()) {
            throw new IllegalArgumentException("Invalid application name.");
        }
        this.appName = appName;
    }

    /**
     * 
     * @return 
     */
    public String getAppName() {
        return appName;
    }

    /**
     * 
     * @return 
     */
    public String getPackageName() {
        return packageName;
    }

    private static String normalize(final String str) {
        if (str == null || str.isEmpty()) {
            return null;
        } else {
            return str;
        }
    }
    
    /**
     * 
     * @param packageName 
     */
    public void setPackageName(final String packageName) {
        this.packageName = normalize(packageName);
    }

    /**
     * 
     * @return 
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description 
     */
    public void setDescription(final String description) {
        this.description = normalize(description);
    }

    /**
     * 
     * @return 
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 
     * @param displayName 
     */
    public void setDisplayName(final String displayName) {
        this.displayName = normalize(displayName);
    }

    /**
     * 
     * @return 
     */
    public boolean isDistributable() {
        return distributable;
    }

    /**
     * 
     * @param distributable 
     */
    public void setDistributable(final String distributable) {
        if (distributable != null) {
            this.distributable = Boolean.valueOf(distributable);
        }
    }

    /**
     * 
     * @return 
     */
    public String getLargeIcon() {
        return largeIcon;
    }

    /**
     * 
     * @param largeIcon 
     */
    public void setLargeIcon(final String largeIcon) {
        this.largeIcon = normalize(largeIcon);
    }

    /**
     * 
     * @return 
     */
    public String getMainServlet() {
        return mainServlet;
    }

    /**
     * 
     * @param mainServlet 
     */
    public void setMainServlet(final String mainServlet) {
        this.mainServlet = normalize(mainServlet);
    }

    /**
     * 
     * @return 
     */
    public int getProxyTimeout() {
        return proxyTimeout;
    }

    /**
     * 
     * @param proxyTimeout
     * @throws IllegalArgumentException 
     */
    public void setProxyTimeout(final String proxyTimeout) throws IllegalArgumentException {
        if (proxyTimeout != null) {
            try {
                this.proxyTimeout = Integer.valueOf(proxyTimeout);
            } catch (final NumberFormatException e) {
                throw new IllegalArgumentException("proxyTimeout is not a valid integer.");
            }
        }
    }

    /**
     * 
     * @return 
     */
    public int getSessionTimeout() {
        return sessionTimeout;
    }

    /**
     * 
     * @param sessionTimeout
     * @throws IllegalArgumentException 
     */
    public void setSessionTimeout(final String sessionTimeout) throws IllegalArgumentException {
        if (sessionTimeout != null) {
            try {
                this.sessionTimeout = Integer.valueOf(sessionTimeout);
            } catch (final NumberFormatException e) {
                throw new IllegalArgumentException("sessionTimeout is not a valid integer.");
            }
        }
    }

    /**
     * 
     * @return 
     */
    public String getSmallIcon() {
        return smallIcon;
    }

    /**
     * 
     * @param smallIcon 
     */
    public void setSmallIcon(final String smallIcon) {
        this.smallIcon = normalize(smallIcon);
    }

    /**
     * 
     * @return 
     */
    @Override
    public String toString() {
        return "SipApplicationMetadata{" + "packageName=" + packageName + ", description=" + description + ", displayName=" + displayName + ", distributable=" + distributable + ", largeIcon=" + largeIcon + ", mainServlet=" + mainServlet + ", proxyTimeout=" + proxyTimeout + ", sessionTimeout=" + sessionTimeout + ", smallIcon=" + smallIcon + '}';
    }
}
