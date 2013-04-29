package org.andrewwinter.sip.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andrewwinter77
 */
public class TelUrl extends Uri {

    private static final Logger LOG = LoggerFactory.getLogger(TelUrl.class);
    
    private String uriAsString;

    private String number;
    
    @Override
    public String getScheme() {
        return "tel";
    }
    
    /**
     *
     * @param number
     * @throws ParseException
     */
    public void setGlobalNumber(final String number) throws ParseException {
        
        // TODO: Parse to make sure the number is valid

        this.number = number;
        setParameter("phone-context", null);
    }
    
    /**
     *
     * @param number
     * @param phoneContext
     * @throws ParseException
     */
    public void setLocalNumber(final String number, final String phoneContext) throws ParseException {
        
        // TODO: Parse to make sure the number is valid
        
        this.number = number;
        setParameter("phone-context", phoneContext);
    }
    
    /**
     * For internal use while parsing only. This method neither causes the
     * "phone-context" param to be added or removed.
     * @param number 
     */
    private void setLocalGlobalAgnosticNumber(final String number) {
        this.number = number;
    }
    
    /**
     *
     * @return
     */
    public String getNumber() {
        return number;
    }
    
    private TelUrl() {
    }
    
    private void setUriAsString(String uriAsString) {
        this.uriAsString = uriAsString;
    }

    /**
     * Assumes string starts with "tel:" - unpredictable results will arise if
     * this is not the case.
     * @param str
     * @return
     * @throws ParseException  
     */
    public static TelUrl parse(String str) throws ParseException {
        final TelUrl uri = new TelUrl();
        
        uri.setUriAsString(str);

        // An assumption of this parser is that the string starts with "tel:"
        str = str.substring(4);
        
        int endOfNumber = str.indexOf(';');
        if (endOfNumber == -1) {
            uri.setLocalGlobalAgnosticNumber(str);
        } else {
            uri.setLocalGlobalAgnosticNumber(str.substring(0, endOfNumber));
            str = str.substring(endOfNumber);
            uri.parseParams(str);
        }
        
        return uri;
    }
    
    private static void moveToFront(final List<String> list, final String key) {
        int index = list.indexOf(key);
        if (index > 0) {
            Collections.swap(list, 0, index);
        }
    }
    
    /**
     *
     * @param domain
     * @param secure
     * @return
     */
    public SipUri toSipUri(final String domain, final boolean secure) {
        final StringBuilder sb = new StringBuilder();
        if (secure) {
            sb.append("sips:");
        } else {
            sb.append("sip:");
        }
        
        sb.append(number);

        // and order the telephone-subscriber parameters lexically by parameter
        // name
        
        final List<String> paramNames = new ArrayList<String>();
        paramNames.addAll(getParameterNames());
        Collections.sort(paramNames);
        
        // excepting isdn-subaddress and post-dial, which occur first and in
        // that order.
        moveToFront(paramNames, "post-dial");
        moveToFront(paramNames, "isdn-subaddress");
        
        for (final String paramName : paramNames) {
            sb.append(";").append(paramName);
            String paramValue = getParameter(paramName);
            if (paramValue.length() > 0) {
                sb.append("=");
                if (paramValue.charAt(0) == '"') {
                    sb.append(paramValue);
                } else {
                    
                    // To mitigate this problem, elements constructing
                    // telephone-subscriber fields to place in the userinfo part
                    // of a SIP or SIPS URI SHOULD fold any case-insensitive
                    // portion of telephone-subscriber to lower case
                    
                    sb.append(paramValue.toLowerCase(Locale.US));
                }
            }
        }
        
        sb.append("@").append(domain);
        
        sb.append(";user=phone");
        
        try {
            return SipUri.parse(sb.toString());
        } catch (ParseException e) {
            LOG.info("Failed to convert tel URL to SIP URI: {}", sb.toString());
            return null;
        }
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        if (uriAsString == null) {
            
            final StringBuilder sb = new StringBuilder();
            sb.append("tel:");
            sb.append(number);
            sb.append(toParamString());
            uriAsString = sb.toString();
        }
        
        return uriAsString;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.number != null ? this.number.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TelUrl other = (TelUrl) obj;
        
        // TODO: Implement proper equality testing according to RFC 3966 section 5.
        throw new UnsupportedOperationException("TODO: Implement proper equality testing");
        
//        if ((this.number == null) ? (other.number != null) : !this.number.equals(other.number)) {
//            return false;
//        }
//        return true;
    }
    
    
}
