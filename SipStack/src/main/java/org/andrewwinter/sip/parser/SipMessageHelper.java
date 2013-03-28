package org.andrewwinter.sip.parser;

import java.util.List;

/**
 *
 * @author andrew
 */
public final class SipMessageHelper {

    private SipMessageHelper() {
    }
    
    public static void setExpires(final int expires, final SipMessage message) {
        message.setHeader(HeaderName.EXPIRES, expires);
    }

    public static void setMaxForwards(final int maxForwards, final SipRequest request) {
        request.setHeader(HeaderName.MAX_FORWARDS, maxForwards);
    }

    public static void setMinExpires(final int expires, final SipResponse response) {
        response.setHeader(HeaderName.MIN_EXPIRES, expires);
    }

    public static void setContentLength(final int length, final SipMessage message) {
        message.setHeader(HeaderName.CONTENT_LENGTH, length);
    }
    
    /**
     * 
     * @param contentType Allows pre-encoded params.
     */
    public static void setContentType(final String contentType, final SipMessage message) {
        message.setHeader(HeaderName.CONTENT_TYPE, contentType);
    }

    /**
     *
     * @param vias
     */
    public static void setVias(final List<Via> vias, final SipMessage message) {
        message.setHeaders(HeaderName.VIA, vias);
    }
    
    /**
     *
     * @param via
     */
    public static void setVia(final Via via, final SipMessage message) {
        message.setHeader(HeaderName.VIA, via);
    }

    /**
     *
     * @param via
     */
    public static void pushVia(final Via via, final SipMessage message) {
        message.pushHeader(HeaderName.VIA, via);
    }
    
    /**
     *
     * @param date
     */
    public static void setDate(final String date, final SipMessage message) {
        message.setHeader(HeaderName.DATE, date);
    }

    /**
     * @return 
     */
    public static Address getFrom(final SipMessage message) {
        return message.getAddress(HeaderName.FROM);
    }

    /**
     * @return 
     */
    public static Address getTo(final SipMessage message) {
        return message.getAddress(HeaderName.TO);
    }
    
    /**
     *
     * @param address
     */
    public static void setFrom(final Address address, final SipMessage message) {
        message.setHeader(HeaderName.FROM, address);
    }

    /**
     *
     * @param uri
     */
    public static void setContact(final Address address, final SipMessage message) {
        message.setHeader(HeaderName.CONTACT, address);
    }
    
    /**
     *
     * @param address
     */
    public static void setTo(final Address address, final SipMessage message) {
        message.setHeader(HeaderName.TO, address);
    }
    
    /**
     * @return 
     */
    public static Address getReplyTo(final SipMessage message) {
        return message.getAddress(HeaderName.REPLY_TO);
    }

    /**
     *
     * @return
     */
    public static String getOrganization(final SipMessage message) {
        return (String) message.getFirstOccurrenceOfHeader(HeaderName.ORGANIZATION);
    }
    
    /**
     *
     * @return
     */
    public static String getSubject(final SipMessage message) {
        return (String) message.getFirstOccurrenceOfHeader(HeaderName.SUBJECT);
    }
    
    /**
     *
     * @return
     */
    public static List<Address> getRecordRoutes(final SipMessage message) {
        return message.getAddressHeaders(HeaderName.RECORD_ROUTE);
    }
    
    public static List<Address> getCallInfo(final SipMessage message) {
        return message.getAddressHeaders(HeaderName.CALL_INFO);
    }
    
    /**
     *
     * @return
     */
    public static List<Address> getContact(final SipMessage message) {
        return message.getAddressHeaders(HeaderName.CONTACT);
    }

    /**
     *
     * @return
     */
    public static Parameterable getContentType(final SipMessage message) {
        return message.getParameterable(HeaderName.CONTENT_TYPE);
    }
    
    /**
     *
     * @return
     */
    public static Parameterable getContentDisposition(final SipMessage message) {
        return message.getParameterable(HeaderName.CONTENT_DISPOSITION);
    }

    /**
     *
     * @param timestamp
     */
    public static void setTimestamp(final Timestamp timestamp, final SipMessage message) {
        message.setHeader(HeaderName.TIMESTAMP, timestamp);
    }
    
    /**
     *
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
     * @return
     */
    public static Address getTopmostRoute(final SipMessage message) {
        return message.getAddress(HeaderName.ROUTE);
    }

    /**
     *
     * @return
     */
    public static Address getTopmostRecordRoute(final SipMessage message) {
        return message.getAddress(HeaderName.RECORD_ROUTE);
    }

    /**
     *
     * @return
     * @throws NumberFormatException if the Expires header exists and does not
     * contain a parsable {@code int}.
     */
    public static Integer getExpires(final SipMessage message) throws NumberFormatException {
        return message.getInteger(HeaderName.EXPIRES);
    }

    /**
     * 
     * @return Null if there is no Content-Length header.
     */
    public static Integer getContentLength(final SipMessage message) throws NumberFormatException {
        return message.getInteger(HeaderName.CONTENT_LENGTH);
    }
}
