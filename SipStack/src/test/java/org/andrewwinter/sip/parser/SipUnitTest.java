/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.sip.parser;

import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author andrewwinter77
 */
public class SipUnitTest {

    private static final String CRLF = "\r\n";
    private static final String LF = "\n";
    
    private static final String OFFER2_1_1 =
        "v=0" + LF + 
        "o=alice 2890844526 2890844526 IN IP4 host.atlanta.example.com" + CRLF +
        "s=" + CRLF +
        "c=IN IP4 host.atlanta.example.com"  + LF +
        "t=0 0"  + CRLF +
        "r=7d 1h 0 25h" + LF +
        "b=CT:123" + CRLF +
        "b=UNKNOWN:123" + CRLF +
        "m=audio 49170 RTP/AVP 0 8 97"  + CRLF +
        "a=rtpmap:0 PCMU/8000"  + CRLF +
        "a=rtpmap:8 PCMA/8000"  + CRLF +
        "a=rtpmap:97 iLBC/8000"  + CRLF +
        "m=video 51372 RTP/AVP 31 32"  + CRLF +
        "a=rtpmap:31 H261/90000"  + CRLF +
        "a=rtpmap:32 MPV/90000";
    
    private static final String SDP5 =
      "v=0" + CRLF +
      "o=jdoe 2890844526 2890842807 IN IP4 10.47.16.5" + CRLF +
      "s=SDP Seminar" + CRLF +
      "i=A Seminar on the session description protocol" + CRLF +
      "u=http://www.example.com/seminars/sdp.pdf" + CRLF +
      "e=j.doe@example.com (Jane Doe)" + CRLF +
      "c=IN IP4 224.2.17.12/127" + CRLF +
      "t=2873397496 2873404696" + CRLF +
      "a=recvonly" + CRLF +
      "m=audio 49170 RTP/AVP 0" + CRLF +
      "m=video 51372 RTP/AVP 99" + CRLF +
      "a=rtpmap:99 h263-1998/90000";
    
