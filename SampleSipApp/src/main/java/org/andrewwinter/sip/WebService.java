package org.andrewwinter.sip;


import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipSessionsUtil;
import javax.servlet.sip.TimerService;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

/**
 * 
 */
@ApplicationPath("/foo/")
@Path("/")
public class WebService extends Application {

    @Resource
    private SipFactory sipFactoryVariableName;

    @Resource
    private SipSessionsUtil sipSessionsUtil;

    @Resource
    private TimerService timerService;

    /**
     * Replace this method with something of your own.
     * @param param
     */
    @GET
    @Path("/bar/{someParam}")
    public String doSomething(
            @PathParam("someParam") final String param,
            @Context ServletContext servletContext,
            @Context HttpServletRequest request) {
        
        if (sipFactoryVariableName == null) {
            return "sipFactory is null.";
        }
        
        final SipApplicationSession sas = sipFactoryVariableName.createApplicationSession();

        final SipServletRequest invite;
        try {
            invite = sipFactoryVariableName.createRequest(
                    sas,
                    "INVITE",
                    "sip:from@someone.com",
                    "sip:to@someone.com");
        } catch (ServletParseException e) {
            return "ServletParseException!";
        }
        
        try {
            invite.send();
        } catch (IOException e) {
            return "IOException sending INVITE";
        }
         
        return "INVITE sent successfully.";
    }
}
