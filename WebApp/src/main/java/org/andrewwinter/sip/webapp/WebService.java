package org.andrewwinter.sip.webapp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.sip.Address;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletRequest;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.andrewwinter.sip.model.Subscriber;

/**
 * 
 */
@ApplicationPath("/service")
@Path("/")
public class WebService extends Application {

    @EJB
    private DataManager dataMgr;

    @Resource
    private SipFactory sf;

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
    @Path("/user/create")
    public Response createUser(
            @FormParam("forename") final String forename,
            @FormParam("surname") final String surname,
            @FormParam("email") final String email,
            @FormParam("password") final String password,
            @FormParam("admin") final boolean admin,
            @Context HttpServletRequest request) {
        
        final Subscriber user;
        if (request.isRequestedSessionIdValid()) {
            user = (Subscriber) request.getSession().getAttribute("user");
            if (user.isAdminUser()) {

                Subscriber newUser = dataMgr.getUserByEmail(email);
                if (newUser == null) {

                    dataMgr.createSubscriber(
                                    user.getPbx(),
                                    forename,
                                    surname,
                                    email,
                                    password,
                                    admin);
                    
                    return Response.status(Response.Status.CREATED).build();
                } else {
                    return Response.status(Response.Status.CONFLICT).build();
                }
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    
    @POST
    @Path("/login")
    @Produces("application/json")
    public Response login(
            @FormParam("email") final String email,
            @FormParam("password") final String password,
            @Context HttpServletRequest request) {
        
        final Subscriber user;
        if (request.isRequestedSessionIdValid()) {
            user = (Subscriber) request.getSession().getAttribute("user");
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
    
    @GET
    @Path("/extensions")
    public Response getExtensions(@Context HttpServletRequest request) {
        final Subscriber user;
        if (request.isRequestedSessionIdValid()) {
            user = (Subscriber) request.getSession().getAttribute("user");
            return Response.ok(dataMgr.findExtensionsInUse(user.getPbx())).build();
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }
    
    /**
     * Replace this method with something of your own.
     * @param param
     */
    @POST
    @Path("/c2c/{callee}")
    public Response call(
            @PathParam("callee") final String to,
            @Context ServletContext servletContext,
            @Context HttpServletRequest request) {
        
        final Subscriber user;
        if (request.isRequestedSessionIdValid()) {
            user = (Subscriber) request.getSession().getAttribute("user");
        } else {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        final String from = "sip:andrew@sip.sipseer.com";
        
        final SipApplicationSession appSession = sf.createApplicationSession();

        final SipServletRequest inviteToSubscriber;
        try {
            final Address subscriberAddress = sf.createAddress(to);
            final Address calleeAddress = sf.createAddress(from);
        
            inviteToSubscriber = sf.createRequest(
                                        appSession,
                                        "INVITE",
                                        calleeAddress,
                                        subscriberAddress);
            
            inviteToSubscriber.setContent(Util.makeOfferSdp().toString().getBytes(), "application/sdp");
            
            inviteToSubscriber.getSession().setAttribute(Util.SUBSCRIBER_ADDRESS_KEY, subscriberAddress);
            inviteToSubscriber.getSession().setAttribute(Util.CALLEE_ADDRESS_KEY, calleeAddress);
            inviteToSubscriber.getSession().setAttribute(Util.IS_SUBSCRIBER_KEY, "");
      
        } catch (ServletParseException e) {
            // "There was something wrong with the addresses."
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (UnsupportedEncodingException e) {
            // "There was something wrong with the content type."
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        try {
            inviteToSubscriber.send();
        } catch (IOException e) {
            // "There was an error placing the call."
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        // TODO: Return an ID that can be used to cancel/hang up the call
        return Response.ok().build();
    }
}
