package org.andrewwinter.sip.webapp;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.andrewwinter.sip.model.User;

/**
 * 
 */
@ApplicationPath("/service")
@Path("/")
public class WebService extends Application {

    @EJB
    private DataManager dataMgr;

    /**
     * Replace this method with something of your own.
     * @param param
     */
    @POST
    @Path("/createpbx")
    public String createPbx(
            @FormParam("domain") final String domain,
            @FormParam("email") final String email,
            @FormParam("forename") final String forename,
            @FormParam("surname") final String surname,
            @FormParam("password") final String password,
            @Context ServletContext servletContext,
            @Context HttpServletRequest request) {
        
        dataMgr.createPbx(domain, email, forename, surname, password);
        
        return "Finished create PBX";
    }
    
    @POST
    @Path("/login")
    @Produces("application/json")
    public Response login(
            @FormParam("email") final String email,
            @FormParam("password") final String password,
            @Context HttpServletRequest request) {
        
        final User user;
        if (request.isRequestedSessionIdValid()) {
            user = (User) request.getSession().getAttribute("user");
        } else {
            user = dataMgr.getUserByEmail(email);
            if (user == null || !user.getPassword().equals(password)) {
                return Response.status(Response.Status.FORBIDDEN).build();
            } else {
                final HttpSession session = request.getSession();
                session.setAttribute("user", user);
            }
        }
        return Response.ok(user).build();
    }
    
    @POST
    @Path("/logout")
    public Response logout(@Context HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return Response.ok().build();
    }
}
