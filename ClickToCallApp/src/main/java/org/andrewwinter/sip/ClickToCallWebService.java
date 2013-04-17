package org.andrewwinter.sip;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.sip.Address;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletRequest;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
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
 */
@ApplicationPath("/click2call/")
@Path("/")
public class ClickToCallWebService extends Application {

    @Resource
    private SipFactory sf;

    /**
     * Replace this method with something of your own.
     * @param param
     */
    @POST
    @Path("/call")
    public String call(
            @FormParam("from") final String from,
            @FormParam("to") final String to,
            @Context ServletContext servletContext,
            @Context HttpServletRequest request) {
        
        final SipApplicationSession appSession = sf.createApplicationSession();

        final SipServletRequest inviteToAlice;
        try {
            final Address aliceAddress = sf.createAddress(to);
            final Address bobAddress = sf.createAddress(from);
        
            inviteToAlice = sf.createRequest(
                                    appSession,
                                    "INVITE",
                                    bobAddress,
                                    aliceAddress);
            
            inviteToAlice.setContent(makeOfferSdp().toString().getBytes(), "application/sdp");
            
            inviteToAlice.getSession().setAttribute(Util.ALICE_ADDRESS_KEY, aliceAddress);
            inviteToAlice.getSession().setAttribute(Util.BOB_ADDRESS_KEY, bobAddress);
            inviteToAlice.getSession().setAttribute(Util.IS_ALICE_KEY, "");
      
        } catch (ServletParseException e) {
            return "There was something wrong with the addresses.";
        } catch (UnsupportedEncodingException e) {
            return "There was something wrong with the content type.";
        }
        
        try {
            inviteToAlice.send();
        } catch (IOException e) {
            return "There was an error placing the call.";
        }
         
        return "Call in progress.";
    }

    private SessionDescription makeOfferSdp() {
        
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