    private static final String SIP1 = 
      "INVITE       sip:vivekg@chair-dnrc.example.com;unknownparam SIP/2.0" + CRLF +
      "TO :" + CRLF +
      " sip:vivekg@chair-dnrc.example.com ;   tag    = 1918181833n" + CRLF +
      "from   : \"J Rosenberg \"       <sip:jdrosen@example.com>" + CRLF +
      "  ;" + CRLF +
      "  tag = 98asjd8" + CRLF +
      "MaX-fOrWaRdS: 0068" + CRLF +
      "Call-ID: wsinv.ndaksdj@192.0.2.1" + CRLF +
      "Content-Length   : 150" + CRLF +
      "cseq: 0009" + CRLF +
      "  INVITE" + CRLF +
      "Accept: application/sdp;level=1, application/x-private;description=\"comma, and, space, delimited\"" + CRLF +
      "Accept: text/html" + CRLF +
      "reQuirE: abc    ,def" + CRLF +
      "Via  : SIP  /   2.0" + CRLF +
      " /UDP" + CRLF +
      "    192.0.2.2;branch=390skdjuw" + CRLF +
      "s :" + CRLF +
      "NewFangledHeader:   newfangled value" + CRLF +
      " continued newfangled value" + CRLF +
      "require: ghi" + CRLF +
      "UnknownHeaderWithUnusualValue: ;;,,;;,;" + CRLF +
      "Content-Type: text/html   ; charset=ISO-8859-4" + CRLF +
      "Route:" + CRLF +
      " <sip:services.example.com;lr;unknownwith=value;unknown-no-value>" + CRLF +
      "v:  SIP  / 2.0  / TCP     spindle.example.com   ;" + CRLF +
      "  branch  =   z9hG4bK9ikj8  ," + CRLF +
      " SIP  /    2.0   / UDP  192.168.255.111   ; branch=" + CRLF +
      " z9hG4bK30239" + CRLF +
      "m:\"Quoted string \\\"\\\"\" <sip:jdrosen@example.com> ; newparam =" + CRLF +
      "      newvalue ;" + CRLF +
      "  secondparam ; q = 0.33" + CRLF +
      "" + CRLF +
      "v=0" + CRLF +
      "o=mhandley 29739 7272939 IN IP4 192.0.2.3" + CRLF +
      "s=-" + CRLF +
      "c=IN IP4 192.0.2.4" + CRLF +
      "t=0 0" + CRLF +
      "m=audio 49217 RTP/AVP 0 12" + CRLF +
      "m=video 3227 RTP/AVP 31" + CRLF +
      "a=rtpmap:31 LPC";

    
    private static final String SIP2 =
      "!interesting-Method0123456789_*+`.%indeed'~ sip:1_unusual.URI~(to-be!sure)&isn't+it$/crazy?,/;;*:&it+has=1,weird!*pas$wo~d_too.(doesn't-it)@example.com SIP/2.0" + CRLF +
      "Via: SIP/2.0/TCP host1.example.com;branch=z9hG4bK-.!%66*_+`'~" + CRLF +
      "To: \"BEL:\\u0007 NUL:\\u0000 DEL:\\u007F\" <sip:1_unusual.URI~(to-be!sure)&isn't+it$/crazy?,/;;*@example.com>" + CRLF +
      "From: token1~` token2'+_ token3*%!.- <sip:mundane@example.com>;fromParam''~+*_!.-%=\"\u00D1\u0080\u00D0\u00B0\u00D0\u00B1\u00D0\u00BE\u00D1\u0082\u00D0\u00B0\u00D1\u008E\u00D1\u0089\u00D0\u00B8\u00D0\u00B9\";tag=_token~1'+`*%!-." + CRLF +
      "Call-ID: intmeth.word%ZK-!.*_+'@word`~)(><:\\/\"][?}{" + CRLF +
      "CSeq: 139122385 !interesting-Method0123456789_*+`.%indeed'~" + CRLF +
      "Max-Forwards: 255" + CRLF +
      "extensionHeader-!.%*+_`'~:\u00EF\u00BB\u00BF\u00E5\u00A4\u00A7\u00E5\u0081\u009C\u00E9\u009B\u00BB" + CRLF +
      "Content-Length: 0" + CRLF +
      CRLF;

            
    private static final String NOREASON =
      "SIP/2.0 100\u0020" + CRLF +
      "Via: SIP/2.0/UDP 192.0.2.105;branch=z9hG4bK2398ndaoe" + CRLF +
      "Call-ID: noreason.asndj203insdf99223ndf" + CRLF +
      "CSeq: 35 INVITE" + CRLF +
      "From: <sip:user@example.com>;tag=39ansfi3" + CRLF +
      "To: <sip:user@example.edu>;tag=902jndnke3" + CRLF +
      "Content-Length: 0" + CRLF +
      "Contact: <sip:user@host105.example.com>" + CRLF +
      CRLF;
      
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
    public void testSipParser() {
        final SipMessage message = SipMessage.parse(SIP1);
        Assert.assertTrue(message instanceof SipRequest);
        
        final SipRequest request = (SipRequest) message;
        Assert.assertEquals(request.getMethod(), "INVITE");
        
        final Uri requestUri = request.getRequestUri();
        Assert.assertEquals("sip", requestUri.getScheme());
        Assert.assertTrue(requestUri.isSipUri());
        
        Address to = SipMessageHelper.getTo(request);
        Assert.assertEquals("1918181833n", to.getTag());

        Address from = SipMessageHelper.getFrom(request);
        Assert.assertEquals("98asjd8", from.getTag());
        
        String newFangledHeader = (String) request.getFirstOccurrenceOfHeader(HeaderName.fromString("newfangledheader"));
        Assert.assertEquals("newfangled value continued newfangled value", newFangledHeader);
        
        
        
//        List<String> require = request.getRequire();
//        for (final String r : require) {
//            System.out.println(r);
//        }
//        
//        System.out.println("[" + request.getContentType().getParameter("charset") + "]");
//        
//        
//        for (final StringWithParams swp : request.getAccept()) {
//            System.out.println(swp);
//        }
        
        List<Via> vias = request.getVias();
        for (final Via via : vias) {
            System.out.println(via);
        }
        
//        System.out.println(request.getCSeq());
  
        for (final Address address : SipMessageHelper.getContact(request)) {
            System.out.println("This one: " + address);
        }
        
//        Assert.assertNull(requireHeader);
        
        
//        System.out.println("\n==================================");
//        final SipResponse response = SipMessageFactory.createResponse(request, ResponseType.OK);
//        System.out.println(response);
//        System.out.println("==================================");
    } 
}
