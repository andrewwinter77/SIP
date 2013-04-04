package org.andrewwinter.sip.sdp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.andrewwinter.sip.sdp.util.Util;

/**
 *
      Media description, if present
         m=  (media name and transport address)
         i=* (media title)
         c=* (connection information -- optional if included at
              session level)
         b=* (zero or more bandwidth information lines)
         k=* (encryption key)
         a=* (zero or more media attribute lines)
 * @author andrewwinter77
 */
public class MediaDescription {

    private final List<ConnectionData> connectionData;

    private final String info;
    
    private final EncryptionKey encryptionKey;
    
    private final Media media;
    
    private final List<Attribute> attributes;
    
    private final List<Bandwidth> bandwidths;

    MediaDescription(
            final String info,
            final EncryptionKey encryptionKey,
            final Media media,
            final List<ConnectionData> connectionData,
            final List<Attribute> attributes,
            final List<Bandwidth> bandwidths) {
        
        this.info = info;
        this.encryptionKey = encryptionKey;
        this.media = media;
        this.connectionData = new ArrayList<ConnectionData>(connectionData);
        this.attributes = new ArrayList<Attribute>(attributes);
        this.bandwidths = new ArrayList<Bandwidth>(bandwidths);
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(media);
        if (info != null) {
            sb.append(Util.CRLF).append("i=").append(info);
        }
        for (final ConnectionData cd : connectionData) {
            sb.append(Util.CRLF).append("c=").append(cd);
        }
        for (final Bandwidth b : bandwidths) {
            sb.append(Util.CRLF).append("b=").append(b);
        }
        if (encryptionKey != null) {
            sb.append(Util.CRLF).append("k=").append(encryptionKey);
        }
        for (final Attribute a : attributes) {
            sb.append(Util.CRLF).append("a=").append(a);
        }
        return sb.toString();
    }

    /**
     *
     * @return
     */
    public Media getMedia() {
        return media;
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
    public EncryptionKey getEncryptionKey() {
        return encryptionKey;
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
    public List<ConnectionData> getConnectionData() {
        return Collections.unmodifiableList(connectionData);
    }

    /**
     *
     * @return
     */
    public List<Bandwidth> getBandwidths() {
        return Collections.unmodifiableList(bandwidths);
    }

}
