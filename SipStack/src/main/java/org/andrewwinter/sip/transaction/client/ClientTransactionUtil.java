package org.andrewwinter.sip.transaction.client;

import org.andrewwinter.sip.parser.SipMessage;

/**
 *
 * @author andrewwinter77
 */
public final class ClientTransactionUtil {
    
    private ClientTransactionUtil() {
    }
    
    /**
     * See 17.1.3.
     * @param msg
     * @return 
     */
    public static String getKey(final SipMessage msg) {
        
        // A response matches a client transaction under two conditions:
        //
        // 1. If the response has the same value of the branch parameter in the
        // top Via header field as the branch parameter in the top Via header
        // field of the request that created the transaction.
        //
        // 2. If the method parameter in the CSeq header field matches the
        // method of the request that created the transaction. The method is
        // needed since a CANCEL request constitutes a different transaction,
        // but shares the same value of the branch parameter.

        final String method = msg.getCSeq().getMethod();
        final String branch = msg.getTopmostVia().getBranch();

        return method + "/" + branch;
    }
}
