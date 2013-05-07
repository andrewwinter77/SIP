package org.andrewwinter.sip.transport;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 *
 * @author andrew
 */
class NettySimpleChannelHandler extends SimpleChannelHandler {

    private final ServerTransport serverTransport;
    
    NettySimpleChannelHandler(final ServerTransport serverTransport) {
        this.serverTransport = serverTransport;
    }
    
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent event) {
        try {
            ChannelBuffer buf = (ChannelBuffer) event.getMessage();
            final String sipMessageAsString = buf.toString(Charset.forName("UTF-8"));
            if (((InetSocketAddress) event.getRemoteAddress()).getPort() > 0) {
                serverTransport.handleIncomingMessage(
                        sipMessageAsString,
                        (InetSocketAddress) event.getRemoteAddress(),
                        null);
            } else {
                // TODO: What's going on here?
                System.out.println("Dropping packets received on port 0.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();

        Channel ch = e.getChannel();
        ch.close();
    }
}
