package org.andrewwinter.sip.message;

/**
 *
 * @author andrewwinter77
 */
public enum ResponseType {
    
    /**
     * Constant for 100 Trying.
     */
    TRYING(100, "Trying"),
    
    /**
     * Constant for 180 Ringing.
     */
    RINGING(180, "Ringing"),
    
    /**
     * Constant for 181 Call Is Being Forwarded.
     */
    CALL_IS_BEING_FORWARDED(181, "Call Is Being Forwarded"),
    
    /**
     * Constant for 182 Queued.
     */
    QUEUED(182, "Queued"),
    
    /**
     * Constant for 183 Session Progress.
     */
    SESSION_PROGRESS(183, "Session Progress"),
    
    /**
     * Constant for 200 OK.
     */
    OK(200, "OK"),
    
    /**
     * Constant for 300 Multiple Choices.
     */
    MULTIPLE_CHOICES(300, "Multiple Choices"),
    
    /**
     * Constant for 301 Moved Permanently.
     */
    MOVED_PERMANENTLY(301, "Moved Permanently"),
    
    /**
     * Constant for 302 Moved Temporarily.
     */
    MOVED_TEMPORARILY(302, "Moved Temporarily"),
    
    /**
     * Constant for 305 Use Proxy.
     */
    USE_PROXY(305, "Use Proxy"),
    
    /**
     * Constant for 380 Alternative Service.
     */
    ALTERNATIVE_SERVICE(380, "Alternative Service"),
    
    /**
     * Constant for 400 Bad Request.
     */
    BAD_REQUEST(400, "Bad Request"),
    
    /**
     * Constant for 401 Unauthorized.
     */
    UNAUTHORIZED(401, "Unauthorized"),
    
    /**
     * Constant for Payment Required.
     */
    PAYMENT_REQUIRED(402, "Payment Required"),
    
    /**
     * Constant for 403 Forbidden.
     */
    FORBIDDEN(403, "Forbidden"),
    
    /**
     * Constant for 404 Not Found.
     */
    NOT_FOUND(404, "Not Found"),
    
    /**
     * Constant for 405 Method Not Allowed.
     */
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    
    /**
     * Constant for 406 Not Acceptable.
     */
    NOT_ACCEPTABLE_406(406, "Not Acceptable"),
    
    /**
     * Constant for 407 Proxy Authentication Required.
     */
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
    
    /**
     * Constant for 408 Request Timeout.
     */
    REQUEST_TIMEOUT(408, "Request Timeout"),
    
    /**
     * Constant for 410 Gone.
     */
    GONE(410, "Gone"),
    
    /**
     * Constant for 413 Request Entity Too Large.
     */
    REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),
    
    /**
     * Constant for 414 Request-URI Too Long.
     */
    REQUEST_URI_TOO_LONG(414, "Request-URI Too Long"),
    
    /**
     * Constant for 415 Unsupported Media Type.
     */
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
    
    /**
     * Constant for 416 Unsupported URI Scheme.
     */
    UNSUPPORTED_URI_SCHEME(416, "Unsupported URI Scheme"),
    
    /**
     * Constant for 420 Bad Extension.
     */
    BAD_EXTENSION(420, "Bad Extension"),
    
    /**
     * Constant for 421 Extension Required.
     */
    EXTENSION_REQUIRED(421, "Extension Required"),
    
    /**
     * Constant for 423 Interval Too Brief.
     */
    INTERVAL_TOO_BRIEF(423, "Interval Too Brief"),
    
    /**
     * Constant for 480 Temporarily Unavailable.
     */
    TEMPORARILY_UNAVAILABLE(480, "Temporarily Unavailable"),
    
    /**
     * Constant for 481 Call/Transaction Does Not Exist.
     */
    CALL_TRANSACTION_DOES_NOT_EXIST(481, "Call/Transaction Does Not Exist"),
    
    /**
     * Constant for 482 Loop Detected.
     */
    LOOP_DETECTED(482, "Loop Detected"),
    
    /**
     * Constant for 483 Too Many Hops.
     */
    TOO_MANY_HOPS(483, "Too Many Hops"),
    
    /**
     * Constant for 484 Address Incomplete.
     */
    ADDRESS_INCOMPLETE(484, "Address Incomplete"),
    
    /**
     * Constant for 485 Ambiguous.
     */
    AMBIGUOUS(485, "Ambiguous"),
    
    /**
     * Constant for 486 Busy Here.
     */
    BUSY_HERE(486, "Busy Here"),
    
    /**
     * Constant for 487 Request Terminated.
     */
    REQUEST_TERMINATED(487, "Request Terminated"),
    
    /**
     * Constant for 488 Not Acceptable Here.
     */
    NOT_ACCEPTABLE_HERE(488, "Not Acceptable Here"),
    
    /**
     * Constant for 491 Request Pending.
     */
    REQUEST_PENDING(491, "Request Pending"),
    
    /**
     * Constant for 493 Undecipherable.
     */
    UNDECIPHERABLE(493, "Undecipherable"),
    
    /**
     * Constant for 500 Server Internal Error.
     */
    SERVER_INTERNAL_ERROR(500, "Server Internal Error"),
    
    /**
     * Constant for 501 Not Implemented.
     */
    NOT_IMPLEMENTED(501, "Not Implemented"),
    
    /**
     * Constant for 502 Bad Gateway.
     */
    BAD_GATEWAY(502, "Bad Gateway"),
    
    /**
     * Constant for 503 Service Unavailable.
     */
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    
    /**
     * Constant for 504 Server Time-out.
     */
    SERVER_TIMEOUT(504, "Server Time-out"),
    
    /**
     * Constant for 505 Version Not Supported.
     */
    VERSION_NOT_SUPPORTED(505, "Version Not Supported"),
    
    /**
     * Constant for 513 Message Too Large.
     */
    MESSAGE_TOO_LARGE(513, "Message Too Large"),
    
    /**
     * Constant for 600 Busy Everywhere.
     */
    BUSY_EVERYWHERE(600, "Busy Everywhere"),
    
    /**
     * Constant for 603 Decline.
     */
    DECLINE(603, "Decline"),
    
    /**
     * Constant for 604 Does Not Exist Anywhere.
     */
    DOES_NOT_EXIST_ANYWHERE(604, "Does Not Exist Anywhere"),
    
    /**
     * Constant for 606 Not Acceptable.
     */
    NOT_ACCEPTABLE_606(606, "Not Acceptable");

    private final int statusCode;
    private final String reasonPhrase;
    
    /**
     * 
     * @param statusCode
     * @param reasonPhrase 
     */
    private ResponseType(final int statusCode, final String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }
    
    /**
     * 
     * @return 
     */
    public int getStatusCode() {
        return statusCode;
    }
    
    /**
     * 
     * @return 
     */
    public String getReasonPhrase() {
        return reasonPhrase;
    }
    
    /**
     * Returns the {@code ResponseType} object matching the given status code,
     * of {@code null} if no matching {@code ResponseType} could be found.
     * @param statusCode
     * @return 
     */
    public static ResponseType get(final int statusCode) {
        return ResponseTypeMap.get(statusCode);
    }
}
