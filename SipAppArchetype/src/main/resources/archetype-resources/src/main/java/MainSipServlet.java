
import javax.annotation.Resource;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipSessionsUtil;
import javax.servlet.sip.TimerService;


@javax.servlet.sip.annotation.SipServlet
public class MainSipServlet extends SipServlet {
    
    @Resource
    private SipFactory sipFactory;
    
    @Resource
    private SipSessionsUtil sipSessionsUtil;

    @Resource
    private TimerService timerService;
}

