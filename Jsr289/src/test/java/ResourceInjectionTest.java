
import javax.annotation.Resource;
import javax.servlet.sip.SipFactory;
import org.junit.Assert;
import org.junit.Test;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author andrew
 */
public class ResourceInjectionTest {
    
    @Resource
    private SipFactory sipFactory;
    
    @Test
    public void test() {
        Assert.assertNotNull("sipFactory is null", sipFactory);
    }
}
