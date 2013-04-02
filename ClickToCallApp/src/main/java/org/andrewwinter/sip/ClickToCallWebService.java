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
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;

/**
 * 
 */
@ApplicationPath("/click2call/")
@Path("/")
public class ClickToCallWebService extends Application {

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
    @POST
    @Path("/call")
    public String call(
            @FormParam("from") final String from,
            @FormParam("to") final String to,
            @Context ServletContext servletContext,
            @Context HttpServletRequest request) {
        
        final SipApplicationSession appSession = sipFactoryVariableName.createApplicationSession();

        final SipServletRequest invite;
        try {
            invite = sipFactoryVariableName.createRequest(
                    appSession,
                    "INVITE",
                    from,
                    to);
        } catch (ServletParseException e) {
            return "There was something wrong with the addresses.";
        }
        
        try {
            invite.send();
        } catch (IOException e) {
            return "There was an error placing the call.";
        }
         
        return "Call in progress.";
    }
}
