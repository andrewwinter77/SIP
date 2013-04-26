package org.andrewwinter.jsr289.api;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.sip.Proxy;
import javax.servlet.sip.ProxyBranch;
import javax.servlet.sip.Rel100Exception;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.message.InboundSipResponse;
import org.andrewwinter.sip.message.SipMessageFactory;
import org.andrewwinter.sip.parser.SipRequest;

/**
 *
 * @author andrew
 */
public class InboundSipServletResponseImpl extends SipServletResponseImpl implements SipServletResponse {

    private final InboundSipResponse inboundSipResponse;
    
    /**
     * @param isr Response received from network.
     */
    public InboundSipServletResponseImpl(final InboundSipResponse isr, final OutboundSipServletRequestImpl request) {
        super(isr.getResponse(), request);
        final SipSession session = getSession(false);
        servletRequest.setSipSession(session);
        this.inboundSipResponse = isr;
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
    public SipServletRequest createAck() {
        
        if (!servletRequest.getSipRequest().isINVITE()) {
            throw new IllegalStateException("Original request was not an INVITE.");
        }
        
        if (response.getStatusCode() < 200) {
            throw new IllegalStateException("Cannot create ACK for provisional response.");
        }
        
        // TODO: IllegalStateException if ACK has already been generated.
        // TODO: IllegalStateException if transaction state doesn't allow ACK to be sent now.
        
        final SipRequest ack = SipMessageFactory.createAck(inboundSipResponse);
        final OutboundSipServletRequestImpl sipServletAck = new OutboundSipServletRequestImpl(ack, null);
        sipServletAck.setServletContext(getServletContext());
        sipServletAck.setSipSession(getSession());
        return sipServletAck;
    }

    @Override
    public SipServletRequest createPrack() throws Rel100Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isBranchResponse() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return response.toString();
    }

    @Override
    public void send() throws IOException {
        throw new IllegalStateException("Cannot send inbound responses.");
    }

    @Override
    public void sendReliably() throws Rel100Exception {
        throw new IllegalStateException("Cannot send inbound responses.");
    }

    @Override
    public void setContentLength(final int len) {
        throw new IllegalStateException("Cannot set content on incoming messages.");
    }

    @Override
    public String getInitialRemoteAddr() {
        return inboundSipResponse.getInitialRemoteAddr();
    }

    @Override
    public int getInitialRemotePort() {
        return inboundSipResponse.getInitialRemotePort();
    }
    
    @Override
    public Dialog getDialog() {
        return inboundSipResponse.getDialog();
    }

    @Override
    public String getLocalAddr() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getLocalPort() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTransport() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRemoteAddr() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getRemotePort() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getInitialTransport() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
