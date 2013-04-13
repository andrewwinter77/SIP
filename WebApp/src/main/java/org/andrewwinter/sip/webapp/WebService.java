package org.andrewwinter.sip.webapp;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

/**
 * 
 */
@ApplicationPath("/service")
@Path("/")
public class WebService extends Application {

    @EJB
    private PbxManager pbxMgr;

    /**
     * Replace this method with something of your own.
     * @param param
     */
    @POST
    @Path("/createpbx")
    public String createPbx(
            @FormParam("domain") final String domain,
            @FormParam("email") final String email,
            @FormParam("password") final String password,
            @Context ServletContext servletContext,
            @Context HttpServletRequest request) {
        
        pbxMgr.createPbx(domain, email, password);
        
        return "Finished create PBX";
    }
}
