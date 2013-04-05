package org.andrewwinter.sip.transport;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import org.andrewwinter.sip.SipRequestHandler;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.dialog.DialogId;
import org.andrewwinter.sip.dialog.DialogStore;
import org.andrewwinter.sip.message.InboundSipRequest;
import org.andrewwinter.sip.message.InboundSipResponse;
import org.andrewwinter.sip.parser.ParseException;
import org.andrewwinter.sip.parser.SipMessage;
import org.andrewwinter.sip.parser.SipMessageHelper;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.parser.Via;
import org.andrewwinter.sip.properties.ServerProperties;
import org.andrewwinter.sip.transaction.client.ClientTransaction;
import org.andrewwinter.sip.transaction.client.ClientTransactionStore;
import org.andrewwinter.sip.transaction.server.ServerTransaction;
import org.andrewwinter.sip.transaction.server.ServerTransactionStore;
import org.andrewwinter.sip.transaction.server.ack.AckServerTransaction;
import org.andrewwinter.sip.transaction.server.invite.InviteServerTransaction;
import org.andrewwinter.sip.transaction.server.noninvite.NonInviteServerTransaction;

/**
 *
 * @author andrewwinter77
 */
public abstract class ServerTransport {

    private final SipRequestHandler sipListener;
    
    /**
     *
     * @param request
     * @param tcpSocket {@code null} if request was received over UDP.
     */
    private void handleIncomingRequest(
            final SipRequest request,
            final InetSocketAddress remoteAddress,
            final TcpSocketWrapper tcpSocket) {

        Dialog dialog = null;
        if (SipMessageHelper.getTo(request).getTag() != null) {
            final DialogId dialogId = DialogId.createForUAS(request);
            dialog = DialogStore.getInstance().get(dialogId);
        }

        request.setMatchesExistingDialog(dialog != null);
        
        // When a request is received from the network by the server, it has to
        // be matched to an existing transaction.

        ServerTransaction txn = ServerTransactionStore.getInstance().get(request);
        if (txn == null) {
            
            request.setMatchesExistingServerTransaction(false);

            final InboundSipRequest isr = new InboundSipRequest(request, remoteAddress, new ResponseSender(tcpSocket));

            // TODO: Create a new thread for this.

            if (request.isINVITE()) {
                InviteServerTransaction.create(dialog, isr, sipListener);
            } else if (request.isACK()) {
                AckServerTransaction.create(dialog, isr, sipListener);
            } else {
                NonInviteServerTransaction.create(dialog, isr, null, sipListener);
            }
        } else {

            request.setMatchesExistingServerTransaction(true);

            if (request.isCANCEL()) {

                final InboundSipRequest isr = new InboundSipRequest(request, remoteAddress, new ResponseSender(tcpSocket));

                NonInviteServerTransaction.create(dialog, isr, txn, sipListener);

                if (!txn.hasStatefulProxy()) {
                    txn.cancel();
                }

            } else {

                // Most likely either a retransmission or an ACK.
                txn.handleRequestFromTransportLayer(request);
            }
        }
    }

    /**
     *
     * @param response
     */
    private static void handleIncomingResponse(final SipResponse response, final InetSocketAddress remoteAddress) {
        final ClientTransaction txn = ClientTransactionStore.getInstance().get(response);
        if (txn == null) {
            // TODO: Forward response to the core, p104-105
        } else {

            // If there is a match, the response MUST be passed to that
            // transaction.
            
            final InboundSipResponse isr = new InboundSipResponse(response, remoteAddress, txn);
            txn.handleResponseFromTransportLayer(isr);
        }
    }

    private static void updateVia(final SipMessage message, final InetSocketAddress remoteAddress) {

        // When the server transport receives a request over any transport, it
        // MUST examine the value of the sent-by parameter in the top Via header
        // field value. If the host portion of the sent-by parameter contains a
        // domain name, or if it contains an IP address that differs from the
        // packet source address, the server MUST add a received parameter to
        // that Via header field value. This parameter MUST contain the source
        // address from which the packet was received.

        try {
            final InetAddress viaAddress = InetAddress.getByName(message.getTopmostVia().getHost());
            final InetAddress addr = remoteAddress.getAddress();

            String rport = message.getTopmostVia().getParameter("rport");
            if (!addr.equals(viaAddress) || (rport != null && rport.isEmpty())) {

                final Via via = message.popVia();

                via.setParameter("received", addr.getHostAddress());

                if (rport != null && rport.isEmpty()) {
                    via.setParameter("rport", String.valueOf(((InetSocketAddress) remoteAddress).getPort()));
                }

                SipMessageHelper.pushVia(via, message);
            }
        } catch (UnknownHostException e) {
            // TODO: Handle exception
        }
    }

    void handleIncomingMessage(
            final String messageAsString,
            final InetSocketAddress remoteAddress,
            final TcpSocketWrapper tcpSocketWrapper) {

        SipMessage message;
        try {
            message = SipMessage.parse(messageAsString);
        } catch (ParseException e) {
            // TODO: Log parse exception
            // Message was invalid. Don't take further action.
            return; 
        }

        if (ServerProperties.getInstance().isStatelessProxy()) {
            // TODO: Handle stateless proxy case
        } else {


            System.out.println("\n---IN-from " + remoteAddress + "--------------------------------");
            System.out.println(message);
            System.out.println("--------------------------------------");

            // TODO: Where do we check the validity of the message?

            // TODO: Handle case where message doesn't contain a via 
            // - this would be an invalid message. There is code that will blow up
            // with an NPE if the message doesn't contain a via.

            if (message instanceof SipRequest) {
                updateVia(message, remoteAddress);
                handleIncomingRequest((SipRequest) message, remoteAddress, tcpSocketWrapper);
            } else {
                handleIncomingResponse((SipResponse) message, remoteAddress);
            }
        }
    }

    public ServerTransport(final SipRequestHandler sipListener) {
        this.sipListener = sipListener;
    }
    
    public abstract void stopListening() throws IOException;
    
    public abstract void listen();
}
