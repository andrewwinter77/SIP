/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.sip.gui;

import java.awt.Frame;
import org.andrewwinter.sip.SipRequestHandler;
import org.andrewwinter.sip.SipResponseHandler;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.element.UserAgentClient;
import org.andrewwinter.sip.message.InboundSipRequest;
import org.andrewwinter.sip.message.InboundSipResponse;
import org.andrewwinter.sip.message.ResponseType;
import org.andrewwinter.sip.message.SipMessageFactory;
import org.andrewwinter.sip.parser.Address;
import org.andrewwinter.sip.parser.HeaderName;
import org.andrewwinter.sip.parser.Parameterable;
import org.andrewwinter.sip.parser.SipMessageHelper;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.properties.UserAgentProperties;
import org.andrewwinter.sip.sdp.SessionDescription;
import org.andrewwinter.sip.sdp.SessionDescriptionBuilder;

/**
 *
 * @author andrew
 */
public class CallWindow extends javax.swing.JDialog implements SipRequestHandler, SipResponseHandler {

    private final UserAgentProperties properties;
    
    private Dialog dialog;
    
    private UserAgentClient uac;
    
    /**
     * Creates new form InProgressCallDialog
     * @param properties 
     */
    public CallWindow(final UserAgentProperties properties, final Address otherParty) {
        super((Frame) null, false);
        initComponents();
        this.properties = properties;
        otherPartyAddressLabel.setText("In call with " + otherParty.toString());
    }
    
    void setUserAgentClient(final UserAgentClient uac) {
        this.uac = uac;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        optionsBtn = new javax.swing.JButton();
        otherPartyAddressLabel = new javax.swing.JLabel();
        byeButton = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        textArea.setColumns(20);
        textArea.setRows(5);
        jScrollPane2.setViewportView(textArea);

        optionsBtn.setText("OPTIONS");
        optionsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsBtnActionPerformed(evt);
            }
        });

        otherPartyAddressLabel.setText("other party's address here");

        byeButton.setText("BYE");
        byeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                byeButtonActionPerformed(evt);
            }
        });

        cancelBtn.setText("CANCEL");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(otherPartyAddressLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(0, 55, Short.MAX_VALUE)
                        .add(cancelBtn)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(optionsBtn)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(byeButton)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(otherPartyAddressLabel)
                .add(47, 47, 47)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(optionsBtn)
                    .add(byeButton)
                    .add(cancelBtn))
                .addContainerGap(248, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane2)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void optionsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsBtnActionPerformed

        if (dialog == null) {
            System.out.println("Dialog is null");
        } else {
            final SipRequest options = SipMessageFactory.createInDialogRequest(dialog, "OPTIONS", null, null);
            
            // An Accept header field SHOULD be included to indicate the type of
            // message body the UAC wishes to receive in the response.
            // Typically, this is set to a format that is used to describe the
            // media capabilities of a UA, such as SDP (application/sdp).
            
            options.addHeader(HeaderName.ACCEPT, "application/sdp");

            UserAgentClient.createUacAndSendRequest(this, options, null);
            Util.logMessage(options, false, textArea);
        }
    }//GEN-LAST:event_optionsBtnActionPerformed

    private void byeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_byeButtonActionPerformed
        if (dialog == null) {
            System.out.println("Dialog is null");
        } else {
            final SipRequest bye = SipMessageFactory.createInDialogRequest(dialog, "BYE", null, null);
            
            UserAgentClient.createUacAndSendRequest(this, bye, null);
            Util.logMessage(bye, false, textArea);
            setVisible(false);
        }
    }//GEN-LAST:event_byeButtonActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        if (uac != null) {
            uac.cancel(null);
        }
    }//GEN-LAST:event_cancelBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton byeButton;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JButton optionsBtn;
    private javax.swing.JLabel otherPartyAddressLabel;
    private javax.swing.JTextArea textArea;
    // End of variables declaration//GEN-END:variables

    @Override
    public void doRequest(final InboundSipRequest isr) {
        final SipRequest request = isr.getRequest();

        Util.logMessage(request, true, textArea);
        
        if (dialog == null && isr.getServerTransaction().getDialog() != null) {
            dialog = isr.getServerTransaction().getDialog();
        }
        
        SipResponse response = null;
        
        if (request.isREGISTER()) {

        } else if (request.isINVITE()) {

            // This is a re-INVITE
            response = AlertWindow.generateInviteResponse(isr, ResponseType.OK, properties);
            
        } else if (request.isACK()) {
            System.out.println("Call established.");
            
        } else if (request.isBYE()) {
            response = isr.createResponse(ResponseType.OK);
            setVisible(false);
            
        } else if (request.isCANCEL()) {
            
        } else if (request.isOPTIONS()) {
            response = isr.createResponse(ResponseType.OK);
            
        } else if (request.isREFER()) {

        } else {
            System.out.println("Unsupported request method: " + request.getMethod());
        }
        
        if (response != null) {
            Util.logMessage(response, false, textArea);
            isr.sendResponse(response);
        }
    }

    @Override
    public void doResponse(final InboundSipResponse isr) {
        final SipResponse response = isr.getResponse();
        
        Util.logMessage(response, true, textArea);
        
        final SipRequest originalRequest = isr.getRequest();
        final int code = response.getStatusCode();
        if (code >= 200 && code < 300 && originalRequest.isINVITE()) {
            
            dialog = isr.getDialog();
            
            // The UAC core MUST generate an ACK request for each 2xx received
            // from the transaction layer. The header fields of the ACK are
            // constructed in the same way as for any request sent within a
            // dialog (see Section 12)...
            
            final SipRequest ack = SipMessageFactory.createAck(isr);
            
            // If the 2xx contains an offer (based on the rules above), the ACK
            // MUST carry an answer in its body.
            
            if (originalRequest.getBody() == null && response.getBody() != null) {

                final Parameterable contentType = SipMessageHelper.getContentType(response);
                
                // TODO: Put real SDP here - all of this is temporary
                if (contentType != null && "application/sdp".equalsIgnoreCase(contentType.getValue())) {
                    final SessionDescription offer = SessionDescriptionBuilder.parse(response.getBody());
                    final String sdp = offer.toString();
                    ack.setBody(sdp);
                    SipMessageHelper.setContentLength(sdp.length(), ack);
                    SipMessageHelper.setContentType("application/sdp", ack);
                    ack.setBody(response.getBody());
                }
                // end temp

            }
            
            
            UserAgentClient.createUacAndSendRequest(this, ack, isr.getDialog());

            Util.logMessage(ack, false, textArea);
        }
    }
}
