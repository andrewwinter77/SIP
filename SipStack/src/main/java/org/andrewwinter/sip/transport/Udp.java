package org.andrewwinter.sip.transport;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.andrewwinter.sip.parser.SipMessage;
import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.DatagramChannel;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.util.CharsetUtil;

/**
 *
 * @author andrewwinter77
 */
class Udp {

    private static ChannelFactory factory;
    private static ConnectionlessBootstrap bootstrap;

    static void send(final SipMessage message, final String address, final int port) {

        if (factory == null) {
            factory = new NioDatagramChannelFactory(Executors.newCachedThreadPool());

            bootstrap = new ConnectionlessBootstrap(factory);

            bootstrap.setPipelineFactory(
                    new ChannelPipelineFactory() {
                        @Override
                        public ChannelPipeline getPipeline() throws Exception {
                            return Channels.pipeline(
                                    new StringEncoder(CharsetUtil.UTF_8),
                                    new StringDecoder(CharsetUtil.UTF_8),
                                    new SimpleChannelUpstreamHandler());
                        }
                    });

            bootstrap.setOption("reuseAddress", true);

            bootstrap.setOption("tcpNoDelay", true);
            bootstrap.setOption("broadcast", "false");
            bootstrap.setOption("sendBufferSize", 65536);
            bootstrap.setOption("receiveBufferSize", 65536);

            //           channel = (DatagramChannel) bootstrap.bind(new InetSocketAddress(0));
        }

        final DatagramChannel channel = (DatagramChannel) bootstrap.bind(new InetSocketAddress(0));

        ChannelFuture future = channel.write(
                message.toString(), new InetSocketAddress(address, port));
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture cf) throws Exception {
//                              cf.getChannel().close();
//                              factory.releaseExternalResources();
            }
        });
    }
//    void send(final SipMessage message, final String address, final int port) {
//        
//        System.out.println("Sending message via UDP to " + address + ":" + port);
//        
//        try {
//            final byte[] serializedMessage = message.toString().getBytes();
//        
//            InetAddress inet = InetAddress.getByName(address);
//            DatagramPacket packet = new DatagramPacket(
//                    serializedMessage, serializedMessage.length, inet, port);
//
//            DatagramSocket socket = new DatagramSocket();
//            socket.send(packet);
//        } catch (SocketException e) {
//            System.out.println("Something went wrong: " + e.getMessage());
//            e.printStackTrace();
//        }
//        catch (IOException e) {
//            System.out.println("Something went wrong: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
}
