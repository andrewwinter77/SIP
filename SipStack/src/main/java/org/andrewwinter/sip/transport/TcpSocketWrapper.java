package org.andrewwinter.sip.transport;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * @author andrew
 */
public interface TcpSocketWrapper {
    
    /**
     *
     * @throws IOException
     */
    void close() throws IOException;
    
    /**
     *
     * @return
     */
    boolean isConnected();
    
    /**
     *
     * @param bb
     * @throws IOException
     */
    void write(ByteBuffer bb) throws IOException;
}
