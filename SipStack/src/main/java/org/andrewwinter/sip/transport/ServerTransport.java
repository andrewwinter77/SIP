package org.andrewwinter.sip.transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executors;
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
import org.andrewwinter.sip.parser.Util;
import org.andrewwinter.sip.parser.Via;
import org.andrewwinter.sip.properties.ServerProperties;
import org.andrewwinter.sip.transaction.client.ClientTransaction;
import org.andrewwinter.sip.transaction.client.ClientTransactionStore;
import org.andrewwinter.sip.transaction.server.ServerTransaction;
import org.andrewwinter.sip.transaction.server.ServerTransactionStore;
import org.andrewwinter.sip.transaction.server.ack.AckServerTransaction;
import org.andrewwinter.sip.transaction.server.invite.InviteServerTransaction;
import org.andrewwinter.sip.transaction.server.noninvite.NonInviteServerTransaction;
import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.FixedReceiveBufferSizePredictorFactory;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.DatagramChannelFactory;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.util.CharsetUtil;

/**
 *
 * @author andrewwinter77
 */
public class ServerTransport {

    private SipRequestHandler sipListener;
    
    private SocketAddress localPort;
    
    private ConnectionlessBootstrap udpBootstrap;
        
    private Channel udpChannel;

    /**
     *
     * @param request
     * @param remoteAddress 
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

            final InboundSipRequest isr = new InboundSipRequest(request, remoteAddress, new ResponseSender(tcpSocket, this));

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

                final InboundSipRequest isr = new InboundSipRequest(request, remoteAddress, new ResponseSender(tcpSocket, this));

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

        final String sentBy = message.getTopmostVia().getHost();
        final String srcIp = remoteAddress.getAddress().getHostAddress();

        final String rport = message.getTopmostVia().getParameter("rport");
        if (!srcIp.equals(sentBy) || (rport != null && rport.isEmpty())) {

            final Via via = message.popVia();

            via.setParameter("received", srcIp);

            if (rport != null && rport.isEmpty()) {
                via.setParameter("rport", String.valueOf(((InetSocketAddress) remoteAddress).getPort()));
            }

            SipMessageHelper.pushVia(via, message);
        }
    }

    /**
     * See http://tools.ietf.org/html/rfc5626
     * @param remoteAddress
     * @param tcpSocketWrapper 
     */
    void handlePing(final InetSocketAddress remoteAddress, final TcpSocketWrapper tcpSocketWrapper) {
        if (tcpSocketWrapper == null) {
            sendOverUdp(Util.CRLF, remoteAddress);
        } else {
            throw new UnsupportedOperationException("TCP not supported here");
        }
    }
    
    void handleIncomingMessage(
            final String messageAsString,
            final InetSocketAddress remoteAddress,
            final TcpSocketWrapper tcpSocketWrapper) {

        if (ServerProperties.getInstance().isStatelessProxy()) {
            // TODO: Handle stateless proxy case
        } else {

            System.out.println("\n---IN-from " + remoteAddress + "--------------------------------");
            System.out.println(messageAsString);
            System.out.println("--------------------------------------");

            SipMessage message;
            try {
                message = SipMessage.parse(messageAsString);
            } catch (ParseException e) {

                // Message was invalid. Don't take further action.

                System.out.println("Failed to parse: " + messageAsString);
                e.printStackTrace();
                return; 
            }
            
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

    private static ServerTransport INSTANCE = new ServerTransport();
    
    public static ServerTransport getInstance() {
        return INSTANCE;
    }
    
    private ServerTransport() {
    }
    
    /**
     *
     * @param sipListener
     */
    public void init(final SipRequestHandler sipListener, final int tcpPort) {
        this.sipListener = sipListener;
        this.localPort = new InetSocketAddress(tcpPort);
    }
    
    /**
     *
     * @throws IOException
     */
    public void stopListening() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     *
     */
    public void listen() {
        listenForTcp();
        listenForUdp();
    }

    private class SipServerPipelineFactory implements ChannelPipelineFactory {
        @Override
        public ChannelPipeline getPipeline() throws Exception {
            return Channels.pipeline(
                    new NettySimpleChannelHandler(ServerTransport.this),
                    new StringEncoder(CharsetUtil.UTF_8),
                    new StringDecoder(CharsetUtil.UTF_8),
                    new SimpleChannelUpstreamHandler());
        }
    }

    private void listenForTcp() {
        ChannelFactory factory =
                new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool());

        ServerBootstrap bootstrap = new ServerBootstrap(factory);

        bootstrap.setPipelineFactory(new SipServerPipelineFactory());

        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);

        bootstrap.bind(localPort);

        System.out.println("TCP Server Started!");
    }
    
    private void listenForUdp() {
        DatagramChannelFactory f =
            new NioDatagramChannelFactory(Executors.newCachedThreadPool());
 
        udpBootstrap = new ConnectionlessBootstrap(f);
 
        // Configure the pipeline factory.
        udpBootstrap.setPipelineFactory(new SipServerPipelineFactory());
 
        // Enable broadcast
        udpBootstrap.setOption("broadcast", "false");
 
        // Allow packets as large as up to 65535 bytes.
        // You could increase or decrease this value to avoid truncated packets
        // or to improve memory footprint respectively.
        //
        // Please also note that a large UDP packet might be truncated or
        // dropped by your router no matter how you configured this option.
        // In UDP, a packet is truncated or dropped if it is larger than a
        // certain size, depending on router configuration.  IPv4 routers
        // truncate and IPv6 routers drop a large packet.  That's why it is
        // safe to send small packets in UDP.
        udpBootstrap.setOption(
                "receiveBufferSizePredictorFactory",
                new FixedReceiveBufferSizePredictorFactory(65535));

        udpBootstrap.setOption("localAddress", localPort);
        udpBootstrap.setOption("tcpNoDelay", true);
        
        // Bind to the port and start the service.
        udpChannel = udpBootstrap.bind(localPort);
    }
    
    
    /**
     * 
     * @param message
     * @param address
     * @param port 
     */
    public void sendOverUdp(final String message, final InetSocketAddress address) {

        System.out.println("\n---OUT--" + address.getHostString() + ":" + address.getPort() + "/UDP------------------------------");
        System.out.println(message.toString());
  
        ChannelFuture future = udpChannel.write(
                message.toString(), address);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture cf) throws Exception {
//                              cf.getChannel().close();
//                              factory.releaseExternalResources();
            }
        });
    }
    
    /**
     * 
     * @param message
     * @param address
     * @param port 
     */
    public void sendOverUdp(final SipMessage message, final String address, final int port) {
        sendOverUdp(message.toString(), new InetSocketAddress(address, port));
    }
}
