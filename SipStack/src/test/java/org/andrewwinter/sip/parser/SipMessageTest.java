package org.andrewwinter.sip.parser;

import java.util.List;
import org.andrewwinter.sip.message.SipMessageFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author andrew
 */
public class SipMessageTest {

    private static final String MESSAGE = 
        "INVITE sip:bob@biloxi.com SIP/2.0\r\n"
      + "Via: SIP/2.0/UDP pc33.atlanta.com;branch=z9hG4bK776asdhds\r\n"
      + "Max-Forwards: 70\r\n"
      + "To: Bob <sip:bob@biloxi.com>\r\n"
      + "From: Alice <sip:alice@atlanta.com>;tag=8+g97%gtq.\r\n"
      + "Call-ID: a84b4c76e66710@pc33.atlanta.com\r\n"
      + "CSeq: 314159 INVITE\r\n"
      + "Contact: <sip:alice@pc33.atlanta.com>,<sip:alice@pc34.atlanta.com>\r\n"
      + "Content-Disposition: session ; handling =  optional\r\n"
      + "Content-Type: multipart/signed;\r\n"
      + "  protocol=\"application/pkcs7-signature\"\r\n"
      + "  micalg=sha1; boundary=boundary42\r\n"
      + "Content-Length: 0\r\n"
      + "\r\n";
    
    @Test
    public void testGetParameterable() {
        final SipMessage message = SipMessage.parse(MESSAGE);
        final Parameterable param = message.getParameterable(HeaderName.CONTACT);
        Assert.assertEquals("<sip:alice@pc33.atlanta.com>", param.toString());
        System.out.println("Got " + param);
    }
    
    
    @Test
    public void testVia001() {
        final SipMessage message = SipMessage.parse(MESSAGE);
        message.popVia();
        final List<Via> vias = message.getVias();
        Assert.assertTrue(vias == null || vias.isEmpty());
    }
    
    @Test
    public void testFrom001() {
        final SipMessage message = SipMessage.parse(MESSAGE);
        final Address from = SipMessageHelper.getFrom(message);
        Assert.assertEquals("Tag does not match.", "8+g97%gtq.", from.getTag());
    } 
    
    @Test
    public void testContentDisposition001() {
        final SipMessage message = SipMessage.parse(MESSAGE);
        final Parameterable disp = SipMessageHelper.getContentDisposition(message);
        Assert.assertEquals("session", disp.getValue());
        Assert.assertEquals("optional", disp.getParameter("handling"));
    }
  
    @Test
    public void testAddAddressHeader001() {
        
        final SipMessage message = SipMessage.parse(MESSAGE);
        Uri uri = Uri.parse("http://wwww.example.com/alice/photo.jpg");
        Address callInfo = new Address(uri);
        callInfo.setParameter("purpose", "icon");
        
//      resp.addAddressHeader("Call-Info", callInfo, false);
    }
}
