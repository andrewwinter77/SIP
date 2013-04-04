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
        mediaBuilder = mediaBuilder.port(1234);
        final Media media = mediaBuilder.build();
        
        AttributeBuilder attributeBuilder = new AttributeBuilder();
        attributeBuilder = attributeBuilder.name(AttributeName.RTPMAP);
        attributeBuilder = attributeBuilder.value("0 PCMU/8000");
        final Attribute attribute = attributeBuilder.build();

        MediaDescriptionBuilder mediaDescriptionBuilder = new MediaDescriptionBuilder();
        mediaDescriptionBuilder.media(media);
        mediaDescriptionBuilder.attribute(attribute);
        final MediaDescription mediaDescription = mediaDescriptionBuilder.build();

        ConnectionDataBuilder connectionDataBuilder = new ConnectionDataBuilder();
        connectionDataBuilder.addrType(AddressType.IP4);
        connectionDataBuilder.address("192.16.1.1");
        final ConnectionData connetionData = connectionDataBuilder.build();
        
        SessionDescriptionBuilder builder = new SessionDescriptionBuilder();
        builder = builder.origin(origin);
        builder = builder.mediaDescription(mediaDescription);
        builder = builder.connectionData(connetionData);
        
        builder = builder.name("-");
        final SessionDescription sdp = builder.build();
    }
}
