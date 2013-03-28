package org.andrewwinter.sip.parser;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author andrewwinter77
 */
public class UriTest {

//    @Test
//    public void testParser() {
//        System.out.println("======1a========");
//        System.out.println(SessionDescriptionBuilder.parse(OFFER2_1_1));
//        System.out.println("======1b========");
//        System.out.println(SessionDescriptionBuilder.parse(SDP5));
//    }
//    
//    @Test
//    public void testBuilder() {
//        System.out.println("======2========");
//        SessionDescriptionBuilder sdb = new SessionDescriptionBuilder();
//        
//        OriginBuilder ob = new OriginBuilder()
//                .addrType(AddressType.IP4)
//                .unicastAddr("host.atlanta.example.com");
//        sdb = sdb.origin(ob.build());
//        
//        ConnectionDataBuilder cdb = new ConnectionDataBuilder()
//                .addrType(AddressType.IP4)
//                .address("host.atlanta.example.com");
//        sdb = sdb.connectionData(cdb.build());
//        
//        SessionDescription sd = sdb.build();
//        System.out.println(sd);
//    }
    
//    @Test
//    public void testPrintMessages() {
//        System.out.println(SipMessage.parse(SIP1).toString());
//        System.out.println("----------------");
//        System.out.println(SipMessage.parse(SIP2).toString());
//        System.out.println("----------------");
//        System.out.println(SipMessage.parse(NOREASON).toString());
//    }
    
//    @Test
//    public void testUri() {
//        final Uri uri = new Uri("sip:sips%3Auser%40example.com@example.net");
//    }
    
    /**
     *
     */
    @Test
    public void testUri2() throws ParseException {
        final Uri uri = Uri.parse("sip:abc;def?hij&klm=nop@[1:2:3:4:5:6:7:8];tuv:wxy&=abc;zab;cde??=unknown&fgh?ijk?lmn=foo&opq=yes");
        Assert.assertTrue(uri.isSipUri());
        final SipUri sipUri = (SipUri) uri;
        Assert.assertEquals(sipUri.getUser(), "abc;def?hij&klm=nop");
        Assert.assertEquals(sipUri.getPassword(), null);
        Assert.assertEquals(sipUri.getHost(), "1:2:3:4:5:6:7:8");
        Assert.assertEquals(sipUri.getPort(), -1);

        Assert.assertEquals("abc", sipUri.getParameter("tuv:wxy&"));
        Assert.assertEquals("", sipUri.getParameter("zab"));
        Assert.assertNull(sipUri.getParameter("bogusparameter"));
        
        Assert.assertEquals("foo", sipUri.getHeader("fgh?ijk?lmn"));
        Assert.assertEquals("yes", sipUri.getHeader("opq"));
        Assert.assertNull(sipUri.getHeader("bogusheader"));
        
        
//        Assert.assertEquals();
    }

    /**
     *
     */
    @Test
    public void testUri3() throws ParseException {
        final Uri uri = Uri.parse("sip:abc;def?hij&klm=nop@[1:2:3:4:5:6:7:8];tuv:wxy&=abc;zab;cde");
        Assert.assertTrue(uri.isSipUri());
        final SipUri sipUri = (SipUri) uri;
        Assert.assertEquals(sipUri.getUser(), "abc;def?hij&klm=nop");
        Assert.assertEquals(sipUri.getPassword(), null);
        Assert.assertEquals(sipUri.getHost(), "1:2:3:4:5:6:7:8");
        Assert.assertEquals(sipUri.getPort(), -1);

        Assert.assertNull(sipUri.getHeader("bogusheader"));
        
//        Assert.assertEquals();
    }
//    
    /**
     *
     */
    @Test
    public void testUri4() throws ParseException {
        final Uri uri = Uri.parse("sip:qrs.com:500;tuv:wxy&zab;cde??=unknown&fgh?ijk?lmn=foo&opq=yes");
        Assert.assertTrue(uri.isSipUri());
        final SipUri sipUri = (SipUri) uri;
        Assert.assertEquals(sipUri.getUser(), null);
        Assert.assertEquals(sipUri.getPassword(), null);
        Assert.assertEquals(sipUri.getHost(), "qrs.com");
        Assert.assertEquals(sipUri.getPort(), 500);
//        Assert.assertEquals();
    }
//
//    @Test
//    public void testUri5() {
//        final Uri uri = Uri.parse("sip:abc;def?hij&klm=nop@qrs.com;tuv:wxy&zab;cde??fgh?ijk?lmn&opq");
//        Assert.assertTrue(uri.isSipUri());
//        final SipUri sipUri = (SipUri) uri;
//        Assert.assertEquals(sipUri.getUser(), "abc;def?hij&klm=nop");
//        Assert.assertEquals(sipUri.getPassword(), null);
//        Assert.assertEquals(sipUri.getHost(), "qrs.com");
//        Assert.assertEquals(sipUri.getPort(), -1);
////        Assert.assertEquals();
//    }

    //    @Test
//    public void testSipParser() {
//        final SipMessage message = SipMessage.parse(SIP1);
//        Assert.assertTrue(message instanceof SipRequest);
//        
//        final SipRequest request = (SipRequest) message;
//        Assert.assertEquals(request.getMethod(), "INVITE");
//        
//        final String requestUriAsString = request.getRequestUri();
//        System.out.println(requestUriAsString);
//        Uri requestUri = new Uri(requestUriAsString);
//        Assert.assertEquals("sip", requestUri.getScheme());
//        Assert.assertTrue(requestUri.isSipUri());
//    } 
}
