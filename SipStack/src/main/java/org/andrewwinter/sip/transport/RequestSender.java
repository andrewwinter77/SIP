package org.andrewwinter.sip.transport;

import org.andrewwinter.sip.element.Destination;
import org.andrewwinter.sip.parser.SipMessageHelper;
import org.andrewwinter.sip.parser.SipRequest;

/**
 *
 * @author andrew
 */
public class RequestSender {

    private final Destination dest;
    
    /**
     *
     * @param dest 
     */
    public RequestSender(final Destination dest) {
        this.dest = dest;
    }
    
    /**
     *
     * @return
     */
    public Destination getDestination() {
        return dest;
    }
    
    /**
     *
     * @param request 
     */
    public void send(final SipRequest request) {

        // In the case of stream-oriented transports such as TCP, the
        // Content-Length header field indicates the size of the body. The
        // Content-Length header field MUST be used with stream oriented
        // transports.
        if (SipMessageHelper.getContentLength(request) == null) {
            SipMessageHelper.setContentLength(0, request);
        }

        System.out.println("\n---OUT--------------------------------");
        System.out.println(request);
        
        if ("UDP".equals(dest.getTransport())) {
                
            Udp.send(request, dest.getAddress(), dest.getPort());
        } else {
                // TODO: Handle non-UDP transports
        }
    }
}
