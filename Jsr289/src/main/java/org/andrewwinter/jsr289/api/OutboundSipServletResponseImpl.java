package org.andrewwinter.jsr289.api;

import java.io.IOException;
import javax.servlet.sip.Proxy;
import javax.servlet.sip.ProxyBranch;
import javax.servlet.sip.Rel100Exception;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.parser.SipMessageHelper;
import org.andrewwinter.sip.parser.SipResponse;

/**
 *
 * @author andrew   
 */
public class OutboundSipServletResponseImpl extends SipServletResponseImpl implements SipServletResponse {

    private final SipResponse response;
    
    private InboundSipServletRequestImpl servletRequest;
    
    /**
     * UAS constructor.
     * @param request Request created by us earlier.
     * @param response  SipStack response object, to be wrapped by this object.
     */
    public OutboundSipServletResponseImpl(final InboundSipServletRequestImpl request, final SipResponse response) {
        super(response, request);
        this.response = response;
        this.servletRequest = request;
        setSipSession((SipSessionImpl) request.getSession());
    }

    @Override
    public Proxy getProxy() {
        return null;
    }

    @Override
    public ProxyBranch getProxyBranch() {
        return null;
    }

    @Override
    public SipServletRequest createAck() {
        throw new IllegalStateException("Cannot create ACK for outgoing response.");
    }

    @Override
    public SipServletRequest createPrack() throws Rel100Exception {
        throw new IllegalStateException("Cannot create PRACK for outgoing response.");
    }

    @Override
    public boolean isBranchResponse() {
        return false;
    }

    @Override
    public String toString() {
        return response.toString();
    }

    @Override
    public void sendReliably() throws Rel100Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void send() throws IOException {
        
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

    @Override
    public void setContentLength(final int len) {
        if (isSent()) {
            throw new IllegalStateException("Message has already been sent.");
        }
        SipMessageHelper.setContentLength(len, response);
    }

    @Override
    public String getInitialRemoteAddr() {
        return null;
    }

    @Override
    public int getInitialRemotePort() {
        return -1;
    }
    
    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return -1;
    }

    @Override
    public String getTransport() {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public int getRemotePort() {
        return -1;
    }

    @Override
    public String getInitialTransport() {
        return null;
    }

    @Override
    protected Dialog getDialog() {
        return null;
    }
}
