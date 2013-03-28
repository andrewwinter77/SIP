package org.andrewwinter.jsr289.jboss.metadata;

import org.jboss.as.server.deployment.DeploymentUnitProcessingException;

/**
 *
 * @author andrew
 */
public class SipApplicationInfo {

    /**
     * Mandatory.
     */
    private String appName;
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

    public void setAppName(final String appName) throws DeploymentUnitProcessingException {
        this.appName = Util.normalize(appName);
        if (this.appName == null) {
            throw new DeploymentUnitProcessingException("Invalid application application name.");
        }
    }

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(final String packageName) {
        this.packageName = Util.normalize(packageName);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = Util.normalize(description);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = Util.normalize(displayName);
    }

    public boolean isDistributable() {
        return distributable;
    }

    public void setDistributable(final String distributable) {
        if (distributable != null) {
            this.distributable = Boolean.valueOf(distributable);
        }
    }

    public String getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(final String largeIcon) {
        this.largeIcon = Util.normalize(largeIcon);
    }

    public String getMainServlet() {
        return mainServlet;
    }

    public void setMainServlet(final String mainServlet) {
        this.mainServlet = Util.normalize(mainServlet);
    }

    public int getProxyTimeout() {
        return proxyTimeout;
    }

    public void setProxyTimeout(final String proxyTimeout) throws DeploymentUnitProcessingException {
        if (proxyTimeout != null) {
            try {
                this.proxyTimeout = Integer.valueOf(proxyTimeout);
            } catch (final NumberFormatException e) {
                throw new DeploymentUnitProcessingException("proxyTimeout not a valid integer.");
            }
        }
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(final String sessionTimeout) throws DeploymentUnitProcessingException {
        if (sessionTimeout != null) {
            try {
                this.sessionTimeout = Integer.valueOf(sessionTimeout);
            } catch (final NumberFormatException e) {
                throw new DeploymentUnitProcessingException("sessionTimeout not a valid integer.");
            }
        }
    }

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(final String smallIcon) {
        this.smallIcon = Util.normalize(smallIcon);
    }

    @Override
    public String toString() {
        return "SipApplicationMetadata{" + "packageName=" + packageName + ", description=" + description + ", displayName=" + displayName + ", distributable=" + distributable + ", largeIcon=" + largeIcon + ", mainServlet=" + mainServlet + ", proxyTimeout=" + proxyTimeout + ", sessionTimeout=" + sessionTimeout + ", smallIcon=" + smallIcon + '}';
    }
}
