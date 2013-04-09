package org.andrewwinter.jsr289.auth;

/**
 *
 * @author andrew
 */
public class SingleAuthInfo {

    private final int statusCode;
    private final String realm;
    private final String username;
    private final String password;

    public SingleAuthInfo(
            final int statusCode,
            final String realm,
            final String username,
            final String password) {
        this.statusCode = statusCode;
        this.realm = realm;
        this.username = username;
        this.password = password;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public String getRealm() {
        return realm;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
}
