package org.andrewwinter.sip.message;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author andrew
 */
class ResponseTypeMap {

    private static final Map<Integer, ResponseType> MAP;
    
    static {
        MAP = new HashMap<Integer, ResponseType>();
        for (ResponseType rt : ResponseType.values()) {
            MAP.put(rt.getStatusCode(), rt);
        }
    }
    
    /**
     * Returns the {@code ResponseType} object matching the given status code,
     * of {@code null} if no matching {@code ResponseType} could be found.
     * @param statusCode
     * @return 
     */
    static ResponseType get(final int statusCode) {
        return MAP.get(statusCode);
    }
}
