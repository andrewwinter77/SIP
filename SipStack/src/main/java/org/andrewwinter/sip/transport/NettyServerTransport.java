package org.andrewwinter.sip.transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.Executors;
import org.andrewwinter.sip.SipRequestHandler;
import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.FixedReceiveBufferSizePredictorFactory;
import org.jboss.netty.channel.socket.DatagramChannelFactory;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 *
 * @author andrew
 */
public class NettyServerTransport extends ServerTransport {

    private final SocketAddress localPort;
    
    public NettyServerTransport(final SipRequestHandler sipListener, final int tcpPort) {
        super(sipListener);
        this.localPort = new InetSocketAddress(tcpPort);
        
    }
    
    @Override
    public void stopListening() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void listen() {
        listenForTcp();
        listenForUdp();
    }
    
    private class SipServerPipelineFactory implements ChannelPipelineFactory {
        @Override
        public ChannelPipeline getPipeline() throws Exception {
            return Channels.pipeline(new NettySimpleChannelHandler(NettyServerTransport.this));
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
 
        ConnectionlessBootstrap b = new ConnectionlessBootstrap(f);
 
        // Configure the pipeline factory.
        b.setPipelineFactory(new SipServerPipelineFactory());
 
        // Enable broadcast
        b.setOption("broadcast", "false");
 
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
        b.setOption(
                "receiveBufferSizePredictorFactory",
                new FixedReceiveBufferSizePredictorFactory(65535));
 
        // Bind to the port and start the service.
        b.bind(localPort);
    }
}
