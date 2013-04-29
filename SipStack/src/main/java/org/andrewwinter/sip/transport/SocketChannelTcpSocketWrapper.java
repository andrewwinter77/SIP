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
    
    /**
     *
     * @param tcpSocket
     */
    public SocketChannelTcpSocketWrapper(final SocketChannel tcpSocket) {
        this.tcpSocket = tcpSocket;
    }

    /**
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        tcpSocket.close();
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        return tcpSocket.isConnected();
    }

    /**
     *
     * @param bb
     * @throws IOException
     */
    @Override
    public void write(final ByteBuffer bb) throws IOException {
        tcpSocket.write(bb);
    }
}
