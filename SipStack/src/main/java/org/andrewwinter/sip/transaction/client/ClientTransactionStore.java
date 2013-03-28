package org.andrewwinter.sip.transaction.client;

import java.util.HashMap;
import java.util.Map;
import org.andrewwinter.sip.parser.SipResponse;

/**
 *
 * @author andrewwinter77
 */
public class ClientTransactionStore {
    
    private final Map<String, ClientTransaction> txnMap;
    
    private static ClientTransactionStore INSTANCE = new ClientTransactionStore();
    
    /**
     *
     * @return
     */
    public static ClientTransactionStore getInstance() {
        return INSTANCE;
    }
    
    private ClientTransactionStore() {
        txnMap = new HashMap<String, ClientTransaction>();
    }
    
    /**
     * 
     * @param txn The client transaction to be stored.
     */
    public void put(final ClientTransaction txn) {
        final String key = ClientTransactionUtil.getKey(txn.getRequest());
        txnMap.put(key, txn);
    }
    
    /**
     * Returns null if there are no matching transactions;
     * @param response
     * @return 
     */
    public ClientTransaction get(final SipResponse response) {
        final String key = ClientTransactionUtil.getKey(response);
        return txnMap.get(key);
    }

    /**
     *
     * @param txn
     */
    public void remove(final ClientTransaction txn) {
        final String key = ClientTransactionUtil.getKey(txn.getRequest());
        txnMap.remove(key);
    }
}
