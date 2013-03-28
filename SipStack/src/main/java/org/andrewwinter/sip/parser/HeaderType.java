package org.andrewwinter.sip.parser;

/**
 *
 * @author andrewwinter77
 */
public enum HeaderType {
    AUTHENTICATION_CHALLENGE(false),
    INTEGER(false),
    FLOAT(false),
    OPAQUE_STRING(false),
    COMMA_SEPARATED_TOKENS(false),
    COMMA_SEPARATED_STRING_WITH_PARAMS(true),
    COMMA_SEPARATED_ADDRESSES(true),
    STRING_WITH_PARAMS(true),
    ADDRESS(true),
    HEADER_SPECIFIC_FORMAT(false),
    PARAMETERABLE_HEADER_SPECIFIC_FORMAT(true),
    COMMA_SEPARATED_HEADER_SPECIFIC_FORMAT(false),
    COMMA_SEPARATED_ABSOLUTE_URI_WITH_PARAMS(true),
    RFC1123_DATE(false),
    UNKNOWN_EXTENSION_HEADER(true);
    
    private final boolean parameterable;
    
    private HeaderType(final boolean parameterable) {
        this.parameterable = parameterable;
    }
    
    public boolean isParameterable() {
        return parameterable;
    }
}
