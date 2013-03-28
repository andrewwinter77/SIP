package org.andrewwinter.sip;


import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.sip.ConvergedHttpSession;
import javax.servlet.sip.SipFactory;
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

    public WebService() {
        System.out.println("Constructor");
    }
    
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
        
        final StringBuilder sb = new StringBuilder();
        
        if (sipFactoryVariableName == null) {
            sb.append("SipFactory is null. Bad times!!!");
        } else {
            sb.append("SipFactory is non null! ").append(sipFactoryVariableName.toString());
        }
        sb.append("\n\n");
        
        sb.append("Converged HTTP session? ").append(request.getSession() instanceof ConvergedHttpSession);
        sb.append("\n\n");
        sb.append("Request is ").append(request);
        
        
//        
//        return sb.toString();
        
        MainSipServlet ss = new MainSipServlet();
        ss.foo();
        
        return sb.toString();
    }
}
