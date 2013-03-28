
import javax.annotation.Resource;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipSessionsUtil;
import javax.servlet.sip.TimerService;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Application;

/**
 * 
 */
@ApplicationPath("/foo/")
@Path("/")
public class WebService extends Application {

    @Resource
    private SipFactory sipFactory;

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
    public void doSomething(@PathParam("someParam") final String param) {
        
    }
}
