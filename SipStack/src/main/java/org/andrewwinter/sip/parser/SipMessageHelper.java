package org.andrewwinter.sip.parser;

import java.util.List;

/**
 *
 * @author andrew
 */
public final class SipMessageHelper {

    private SipMessageHelper() {
    }
    
    /**
     *
     * @param expires
     * @param message
     */
    public static void setExpires(final int expires, final SipMessage message) {
        message.setHeader(HeaderName.EXPIRES, expires);
    }

    /**
     *
     * @param maxForwards
     * @param request
     */
    public static void setMaxForwards(final int maxForwards, final SipRequest request) {
        request.setHeader(HeaderName.MAX_FORWARDS, maxForwards);
    }

    /**
     *
     * @param expires
     * @param response
     */
    public static void setMinExpires(final int expires, final SipResponse response) {
        response.setHeader(HeaderName.MIN_EXPIRES, expires);
    }

    /**
     *
     * @param length
     * @param message
     */
    public static void setContentLength(final int length, final SipMessage message) {
        message.setHeader(HeaderName.CONTENT_LENGTH, length);
    }
    
    /**
     * 
     * @param contentType Allows pre-encoded params.
     * @param message  
     */
    public static void setContentType(final String contentType, final SipMessage message) {
        message.setHeader(HeaderName.CONTENT_TYPE, contentType);
    }

    /**
     *
     * @param vias
     * @param message  
     */
    public static void setVias(final List<Via> vias, final SipMessage message) {
        message.setHeaders(HeaderName.VIA, vias);
    }
    
    /**
     *
     * @param via
     * @param message  
     */
    public static void setVia(final Via via, final SipMessage message) {
        message.setHeader(HeaderName.VIA, via);
    }

    /**
     *
     * @param via
     * @param message  
     */
    public static void pushVia(final Via via, final SipMessage message) {
        message.pushHeader(HeaderName.VIA, via);
    }
    
    /**
     *
     * @param date
     * @param message  
     */
    public static void setDate(final String date, final SipMessage message) {
        message.setHeader(HeaderName.DATE, date);
    }

    /**
     * @param message 
     * @return 
     */
    public static Address getFrom(final SipMessage message) {
        return message.getAddress(HeaderName.FROM);
    }

    /**
     * @param message 
     * @return 
     */
    public static Address getTo(final SipMessage message) {
        return message.getAddress(HeaderName.TO);
    }
    
    /**
     *
     * @param address
     * @param message  
     */
    public static void setFrom(final Address address, final SipMessage message) {
        message.setHeader(HeaderName.FROM, address);
    }

    /**
     *
     * @param address 
     * @param message  
     */
    public static void setContact(final Address address, final SipMessage message) {
        message.setHeader(HeaderName.CONTACT, address);
    }
    
    /**
     *
     * @param address
     * @param message  
     */
    public static void setTo(final Address address, final SipMessage message) {
        message.setHeader(HeaderName.TO, address);
    }
    
    /**
     * @param message 
     * @return 
     */
    public static Address getReplyTo(final SipMessage message) {
        return message.getAddress(HeaderName.REPLY_TO);
    }

    /**
     *
     * @param message 
     * @return
     */
    public static String getOrganization(final SipMessage message) {
        return (String) message.getFirstOccurrenceOfHeader(HeaderName.ORGANIZATION);
    }
    
    /**
     *
     * @param message 
     * @return
     */
    public static String getSubject(final SipMessage message) {
        return (String) message.getFirstOccurrenceOfHeader(HeaderName.SUBJECT);
    }
    
    /**
     *
     * @param message 
     * @return
     */
    public static List<Address> getRecordRoutes(final SipMessage message) {
        return message.getAddressHeaders(HeaderName.RECORD_ROUTE);
    }
    
    /**
     *
     * @param message
     * @return
     */
    public static List<Address> getCallInfo(final SipMessage message) {
        return message.getAddressHeaders(HeaderName.CALL_INFO);
    }
    
    /**
     *
     * @param message 
     * @return
     */
    public static List<Address> getContact(final SipMessage message) {
        return message.getAddressHeaders(HeaderName.CONTACT);
    }

    /**
     *
     * @param message 
     * @return
     */
    public static Parameterable getContentType(final SipMessage message) {
        return message.getParameterable(HeaderName.CONTENT_TYPE);
    }
    
    /**
     *
     * @param message 
     * @return
     */
    public static Parameterable getContentDisposition(final SipMessage message) {
        return message.getParameterable(HeaderName.CONTENT_DISPOSITION);
    }

    /**
     *
     * @param timestamp
     * @param message  
     */
    public static void setTimestamp(final Timestamp timestamp, final SipMessage message) {
        message.setHeader(HeaderName.TIMESTAMP, timestamp);
    }
    
    /**
     *
     * @param message 
     * @return
     * @throws NumberFormatException if the Expires header exists and does not
     * contain a parsable {@code int}.
     */
    public static Float getMimeVersion(final SipMessage message) throws NumberFormatException {
        return message.getFloat(HeaderName.MIME_VERSION);
    }

    /**
     *
     * @param recordRoute
     * @param message  
     */
    public static void pushRecordRoute(final Address recordRoute, final SipMessage message) {
        if (recordRoute.getDisplayName() == null) {
            recordRoute.setDisplayName("");
        }
        message.pushHeader(HeaderName.RECORD_ROUTE, recordRoute.toString());
    }

    /**
     *
     * @param recordRoutes
     * @param message  
     */
    public static void setRecordRoutes(final List<Address> recordRoutes, final SipMessage message) {
        for (final Address rr : recordRoutes) {
            if (rr.getDisplayName() == null) {
                rr.setDisplayName(""); 
            }
        }
        message.setHeaders(HeaderName.RECORD_ROUTE, recordRoutes);
    }

    /**
     *
     * @param message 
     * @return
     */
    public static Address getTopmostRoute(final SipMessage message) {
        return message.getAddress(HeaderName.ROUTE);
    }

    /**
     *
     * @param message 
     * @return
     */
    public static Address getTopmostRecordRoute(final SipMessage message) {
        return message.getAddress(HeaderName.RECORD_ROUTE);
    }

    /**
     *
     * @param message 
     * @return
     * @throws NumberFormatException if the Expires header exists and does not
     * contain a parsable {@code int}.
     */
    public static Integer getExpires(final SipMessage message) throws NumberFormatException {
        return message.getInteger(HeaderName.EXPIRES);
    }

    /**
     * 
     * @param message 
     * @return Null if there is no Content-Length header.
     * @throws NumberFormatException  
     */
    public static Integer getContentLength(final SipMessage message) throws NumberFormatException {
        return message.getInteger(HeaderName.CONTENT_LENGTH);
    }
}
