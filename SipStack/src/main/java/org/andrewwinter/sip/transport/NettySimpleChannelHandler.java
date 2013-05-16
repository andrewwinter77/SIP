package org.andrewwinter.sip.transport;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import org.andrewwinter.sip.parser.Util;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.UpstreamMessageEvent;

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
            
            System.out.println("Approx free memory: " + Runtime.getRuntime().freeMemory());
            
            ChannelBuffer buf = (ChannelBuffer) event.getMessage();
            final String sipMessageAsString = buf.toString(Charset.forName("UTF-8"));
            
            if (((InetSocketAddress) event.getRemoteAddress()).getPort() > 0) {
                
                if (sipMessageAsString.equals(Util.CRLF + Util.CRLF)) {
                    
                    // Ping. Send back a pong.
                    // See http://tools.ietf.org/html/rfc5626
                    serverTransport.handlePing((InetSocketAddress) event.getRemoteAddress(), null);
                    
                } else if (sipMessageAsString.equals(Util.CRLF)) {
                    
                    // CRLF. What is this?
                    
                } else {
                    serverTransport.handleIncomingMessage(
                            sipMessageAsString,
                            (InetSocketAddress) event.getRemoteAddress(),
                            null);
                }
                
            } else {
                // TODO: What's going on here?
                // Is the buffer empty? If so, why did this method even get called? Investigate.
                
                final StringBuilder sb = new StringBuilder();
                sb.append("buf.array().length = " + buf.array().length + "\n");
                sb.append("Dropped message is [" + sipMessageAsString + "]" + sipMessageAsString.length() + " bytes\n");
                sb.append("Received from address " + event.getRemoteAddress() + "\n");
                sb.append("event instanceof UpstreamMessageEvent: " + (event instanceof UpstreamMessageEvent) + "\n");
                if (event.getRemoteAddress() != null) {
                    sb.append(((InetSocketAddress) event.getRemoteAddress()).getHostName());
                }
                if (ctx.getChannel() == null) {
                    sb.append("ctx.getChannel is null\n");
                } else if (ctx.getChannel().getRemoteAddress() == null) {
                    sb.append("ctx.getChannel().getRemoteAddress is null\n");
                } else {
                    sb.append("ctx.getChannel().getRemoteAddress().getPort() is ").append(((InetSocketAddress) ctx.getChannel().getRemoteAddress()).getPort()).append("\n");
                }
                if (event.getChannel() == null) {
                    sb.append("event.getChannel is null\n");
                } else if (event.getChannel().getRemoteAddress() == null) {
                    sb.append("event.getChannel().getRemoteAddress is null\n");
                } else {
                    sb.append("event.getChannel().getRemoteAddress().getPort() is ").append(((InetSocketAddress) event.getChannel().getRemoteAddress()).getPort()).append("\n");
                }
                sb.append("event.getRemoteAddress is " + event.getRemoteAddress());
                System.out.println("Dropping packets received on port 0 with " + sb);

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
