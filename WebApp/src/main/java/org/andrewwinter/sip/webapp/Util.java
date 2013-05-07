package org.andrewwinter.sip.webapp;

import org.andrewwinter.sip.sdp.AddressType;
import org.andrewwinter.sip.sdp.ConnectionData;
import org.andrewwinter.sip.sdp.ConnectionDataBuilder;
import org.andrewwinter.sip.sdp.Media;
import org.andrewwinter.sip.sdp.MediaBuilder;
import org.andrewwinter.sip.sdp.MediaDescription;
import org.andrewwinter.sip.sdp.MediaDescriptionBuilder;
import org.andrewwinter.sip.sdp.MediaType;
import org.andrewwinter.sip.sdp.Origin;
import org.andrewwinter.sip.sdp.OriginBuilder;
import org.andrewwinter.sip.sdp.SessionDescription;
import org.andrewwinter.sip.sdp.SessionDescriptionBuilder;
import org.andrewwinter.sip.sdp.TransportProtocol;

/**
 *
 * @author andrew
 */
class Util {

    static final String SUBSCRIBER_ADDRESS_KEY = "partyAAttribute";
    static final String CALLEE_ADDRESS_KEY = "partyBAttribute";

    static final String IS_SUBSCRIBER_KEY = "isPartyASession";
    
    static final String OK_KEY = "200ok";
    
    private Util() {
    }

    static SessionDescription makeOfferSdp() {
        
        final String ip = "192.168.1.65";
        
        OriginBuilder originBuilder = new OriginBuilder();
        originBuilder = originBuilder.unicastAddr(ip); // TODO: Set this to be something else, maybe the outbound NIC address
        final Origin origin = originBuilder.build();
        
        MediaBuilder mediaBuilder = new MediaBuilder();
        mediaBuilder = mediaBuilder.type(MediaType.AUDIO);
        mediaBuilder = mediaBuilder.protocol(TransportProtocol.RTPAVP);
        mediaBuilder = mediaBuilder.port(1);
        final Media media = mediaBuilder.build();
        
        MediaDescriptionBuilder mediaDescriptionBuilder = new MediaDescriptionBuilder();
        mediaDescriptionBuilder = mediaDescriptionBuilder.media(media);
        final MediaDescription mediaDescription = mediaDescriptionBuilder.build();
        
        ConnectionDataBuilder connectionDataBuilder = new ConnectionDataBuilder();
        connectionDataBuilder.addrType(AddressType.IP4);
        connectionDataBuilder.address("0.0.0.0");
        final ConnectionData connetionData = connectionDataBuilder.build();
        
        SessionDescriptionBuilder builder = new SessionDescriptionBuilder();
        builder = builder.origin(origin);
        builder = builder.mediaDescription(mediaDescription);
        builder = builder.connectionData(connetionData);
        
        return builder.build();
    }
}
