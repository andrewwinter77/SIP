package org.andrewwinter.sip.transport;

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
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        ChannelBuffer buf = (ChannelBuffer) e.getMessage();
        final String sipMessageAsString = buf.toString(Charset.forName("UTF-8"));
        serverTransport.handleIncomingMessage(
                sipMessageAsString,
                e.getRemoteAddress(),
                null);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();

        Channel ch = e.getChannel();
        ch.close();
    }
}
