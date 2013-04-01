package org.andrewwinter.sip.location;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipServletMessage;
import javax.servlet.sip.SipURI;

/**
 *
 * @author andrew
 */
public class Util {

    /**
     * The address-of-record MUST be a SIP URI or SIPS URI so other schemes are
     * not considered in this method. Modifies the copy passed in, but also
     * returns it for convenience.
     *
     * @param uri
     * @return
     */
    public static SipURI canonicalizeUri(final SipURI uri) {

        // The URI MUST then be converted to a canonical form. To do that, all
        // URI parameters MUST be removed (including the user-param), and any
        // escaped characters MUST be converted to their unescaped form.

        Iterator<String> iter = uri.getParameterNames();
        while (iter.hasNext()) {
            uri.removeParameter(iter.next());
        }

        // TODO: Unescape any escaped characters.

        return uri;
    }

    /**
     * 
     * @param message
     * @return
     * @throws ServletParseException 
     */
    public static int getCSeqValue(final SipServletMessage message) throws ServletParseException {
        final String cseq = message.getHeader("CSeq");
        if (cseq == null) {
            throw new ServletParseException("Missing CSeq header");
        }

        final String[] parts = cseq.trim().split("\\s+");
        try {
            return Integer.parseInt(parts[0]);
        } catch (NumberFormatException e) {
            throw new ServletParseException("Illegal CSeq value");
        }
    }

    /**
     * 
     * @param str
     * @return 
     */
    private static String decode(final String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }

    /**
     * 
     * @param iter
     * @return 
     */
    private static Set<String> toSet(final Iterator<String> iter) {
        final Set<String> result = new HashSet<>();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    /**
     * 
     * @param a
     * @param b
     * @return 
     */
    public static boolean equalsUsingComparisonRules(final SipURI a, final SipURI b) {

        if (!a.getScheme().equals(b.getScheme())) {

            // A SIP and SIPS URI are never equivalent.

            return false;
        }

        // For two URIs to be equal, the user, password, host, and port
        // components must match. A URI omitting the user component will not
        // match a URI that includes one. A URI omitting the password component
        // will not match a URI that includes one.

        // Comparison of the userinfo of SIP and SIPS URIs is case-sensitive.
        // This includes userinfo containing passwords or formatted as
        // telephone-subscribers.

        if ((a.getUser() == null) ? (b.getUser() != null) : !decode(a.getUser()).equals(decode(b.getUser()))) {
            return false;
        }

        // TODO: Match passwords
//        if ((a.password == null) ? (other.password != null) : !password.equals(other.password)) {
//            return false;
//        }


        // Comparison of all other components of the URI is case-insensitive
        // unless explicitly defined otherwise.

        if ((a.getHost() == null) ? (b.getHost() != null) : !a.getHost().equalsIgnoreCase(b.getHost())) {
            return false;
        }

        // A URI omitting any component with a default value will not match a
        // URI explicitly containing that component with its default value. For
        // instance, a URI omitting the optional port component will not match a
        // URI explicitly declaring port 5060. 

        if (a.getPort() != b.getPort()) {
            return false;
        }

        // The same is true for the transport-parameter, ttlparameter,
        // user-parameter, and method components.

        // A user, ttl, or method uri-parameter appearing in only one URI never
        // matches, even if it contains the default value.

        final String[] specialParams = new String[]{"transport", "ttl", "user", "method"};
        for (final String param : specialParams) {
            if ((a.getParameter(param) == null) ? (b.getParameter(param) != null) : !a.getParameter(param).equalsIgnoreCase(b.getParameter(param))) {
                return false;
            }
        }

        // Any uri-parameter appearing in both URIs must match.

        // All other uri-parameters appearing in only one URI are ignored when
        // comparing the URIs.

        final Set<String> intersectionOfParams = new HashSet<>();
        intersectionOfParams.addAll(toSet(a.getParameterNames()));
        intersectionOfParams.retainAll(toSet(b.getParameterNames()));
        for (final String param : intersectionOfParams) {
            if (!a.getParameter(param).equalsIgnoreCase(b.getParameter(param))) {
                return false;
            }
        }


        // URI header components are never ignored. Any present header component
        // MUST be present in both URIs and match for the URIs to match. The
        // matching rules are defined for each header field in Section 20.

        final Set<String> unionOfHeaders = new HashSet<>();
        unionOfHeaders.addAll(toSet(a.getHeaderNames()));
        unionOfHeaders.addAll(toSet(b.getHeaderNames()));
        for (final String header : unionOfHeaders) {
            // TODO: Should we use equals or equalsIgnoreCase here?
            if (a.getHeader(header) == null || b.getHeader(header) == null || !a.getHeader(header).equals(b.getHeader(header))) {
                return false;
            }
        }


        // A URI that includes an maddr parameter will not match a URI that
        // contains no maddr parameter.

        if ((a.getParameter("maddr") == null) != (b.getParameter("maddr") == null)) {
            return false;
        }

        // All other uri-parameters appearing in only one URI are ignored when
        // comparing the URIs

        // TODO: Finish URI comparison - do the above



        return true;
    }
}
