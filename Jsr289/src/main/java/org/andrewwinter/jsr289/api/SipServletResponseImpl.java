package org.andrewwinter.jsr289.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.sip.Proxy;
import javax.servlet.sip.ProxyBranch;
import javax.servlet.sip.Rel100Exception;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.message.InboundSipResponse;
import org.andrewwinter.sip.message.SipMessageFactory;
import org.andrewwinter.sip.parser.AuthenticationChallenge;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;

/**
 *
 * @author andrew
 */
public class SipServletResponseImpl extends SipServletMessageImpl implements SipServletResponse {

    private final SipResponse response;
    
    private SipServletRequestImpl servletRequest;
    
    /**
     * Null if we are the UAS.
     */
    private final InboundSipResponse inboundSipResponse;
    
    /**
     * UAS constructor.
     * @param request Request created by us earlier.
     * @param response  SipStack response object, to be wrapped by this object.
     */
    public SipServletResponseImpl(final SipServletRequestImpl request, final SipResponse response) {
        super(request.getInboundSipRequest(), response);
        this.response = response;
        this.servletRequest = request;
        this.inboundSipResponse = null;
        setSipSession((SipSessionImpl) request.getSession());
    }

    /**
     * UAC constructor.
     * @param isr Response received from network.
     */
    public SipServletResponseImpl(final InboundSipResponse isr) {
        super(isr);
        this.response = isr.getResponse();

        this.servletRequest = new SipServletRequestImpl(isr); // TODO: Do we want to pass null or create a new SipServletRequestImpl constructor for this?
        final SipSession session = getSession(false);
        servletRequest.setSipSession((SipSessionImpl) session);
        
        this.inboundSipResponse = isr;
    }
    
    @Override
    public SipServletRequest getRequest() {
        return servletRequest;
    }

    @Override
    public int getStatus() {
        return response.getStatusCode();
    }

    @Override
    public void setStatus(final int statusCode) {
        setStatus(statusCode, response.getReasonPhrase());
    }

    @Override
    public void setStatus(final int statusCode, final String reasonPhrase) {
        // TODO: Do we want to allow the app to set the response code 100?
        if (statusCode < 100 || statusCode > 699) {
            throw new IllegalArgumentException("Status code not allowed.");
        }
        
        response.setStatus(statusCode, reasonPhrase);
    }

    @Override
    public String getReasonPhrase() {
        return response.getReasonPhrase();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        // Always returns null.
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        // Always returns null.
        return null;
    }

    @Override
    public Proxy getProxy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ProxyBranch getProxyBranch() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendReliably() throws Rel100Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SipServletRequest createAck() {
        
        if (inboundSipResponse == null) {
            throw new IllegalStateException("Cannot create ACK for outgoing response.");
        }
        
        if (!servletRequest.getSipRequest().isINVITE()) {
            throw new IllegalStateException("Original request was not an INVITE.");
        }
        
        if (response.getStatusCode() < 200) {
            throw new IllegalStateException("Cannot create ACK for provisional response.");
        }
        
        // TODO: IllegalStateException if ACK has already been generated.
        // TODO: IllegalStateException if transaction state doesn't allow ACK to be sent now.
        
        final SipRequest ack = SipMessageFactory.createAck(inboundSipResponse);
        final SipServletRequestImpl sipServletAck = new SipServletRequestImpl(ack);
        sipServletAck.setSipSession((SipSessionImpl) getSession());
        return sipServletAck;
    }

    @Override
    public SipServletRequest createPrack() throws Rel100Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<String> getChallengeRealms() {
        final List<String> result = new ArrayList<>();
        List<AuthenticationChallenge> challenges = response.getWWWAuthenticate();
        if (challenges != null) {
            for (final AuthenticationChallenge challenge : challenges) {
                final String realm = challenge.getParam("realm");
                if (realm != null) {
                    result.add(realm);
                }
            }
        }
        challenges = response.getProxyAuthenticate();
        if (challenges != null) {
            for (final AuthenticationChallenge challenge : challenges) {
                final String realm = challenge.getParam("realm");
                if (realm != null) {
                    result.add(realm);
                }
            }
        }
        return result.iterator();
    }

    @Override
    public boolean isBranchResponse() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setBufferSize(final int size) {
        // Therefore, it is not expected that these methods will yield any
        // useful result and implementations may simply do nothing.
    }

    @Override
    public int getBufferSize() {
        // It is recommended that getBufferSize return 0.
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {
        // Therefore, it is not expected that these methods will yield any
        // useful result and implementations may simply do nothing.
    }

    @Override
    public void resetBuffer() {
        // Therefore, it is not expected that these methods will yield any
        // useful result and implementations may simply do nothing.
    }

    @Override
    public void reset() {
        // Therefore, it is not expected that these methods will yield any
        // useful result and implementations may simply do nothing.
    }

    @Override
    public void setLocale(Locale locale) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Locale getLocale() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String toString() {
        return response.toString();
    }

    @Override
    public void send() throws IOException {
        
        if (inboundSipResponse != null) {
            throw new IllegalStateException("Response was received from downstream.");
        }
        
        synchronized (super.sendLock) {
            flagMessageAsSent();
            
            servletRequest.getInboundSipRequest().sendResponse(response);
            final Dialog dialog = servletRequest.getInboundSipRequest().getServerTransaction().getDialog();
            if (dialog != null) {
                SipSessionImpl session = (SipSessionImpl) getSession();
                if (session == null) {
                    // TODO: Perhaps create a session if one doesn't already exist?
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ null session when sending response to " + servletRequest.getSipRequest().getMethod());
                } else {
                    session.setDialog(dialog);
                }
            }
        }
    }
}
