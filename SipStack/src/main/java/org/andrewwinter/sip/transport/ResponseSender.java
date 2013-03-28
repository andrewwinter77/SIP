package org.andrewwinter.sip.transport;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.andrewwinter.sip.parser.SipMessageHelper;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.parser.Via;

/**
 *
 * @author andrew
 */
public class ResponseSender {

    private final TcpSocketWrapper socket;
    
    /**
     *
     * @param socket {@code null} if the request was received over UDP.
     */
    public ResponseSender(final TcpSocketWrapper socket) {
        this.socket = socket;
    }
    
    /**
     *
     */
    public void closeSocket() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error closing socket.");
            }
        }
    }
    
    /**
     *
     * @param response
     */
    public void send(final SipResponse response) {
        // In the case of stream-oriented transports such as TCP, the
        // Content-Length header field indicates the size of the body. The
        // Content-Length header field MUST be used with stream oriented
        // transports.
        if (SipMessageHelper.getContentLength(response) == null) {
            SipMessageHelper.setContentLength(0, response);
        }

        System.out.println("\n---OUT--------------------------------");
        System.out.println(response);
        
        byte[] bytes = null;
        try {
            bytes = response.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            // TODO: Handle exception
        }
        
        if (socket != null && socket.isConnected()) {
            
            try {
                socket.write(ByteBuffer.wrap(bytes));
            } catch (IOException e) {
                e.printStackTrace();
                // TODO: Handle exception
            }    
        } else {

            final Via via = response.getTopmostVia();
            
            if ("TCP".equals(via.getTransport()) || "SCTP".equals(via.getTransport())) {
                
                // If the "sent-protocol" is a reliable transport protocol such
                // as TCP or SCTP, or TLS over those, the response MUST be sent
                // using the existing connection to the source of the original
                // request that created the transaction, if that connection is
                // still open.
                
                // If that connection is no longer open, the server SHOULD open
                // a connection to the IP address in the received parameter,
                // if present, using the port in the sent-by value, or the
                // default port for that transport, if no port is specified. If
                // that connection attempt fails, the server SHOULD use the
                // procedures in [4] for servers in order to determine the IP
                // address and port to open the connection and send the response
                // to.
                
                // TODO: Handle this
            } else if ("UDP".equals(via.getTransport())) {
                
                final String maddr = via.getParameter("maddr");
                if (maddr == null) {

  // From RFC 3581:
  //
  // If the "sent-protocol"
  // component indicates an unreliable unicast transport protocol, such as
  // UDP, and there is no "maddr" parameter, but there is both a
  // "received" parameter and an "rport" parameter, the response MUST be
  // sent to the IP address listed in the "received" parameter, and the
  // port in the "rport" parameter.
           
                    String dest;

                    Integer port;
                    final String received = via.getParameter("received");
                    String rport = via.getParameter("rport");

                    if (received == null) {
                        dest = via.getHost();
                        port = via.getPort();
                        if (port == null) {
                            port = 5060;
                        }
                    } else {
                        dest = received;
                        
                        if (rport == null) {

                            port = via.getPort();
                            if (port == null) {
                               port = 5060;
                            }
                            
                        } else {
                            port = Integer.valueOf(rport);
                        }
                        
                    }

                    // TODO: From RFC 3581:

                    // The response MUST be sent from the same address and port that
                    // the corresponding request was received on.

                    Udp.send(response, dest, port);
                    
                    
                } else {
                    
                    // Otherwise, if the Via header field value contains a maddr
                    // parameter, the response MUST be forwarded to the address
                    // listed there, using the port indicated in sent-by, or
                    // port 5060 if none is present.
                    
                    Integer port = via.getPort();
                    if (port == null) {
                        port = 5060;
                    }
                    
                    // If the address is a
                    // multicast address, the response SHOULD be sent using the
                    // TTL indicated in the ttl parameter, or with a TTL of 1 if
                    // that parameter is not present.
                    
                    // TODO: TTL
                    
                    Udp.send(response, maddr, port);
                }
                
            } else {
                // TODO: Handle other transports
            }
        }
    }
}
