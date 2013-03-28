package org.andrewwinter.sip.transport;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * @author andrew
 */
public interface TcpSocketWrapper {
    
    void close() throws IOException;
    
    boolean isConnected();
    
    void write(ByteBuffer bb) throws IOException;
}
