package org.andrewwinter.sip.transport;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 *
 * @author andrew
 */
public class SocketChannelTcpSocketWrapper implements TcpSocketWrapper {

    private final SocketChannel tcpSocket;
    
    public SocketChannelTcpSocketWrapper(final SocketChannel tcpSocket) {
        this.tcpSocket = tcpSocket;
    }

    public void close() throws IOException {
        tcpSocket.close();
    }

    public boolean isConnected() {
        return tcpSocket.isConnected();
    }

    public void write(final ByteBuffer bb) throws IOException {
        tcpSocket.write(bb);
    }
}
