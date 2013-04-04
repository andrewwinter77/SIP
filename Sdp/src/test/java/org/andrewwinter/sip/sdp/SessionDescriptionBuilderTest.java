package org.andrewwinter.sip.sdp;

import org.junit.Test;

/**
 *
 * @author andrew
 */
public class SessionDescriptionBuilderTest {

    @Test
    public void testBuilder() {
        
        OriginBuilder originBuilder = new OriginBuilder();
        originBuilder.unicastAddr("192.168.1.1");
        final Origin origin = originBuilder.build();
        
        MediaBuilder mediaBuilder = new MediaBuilder();
        mediaBuilder = mediaBuilder.type(MediaType.AUDIO);
        mediaBuilder = mediaBuilder.protocol(TransportProtocol.RTPAVP);
        final Media media = mediaBuilder.build();
        
        MediaDescriptionBuilder mediaDescriptionBuilder = new MediaDescriptionBuilder();
        mediaDescriptionBuilder.media(media);
        final MediaDescription mediaDescription = mediaDescriptionBuilder.build();
        
        SessionDescriptionBuilder builder = new SessionDescriptionBuilder();
        builder = builder.origin(origin);
        builder = builder.mediaDescription(mediaDescription);
        final SessionDescription sdp = builder.build();
        
        System.out.println(sdp.toString());
    }
}
