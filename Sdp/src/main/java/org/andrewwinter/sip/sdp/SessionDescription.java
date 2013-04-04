package org.andrewwinter.sip.sdp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.andrewwinter.sip.sdp.util.Util;

/**
 *
 * @author andrewwinter77
 */
public class SessionDescription {

    private final int version;
    
    private final Origin origin;
    
    private final String name;
    
    private final String info;
    
    private final String uri;
    
    private final List<String> emails;
    
    private final List<String> phones;
    
    private final ConnectionData connectionData;

    private final List<Bandwidth> bandwidths;
    
    private final EncryptionKey encryptionKey;
    
    private final List<Timing> timings;
    
    private final TimeZones timeZones;
    
    private final List<Attribute> attributes;
    
    private List<MediaDescription> mediaDescriptions;
    
    SessionDescription(
            final int version,
            final Origin origin,
            final String name,
            final String info,
            final String uri,
            final List<String> emails,
            final List<String> phones,
            final ConnectionData connectionData,
            final List<Bandwidth> bandwidths,
            final EncryptionKey encryptionKey,
            final List<Timing> timings,
            final TimeZones timeZones,
            final List<Attribute> attributes,
            final List<MediaDescription> mediaDescriptions) {
        this.version = version;
        this.origin = origin;
        this.info = info;
        this.uri = uri;
        this.name = name;
        this.emails = new ArrayList<String>(emails);
        this.phones = new ArrayList<String>(phones);
        this.connectionData = connectionData;
        this.bandwidths = new ArrayList<Bandwidth>(bandwidths);
        this.encryptionKey = encryptionKey;
        this.timings = new ArrayList<Timing>(timings);
        this.timeZones = timeZones;
        this.attributes = new ArrayList<Attribute>(attributes);
        this.mediaDescriptions = new ArrayList<MediaDescription>(mediaDescriptions);
    }
    
    /**
     *
     * @return
     */
    public int getVersion() {
        return version;
    }

    /**
     *
     * @return
     */
    public Origin getOrigin() {
        return origin;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public List<Timing> getTimings() {
        return Collections.unmodifiableList(timings);
    }
    
    /**
     *
     * @return
     */
    public List<MediaDescription> getMediaDescriptions() {
        return Collections.unmodifiableList(mediaDescriptions);
    }
    
    /**
     *
     * @return
     */
    public String getInformation() {
        return info;
    }

    /**
     *
     * @return
     */
    public String getUri() {
        return uri;
    }

    /**
     *
     * @return
     */
    public ConnectionData getConnectionData() {
        return connectionData;
    }

    /**
     *
     * @return
     */
    public EncryptionKey getEncryptionKey() {
        return encryptionKey;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("v=").append(version).append(Util.CRLF);
        sb.append("o=").append(origin).append(Util.CRLF);
        sb.append("s=").append(name).append(Util.CRLF);
        if (info != null) {
            sb.append("i=").append(info).append(Util.CRLF);
        }
        
        if (uri != null) {
            sb.append("u=").append(uri).append(Util.CRLF);
        }
        for (final String email : emails) {
            sb.append("e=").append(email).append(Util.CRLF);
        }
        for (final String phone : phones) {
            sb.append("p=").append(phone).append(Util.CRLF);
        }
        if (connectionData != null) {
            sb.append("c=").append(connectionData).append(Util.CRLF);
        }
        for (final Bandwidth bandwidth : bandwidths) {
            sb.append("b=").append(bandwidth).append(Util.CRLF);
        }
        for (final Timing timing : timings) {
            sb.append("t=").append(timing).append(Util.CRLF);
        }
        if (timeZones != null) {
            sb.append("z=");
            final List<TimeZone> tzs = timeZones.getTimeZones();
            String delimiter = "";
            for (final TimeZone tz : tzs) {
                sb.append(delimiter);
                sb.append(tz.getTime());
                sb.append(" ");
                sb.append(tz.getOffset());
                delimiter = " ";
            }
            sb.append(Util.CRLF);
        }
        if (encryptionKey != null) {
            sb.append("k=").append(encryptionKey).append(Util.CRLF);
        }
        for (final Attribute attribute : attributes) {
            sb.append("a=").append(attribute).append(Util.CRLF);
        }
        for (final MediaDescription mediaDescription : mediaDescriptions) {
            sb.append("m=").append(mediaDescription).append(Util.CRLF);
        }
        return sb.toString();
    }

    /**
     *
     * @return
     */
    public String getInfo() {
        return info;
    }

    /**
     *
     * @return
     */
    public List<String> getEmailAddresses() {
        return Collections.unmodifiableList(emails);
    }

    /**
     *
     * @return
     */
    public List<String> getPhoneNumbers() {
        return Collections.unmodifiableList(phones);
    }

    /**
     *
     * @return
     */
    public TimeZones getTimeZones() {
        return timeZones;
    }

    /**
     *
     * @return
     */
    public List<Attribute> getAttributes() {
        return Collections.unmodifiableList(attributes);
    }

    /**
     *
     * @return
     */
    public List<Bandwidth> getBandwidths() {
        return Collections.unmodifiableList(bandwidths);
    }
}
