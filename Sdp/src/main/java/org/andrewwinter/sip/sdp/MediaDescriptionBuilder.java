package org.andrewwinter.sip.sdp;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrewwinter77
 */
public class MediaDescriptionBuilder {

    private List<ConnectionData> connectionData;

    private String info;
    
    private EncryptionKey encryptionKey;
    
    private Media media;
    
    private List<Attribute> attributes;
    
    private List<Bandwidth> bandwidths;

    /**
     * Set some legal defaults.
     */
    public MediaDescriptionBuilder() {
        connectionData = new ArrayList<ConnectionData>();
        attributes = new ArrayList<Attribute>();
        bandwidths = new ArrayList<Bandwidth>();
    }
    
    private static void nullCheck(final Object obj, final String desc) {
        if (obj == null) {
            throw new IllegalArgumentException(desc + " must not be null.");
        }
    }
    
    private static void assertNotYetSet(final Object obj, final String field) {
        if (obj != null) {
            throw new IllegalArgumentException("No more than one " + field + 
                    " field allowed per media description.");
        }
    }

    /**
     * i=
     * @param info
     * @return 
     */
    public MediaDescriptionBuilder info(final String info) {
        assertNotYetSet(this.info, "i=");
        this.info = info;
        return this;
    }

    /**
     *
     * @param connectionData
     * @return
     */
    public MediaDescriptionBuilder connectionData(final ConnectionData connectionData) {
        nullCheck(connectionData, "connectionData");
        this.connectionData.add(connectionData);
        return this;
    }

    /**
     * k=
     * @param key 
     * @return 
     */
    public MediaDescriptionBuilder encryptionKey(final EncryptionKey key) {
        assertNotYetSet(this.encryptionKey, "k=");
        this.encryptionKey = key;
        return this;
    }

    /**
     * k=
     * @param media 
     * @return 
     */
    public MediaDescriptionBuilder media(final Media media) {
        if (this.media != null) {
            throw new IllegalStateException("Media cannot be called twice"
                    + " for the same media description.");
        }
        this.media = media;
        return this;
    }

    /**
     *
     * @param b
     * @return
     */
    public MediaDescriptionBuilder bandwidth(final Bandwidth b) {
        bandwidths.add(b);
        return this;
    }

    /**
     *
     * @param a
     * @return
     */
    public MediaDescriptionBuilder attribute(final Attribute a) {
        attributes.add(a);
        return this;
    }

    /**
     *
     * @return
     */
    public MediaDescription build() {
        nullCheck(media, "media");
        return new MediaDescription(
                info,
                encryptionKey,
                media,
                connectionData,
                attributes,
                bandwidths);
    }
}
