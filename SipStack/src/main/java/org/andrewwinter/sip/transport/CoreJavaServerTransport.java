package org.andrewwinter.sip.transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.andrewwinter.sip.SipRequestHandler;

/**
 *
 * @author andrew
 */
public class CoreJavaServerTransport extends ServerTransport implements Runnable {

    /**
     * The port we'll listen on.
     */
    private final SocketAddress localPort;
    
    private ServerSocketChannel tcpserver;

    private DatagramChannel udpserver;
    
    private Selector selector;
    
    private boolean listenForTraffic;

    public CoreJavaServerTransport(final SipRequestHandler sipListener) {
        this(sipListener, 5060);
    }

    public CoreJavaServerTransport(final SipRequestHandler sipListener, final int tcpPort) {
        super(sipListener);
        this.localPort = new InetSocketAddress(tcpPort);
        this.listenForTraffic = false;
    }
    
    /**
     *
     */
    @Override
    public void stopListening() throws IOException {
        listenForTraffic = false;
        if (selector != null) {
            try {
                selector.close();
            } catch (final IOException e) {
                System.out.println("Error closing connection.");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void listen() {
        final ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.submit(this);
    }

    private void listenInternal() {
        listenForTraffic = true;
        try {
            // Create to bind a tcp channel to listen for connections on.
            tcpserver = ServerSocketChannel.open();
            tcpserver.socket().bind(localPort);

            // Also create and bind a DatagramChannel to listen on.
            udpserver = DatagramChannel.open();
            udpserver.socket().bind(localPort);

            // Specify non-blocking mode for both channels, since our
            // Selector object will be doing the blocking for us.
            tcpserver.configureBlocking(false);
            udpserver.configureBlocking(false);

            // The Selector object is what allows us to block while waiting
            // for activity on either of the two channels.
            selector = Selector.open();

            // Register the channels with the selector, and specify what
            // conditions (a connection ready to accept, a datagram ready
            // to read) we'd like the Selector to wake up for.
            // These methods return SelectionKey objects, which we don't
            // need to retain in this example.
            tcpserver.register(selector, SelectionKey.OP_ACCEPT);
            udpserver.register(selector, SelectionKey.OP_READ);

            // This is an empty byte buffer to receive emtpy datagrams with.
            // If a datagram overflows the receive buffer size, the extra bytes
            // are automatically discarded, so we don't have to worry about
            // buffer overflow attacks here.
            final ByteBuffer receiveBuffer = ByteBuffer.allocate(65535);

            // Now loop forever, processing client connections
            while (listenForTraffic) {
                try { // Handle per-connection problems below
                    // Wait for a client to connect
                    selector.select();

                    // If we get here, a client has probably connected.
                    
                    // Get the SelectionKey objects for the channels that have
                    // activity on them. These are the keys returned by the
                    // register() methods above. They are returned in a
                    // java.util.Set.
                    final Set<SelectionKey> keys = selector.selectedKeys();
                    
                    System.out.println("Received connection");
                    SocketAddress remoteAddress = null;

                    SocketChannel tcpSocket = null;
                    SocketChannelTcpSocketWrapper tcpSocketWrapper = null;
                    for (final SelectionKey key : keys) {

                        // Get the channel associated with the key
                        final Channel c = (Channel) key.channel();

                        // Now test the key and the channel to find out
                        // whether something happend on the TCP or UDP channel
                        if (key.isAcceptable() && c == tcpserver) {
                            // A client has attempted to connect via TCP.
                            // Accept the connection now.
                            tcpSocket = tcpserver.accept();
                            remoteAddress = tcpSocket.socket().getRemoteSocketAddress();
                            int bytesRead = tcpSocket.read(receiveBuffer);
                            tcpSocketWrapper = new SocketChannelTcpSocketWrapper(tcpSocket);
                            break;
                        } else if (key.isReadable() && c == udpserver) {
                            // A UDP datagram is waiting. Receive it now,
                            // noting the address it was sent from.
                            remoteAddress = udpserver.receive(receiveBuffer);
                            break;
                        }
                    }
                    
                    
                    if (receiveBuffer.position() > 0) {
       
                        receiveBuffer.flip();
                        
                        // TEMPORARY - something gets sent to us that isn't a SIP message that causes us to crash
                        if (receiveBuffer.position() < 10) {
                            for (int i = 0; i < receiveBuffer.position(); ++i) {
                                System.out.println("DEBUG: Char at " + i + " is " + receiveBuffer.get(i));
                            }
                        }
                        // END TEMPORARY
                        
                        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
                        CharBuffer cb = decoder.decode(receiveBuffer);
                        receiveBuffer.clear();
                        final String messageAsString = cb.toString();
                        
                        handleIncomingMessage(
                                messageAsString,
                                (InetSocketAddress) remoteAddress,
                                tcpSocketWrapper);
                        
                        receiveBuffer.clear();
                    }
                    
                } catch (java.io.IOException e) {
                    // This is a (hopefully transient) problem with a single
                    // connection: we log the error, but continue running.
                    // We use our classname for the logger so that a sysadmin
                    // can configure logging for this server independently
                    // of other programs.
                    e.printStackTrace();
                } catch (Throwable t) {
                    // If anything else goes wrong (out of memory, for example)
                    // then log the problem and exit.
                    t.printStackTrace();
                    System.exit(1);
                }
            }
        } catch (IOException e) {
            // This is a startup error: there is no need to log it;
            // just print a message and exit
            System.err.println(e);
            System.exit(1);
        }
        System.out.println("Finished");
    }

    public void run() {
        listenInternal();
    }
}
