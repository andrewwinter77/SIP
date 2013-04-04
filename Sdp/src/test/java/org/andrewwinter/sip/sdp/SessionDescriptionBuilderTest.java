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
        originBuilder.unicastAddr("192.168.1.1"); // TODO: Set this to be something else, maybe the outbound NIC address
        final Origin origin = originBuilder.build();
        
        SessionDescriptionBuilder builder = new SessionDescriptionBuilder();
        builder = builder.origin(origin);
        final SessionDescription sdp = builder.build();
        
        System.out.println(sdp.toString());
    }
}
