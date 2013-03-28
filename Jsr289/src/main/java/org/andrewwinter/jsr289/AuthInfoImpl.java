package org.andrewwinter.jsr289;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.sip.AuthInfo;

/**
 *
 * @author andrew
 */
public class AuthInfoImpl implements AuthInfo {

    private final List<SingleAuthInfo> list;
    
    AuthInfoImpl() {
        list = new ArrayList<>();
    }
    
    @Override
    public void addAuthInfo(
            final int statusCode,
            final String realm,
            final String username,
            final String password) {
        
        list.add(new SingleAuthInfo(statusCode, realm, username, password));
    }
    
    /**
     * Returns an immutable list of SingleAuthInfos.
     * @return 
     */
    public List<SingleAuthInfo> getAuthInfos() {
        return Collections.unmodifiableList(list);
    }
}
