package org.andrewwinter.sip.sdp;

import java.util.ArrayList;
import java.util.List;
import org.andrewwinter.sip.sdp.util.Util;

/**
 *
 * The only protocol version understood is 0. v=0 is set automatically, hence there
 * is no setter method for this property.
 * 
 * @author andrewwinter77
 */
public class SessionDescriptionBuilder {
    
    private int version;

    private Origin origin;
    
    private String name;
    
    private ConnectionData connectionData;
    
    private List<Timing> timings;
    
    private List<MediaDescription> mediaDescriptions;
    
    private String info;
    
    private String uri;
    
    private List<String> emails;
    
    private List<String> phoneNumbers;
    
    private EncryptionKey encryptionKey;
    
    private List<Bandwidth> bandwidths;

    private List<Attribute> attributes;
    
    private TimeZones timeZones;
    
    /**
     * Set some legal defaults.
     */
    public SessionDescriptionBuilder() {
        version = 0;
        
        // Section 5.3:
        // If a session has no meaningful name, the
        // value "s= " SHOULD be used (i.e., a single space as the session
        // name).
        name = " ";
        
        timings = new ArrayList<Timing>();
        mediaDescriptions = new ArrayList<MediaDescription>();
        emails = new ArrayList<String>();
        phoneNumbers = new ArrayList<String>();
        bandwidths = new ArrayList<Bandwidth>();
        attributes = new ArrayList<Attribute>();
    }
    
    /**
     *
     * @return
     */
    public SessionDescription build() {
        
        if (origin == null) {
            throw new IllegalStateException("Origin must be set.");
        }
        
        return new SessionDescription(
                version,
                origin,
                name,
                info,
                uri,
                emails,
                phoneNumbers,
                connectionData,
                bandwidths,
                encryptionKey,
                timings,
                timeZones,
                attributes,
                mediaDescriptions);
    }
    
    /**
     *
     * @param version
     * @return
     */
    public SessionDescriptionBuilder version(final int version) {
        this.version = version;
        return this;
    }

    /**
     *
     * @param origin
     * @return
     */
    public SessionDescriptionBuilder origin(final Origin origin) {
        this.origin = origin;
        return this;
    }
    
    /**
     *
     * @param name
     * @return
     */
    public SessionDescriptionBuilder name(String name) {
        nullCheck(name, "name");
        if (name.isEmpty()) {
            // s= MUST NOT be empty. Be tolerant here
            // and set it the recommended " ".
            name = " ";
        }
        this.name = name;
        return this;
    }
    
    /**
     *
     * @param connectionData
     * @return
     */
    public SessionDescriptionBuilder connectionData(final ConnectionData connectionData) {
        assertNotYetSet(this.connectionData, "c=");
        this.connectionData = connectionData;
        return this;
    }
    
    private static void nullCheck(final Object obj, final String desc) {
        if (obj == null) {
            throw new IllegalArgumentException(desc + " must not be null.");
        }
    }

    /**
     *
     * @param timing
     * @return
     */
    public SessionDescriptionBuilder timing(final Timing timing) {
        nullCheck(timing, "timing");
        timings.add(timing);
        return this;
    }

    private static void assertNotYetSet(final Object obj, final String field) {
        if (obj != null) {
            throw new IllegalArgumentException("No more than one " + field + 
                    " field allowed per session description.");
        }
    }
    
    /**
     * i=
     * @param info
     * @return 
     */
    public SessionDescriptionBuilder info(final String info) {
        assertNotYetSet(this.info, "i=");
        this.info = info;
        return this;
    }
    
    /**
     * u=
     * @param uri
     * @return 
     */
    public SessionDescriptionBuilder uri(final String uri) {
        assertNotYetSet(this.uri, "u=");
        this.uri = uri;
        return this;
    }
    
    /**
     * e=
     * @param email
     * @return 
     */
    public SessionDescriptionBuilder email(final String email) {
        emails.add(email);
        return this;
    }
  
    /**
     * p=
     * @param phoneNumber
     * @return 
     */
    public SessionDescriptionBuilder phone(final String phoneNumber) {
        phoneNumbers.add(phoneNumber);
        return this;
    }
    
    /**
     * k=
     * @param key 
     * @return 
     */
    public SessionDescriptionBuilder encryptionKey(final EncryptionKey key) {
        assertNotYetSet(this.encryptionKey, "k=");
        this.encryptionKey = key;
        return this;
    }
    
    /**
     *
     * @param md
     * @return
     */
    public SessionDescriptionBuilder mediaDescription(final MediaDescription md) {
        mediaDescriptions.add(md);
        return this;
    }

    /**
     *
     * @param b
     * @return
     */
    public SessionDescriptionBuilder bandwidth(final Bandwidth b) {
        bandwidths.add(b);
        return this;
    }
    
    /**
     *
     * @param a
     * @return
     */
    public SessionDescriptionBuilder attribute(final Attribute a) {
        attributes.add(a);
        return this;
    }

