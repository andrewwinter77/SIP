package org.andrewwinter.sip.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author andrewwinter77
 */
public final class Util {
       
    /**
     *
     */
    public static final String CRLF = "\r\n";

    /**
     *
     */
    public static final String NEWLINE = "\n";

    /**
     *
     */
    public static final String SP = " ";
    
    private static final Set<HeaderName> UNCONDITIONAL_SYSTEM_HEADERS = new HashSet<HeaderName>();
        // Note that From and To are system header fields only with respect to
        // their tags (i.e., tag parameters on these headers are not allowed to
        // be modified but modifications are allowed to the other parts).

    static {
        Collections.addAll(UNCONDITIONAL_SYSTEM_HEADERS,
            HeaderName.FROM,
            HeaderName.TO,
            HeaderName.CALL_ID,
            HeaderName.CSEQ,
            HeaderName.VIA,

            HeaderName.RECORD_ROUTE);
//            HeaderName.PATH,
//            HeaderName.RACK,
//            HeaderName.RSEQ);
    }
    
    /**
     * 
     * @param hn
     * @return 
     */
    public static boolean headerCanBeAddress(final HeaderName hn) {
        return (hn.getType() == HeaderType.ADDRESS
             || hn.getType() == HeaderType.COMMA_SEPARATED_ADDRESSES
             || hn.getType() == HeaderType.COMMA_SEPARATED_ABSOLUTE_URI_WITH_PARAMS
              
             // Extension headers can contain anything.
             || hn.getType() == HeaderType.UNKNOWN_EXTENSION_HEADER);
    }
    
    /**
     * 
     * @param hn
     * @param message
     * @param callingMethodIsPushRoute
     * @return 
     */
    public static boolean isSystemHeader(
            final HeaderName hn,
            final SipMessage message,
            final boolean callingMethodIsPushRoute) {
        
        if (UNCONDITIONAL_SYSTEM_HEADERS.contains(hn)) {
            return true;
        } else if (hn.equals(HeaderName.CONTACT)) {

            // Contact is a system header field in messages other than REGISTER
            // requests and responses, 3xx and 485 responses, and 200/OPTIONS
            // responses.

            if (message instanceof SipRequest) {
                return !((SipRequest) message).isREGISTER();
            } else {
                final SipResponse response = (SipResponse) message;
                final int code = response.getStatusCode();
                return !(
                        (code >= 300 && code < 400) 
                        || code == 485
                        || (code == 200 && "OPTIONS".equals(response.getMethod())));
            }
        } else if (hn.equals(HeaderName.ROUTE)) {
            // Route (except through pushRoute)
            return !callingMethodIsPushRoute;
        } else {
            return false;
        }
    }
    
    /**
     * 
     * @param str
     * @param index Index to start searching from; the search does not include
     * the character at this index.
     * @return 
     * @throws IllegalArgumentException if there is no end to the quoted string
     *  or the string does not start with a quotes.
     */
    static int findEndOfQuotedString(final String str, final int index) throws IllegalArgumentException {
        if (str.charAt(index) != '"') {
            throw new IllegalArgumentException("Quoted string expected.");
        }
        
        int end = index;

        while (true) {
            end = str.indexOf('"', end+1);
            if (end == -1) {
                throw new IllegalArgumentException("No end to quoted string.");
            } else {
                if (str.charAt(end-1) == '\\') {
                    // The quote we found was escaped. It
                    // doesn't signify the end of this quoted string.
                    // Ignore it.
                } else {
                    break;
                }
            }
        }
        return end;
    }

    /**
     * 
     * @param str
     * @return
     * @throws IllegalArgumentException if there is no end to the quoted string
     *  or the string does not start with a quotes.
     */
    static int findEndOfQuotedString(final String str) throws IllegalArgumentException {
        return findEndOfQuotedString(str, 0);
    }

    /**
     * 
     * Comma-delimited segments.
     * @param str
     * @return 
     */
    static List<String> safeSplit(String str) {
        final List<String> results = new ArrayList<String>();

        int segmentStart = 0;
        int searchFrom = 0;
        while (true) {
            
            int indexOfQuotes = str.indexOf('"', searchFrom);
            
            if (indexOfQuotes == -1) {
                // No more quoted strings. Just split the remaining
                // string on the delimiter and we're done.

                int indexOfDelimiter = str.indexOf(',', searchFrom);
                if (indexOfDelimiter == -1) {
                    results.add(str.substring(segmentStart).trim());
                } else {
                    results.add(str.substring(segmentStart, indexOfDelimiter).trim());
                    
                    str = str.substring(indexOfDelimiter+1);
                    final String[] parts = str.split(",");
                    for (final String part : parts) {
                        if (part.length() > 0) {
                            results.add(part.trim());
                        }
                    }
                }
                break;
            } else {
                int indexOfDelimiter = str.indexOf(',', searchFrom);

                if (indexOfDelimiter == -1) {
                    // No delimiters remaining (in quoted strings or
                    // otherwise). A very uncontentious situation so
                    // just add the remaining string and we're done.
                    
                    results.add(str.substring(segmentStart).trim());
                    break;
                } else {
                    // There's a quoted string and a delimiter somewhere.
                    // If the delimiter comes before the quote then it's a
                    // real delimiter because it's outside of the quoted string.

                    if (indexOfDelimiter < indexOfQuotes) {
                        results.add(str.substring(segmentStart, indexOfDelimiter).trim());
                        searchFrom = indexOfDelimiter+1;
                        segmentStart = searchFrom;
                    } else {

                        // The quote comes first so the delimiter might be
                        // part of the quoted string. If so, it shouldn't be
                        // treated as a delimiter. Skip to the end of the quoted
                        // string and continue.
                        searchFrom = findEndOfQuotedString(str, indexOfQuotes)+1;
                    }
                }
            }
        }
        
        return results;
    }
    
    /**
     *
     * @param str
     * @return
     */
    public static String unfoldHeaders(final String str) {
        
        // SIP header field values can be folded onto multiple lines if the
        // continuation line begins with a space or horizontal tab. All linear
        // white space, including folding, has the same semantics as SP.        
        
        return str.replaceAll(Util.CRLF + " ", " ");
    }
    
    /**
     *
     * @param source
     * @return
     */
    public static String replaceCRLFwithLF(final String source) {
        return source.replaceAll(Util.CRLF, Util.NEWLINE);
    }
}
