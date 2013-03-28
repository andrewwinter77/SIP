
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.sip.Address;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletRequest;
import org.andrewwinter.servlet.ServletContextImpl;
import org.junit.Test;
import org.andrewwinter.jsr289.SipFactoryImpl;

/**
 *
 * @author andrewwinter77
 */
public class DummyAppTest {

    @Test
    public void test() throws ServletParseException, IOException {
        final ServletContext context = new ServletContextImpl();
        
        SipFactory sipFactory = (SipFactory) context.getAttribute("javax.servlet.sip.SipFactory");
        
        final Address to = sipFactory.createAddress("sip:foo@192.168.1.1");
        final Address from = sipFactory.createAddress("sip:192.168.1.2");
        
        final SipApplicationSession appSession = sipFactory.createApplicationSession();
        
        final SipServletRequest request = sipFactory.createRequest(appSession, "INVITE", from, to);
        System.out.println(request);
        
        request.send();
    }
}