    /**
     *
     * @param tz
     * @return
     */
    public SessionDescriptionBuilder timezones(final TimeZones tz) {
        assertNotYetSet(this.timeZones, "z=");
        this.timeZones = tz;
        return this;
    }

    /**
     *
     * @param sdp
     * @return
     */
    public static SessionDescription parse(String sdp) {
        
        sdp = Util.replaceCRLFwithLF(sdp);
        final String[] lines = sdp.split(Util.NEWLINE);

        SessionDescriptionBuilder builder = new SessionDescriptionBuilder();
        
        TimingBuilder timingBuilder = null;
        MediaDescriptionBuilder mediaDescriptionBuilder = null;
        
        // length-1 because the last newline terminates, it doesn't delimit.
        for (int i=0; i<lines.length-1; ++i) {
            final String line = lines[i];
            final char typeLetter = line.charAt(0);
            final String value = line.substring(2);
            
            if (typeLetter != 'r' && timingBuilder != null) {
                builder = builder.timing(timingBuilder.build());
                timingBuilder = null;
            }
            
            switch (typeLetter) {
                case 'v':
                    builder = builder.version(Integer.parseInt(value));
                    break;
                case 'o':
                    final Origin origin = OriginBuilder.parse(value);
                    builder = builder.origin(origin);
                    break;
                case 's':
                    builder = builder.name(value);
                    break;
                case 'i':
                    if (mediaDescriptionBuilder == null) {
                        builder = builder.info(value);
                    } else {
                        mediaDescriptionBuilder = mediaDescriptionBuilder.info(value);
                    }
                    break;
                case 'u':
                    if (mediaDescriptionBuilder == null) {
                        builder = builder.uri(value);
                    } else {
                        throw new IllegalArgumentException("u= MUST appear before first media field.");
                    }
                    break;
                case 'e':
                    if (mediaDescriptionBuilder == null) {
                        builder = builder.email(value);
                    } else {
                        throw new IllegalArgumentException("e= MUST appear before first media field.");
                    }
                    break;
                case 'p':
                    if (mediaDescriptionBuilder == null) {
                        builder = builder.phone(value);
                    } else {
                        throw new IllegalArgumentException("p= MUST appear before first media field.");
                    }
                    break;
                case 'c':
                    final ConnectionData cd = ConnectionDataBuilder.parse(value);
                    if (mediaDescriptionBuilder == null) {
                        builder = builder.connectionData(cd);
                    } else {
                        mediaDescriptionBuilder = mediaDescriptionBuilder.connectionData(cd);
                    }
                    break;
                case 'b':
                    final Bandwidth b = BandwidthBuilder.parse(value);
                    // b will be null if we don't understand the bandwidth type
                    if (b != null) {
                        if (mediaDescriptionBuilder == null) {
                            builder = builder.bandwidth(b);
                        } else {
                            mediaDescriptionBuilder = mediaDescriptionBuilder.bandwidth(b);
                        }
                    }
                    break;
                case 'z':
                    final TimeZones tz = TimeZonesBuilder.parse(value);
                    builder = builder.timezones(tz);
                    break;
                case 'k':
                    final EncryptionKey k = EncryptionKeyBuilder.parse(value);
                    if (mediaDescriptionBuilder == null) {
                        builder = builder.encryptionKey(k);
                    } else {
                        mediaDescriptionBuilder = mediaDescriptionBuilder.encryptionKey(k);
                    }
                    break;
                case 'a':
                    final Attribute attribute = AttributeBuilder.parse(value);
                    if (attribute == null) {
                        // If an attribute is received that is not understood,
                        // it MUST be ignored by the receiver.
                    } else {
                        if (mediaDescriptionBuilder == null) {
                            builder = builder.attribute(attribute);
                        } else {
                            mediaDescriptionBuilder = mediaDescriptionBuilder.attribute(attribute);
                        }
                    }
                    break;
                case 't':
                    timingBuilder = TimingBuilder.parse(value);
                    break;
                case 'r':
                    if (timingBuilder == null) {
                        throw new IllegalArgumentException("r= with no immediately-preceding t=");
                    }
                    final RepeatTimes repeatTimes = RepeatTimesBuilder.parse(value);
                    timingBuilder.repeatTimes(repeatTimes);
                    break;
                case 'm':
                    if (mediaDescriptionBuilder != null) {
                        builder = builder.mediaDescription(mediaDescriptionBuilder.build());
                    }
                    mediaDescriptionBuilder = new MediaDescriptionBuilder();
                    final Media media = MediaBuilder.parse(value);
                    mediaDescriptionBuilder = mediaDescriptionBuilder.media(media);
                    break;
                default:
                    /* Parser MUST ignore any type letter
                     * that it does not understand.
                     */
            }
        }
        
        if (timingBuilder != null) {
            builder = builder.timing(timingBuilder.build());
        }
        
        if (mediaDescriptionBuilder != null) {
            builder = builder.mediaDescription(mediaDescriptionBuilder.build());
        }

        return builder.build();
    }
}
