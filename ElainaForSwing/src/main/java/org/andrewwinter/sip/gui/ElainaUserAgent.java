package org.andrewwinter.sip.gui;

import org.andrewwinter.sip.SipRequestHandler;
import org.andrewwinter.sip.SipResponseHandler;
import org.andrewwinter.sip.SipStack;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.element.UserAgentClient;
import org.andrewwinter.sip.message.InboundSipRequest;
import org.andrewwinter.sip.message.InboundSipResponse;
import org.andrewwinter.sip.message.ResponseType;
import org.andrewwinter.sip.message.SipMessageFactory;
import org.andrewwinter.sip.parser.HeaderName;
import org.andrewwinter.sip.parser.SipMessageHelper;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.properties.ServerProperties;
import org.andrewwinter.sip.properties.UserAgentProperties;

/**
 *
 * @author andrewwinter77
 */
public class ElainaUserAgent extends javax.swing.JFrame implements SipRequestHandler, SipResponseHandler {

    private final UserAgentProperties userAgentProperties;
    /**
     * Creates new form NewApplication
     */
    public ElainaUserAgent() {
        initComponents();
        userAgentProperties = new UserAgentProperties(null, "andrew");
        final SipStack stack = new SipStack();
        stack.init(this, ServerProperties.getInstance().getUnsecurePort());
        stack.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jDialog2 = new javax.swing.JDialog();
        jDialog3 = new javax.swing.JDialog();
        jDialog4 = new javax.swing.JDialog();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jPanel1 = new javax.swing.JPanel();
        tab = new javax.swing.JTabbedPane();
        inviteMethodPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        toAddressTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        dialBtn = new javax.swing.JButton();
        registerMethodPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        registrationStatus = new javax.swing.JLabel();
        registrarTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        expiresTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        registerBtn = new javax.swing.JButton();
        optionsMethodPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        optionsServerAddressTextField = new javax.swing.JTextField();
        sendOptionsBtn = new javax.swing.JButton();
        referMethodPanel = new javax.swing.JPanel();
        customMethodPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        messageTextArea = new javax.swing.JTextArea();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        deleteMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        org.jdesktop.layout.GroupLayout jDialog1Layout = new org.jdesktop.layout.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout jDialog2Layout = new org.jdesktop.layout.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout jDialog3Layout = new org.jdesktop.layout.GroupLayout(jDialog3.getContentPane());
        jDialog3.getContentPane().setLayout(jDialog3Layout);
        jDialog3Layout.setHorizontalGroup(
            jDialog3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        jDialog3Layout.setVerticalGroup(
            jDialog3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout jDialog4Layout = new org.jdesktop.layout.GroupLayout(jDialog4.getContentPane());
        jDialog4.getContentPane().setLayout(jDialog4Layout);
        jDialog4Layout.setHorizontalGroup(
            jDialog4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        jDialog4Layout.setVerticalGroup(
            jDialog4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Call"));
        jPanel4.setPreferredSize(new java.awt.Dimension(367, 62));

        toAddressTextField.setText("sip:andrew@192.168.1.77:5060");

        jLabel6.setText("To");

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(toAddressTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(toAddressTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel6))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        dialBtn.setText("Dial");
        dialBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dialBtnActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout inviteMethodPanelLayout = new org.jdesktop.layout.GroupLayout(inviteMethodPanel);
        inviteMethodPanel.setLayout(inviteMethodPanelLayout);
        inviteMethodPanelLayout.setHorizontalGroup(
            inviteMethodPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(inviteMethodPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(inviteMethodPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, inviteMethodPanelLayout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(dialBtn)))
                .addContainerGap())
        );
        inviteMethodPanelLayout.setVerticalGroup(
            inviteMethodPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(inviteMethodPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(dialBtn)
                .addContainerGap(159, Short.MAX_VALUE))
        );

        tab.addTab("INVITE", inviteMethodPanel);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Registration"));

        registrationStatus.setText("Unregistered");

        registrarTextField.setText("sip:192.168.1.77:5060");

        jLabel2.setText("Registrar");

        jLabel1.setText("Status");

        jLabel3.setText("Expires");

        expiresTextField.setText("900");
        expiresTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expiresTextFieldActionPerformed(evt);
            }
        });

        jLabel5.setText("seconds");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel2)
                    .add(jLabel1)
                    .add(jLabel3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(registrarTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(registrationStatus)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(expiresTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel5)))
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(registrationStatus))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(expiresTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(registrarTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        registerBtn.setText("Register");
        registerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                registerBtnActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout registerMethodPanelLayout = new org.jdesktop.layout.GroupLayout(registerMethodPanel);
        registerMethodPanel.setLayout(registerMethodPanelLayout);
        registerMethodPanelLayout.setHorizontalGroup(
            registerMethodPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(registerMethodPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(registerMethodPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, registerMethodPanelLayout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(registerBtn)))
                .addContainerGap())
        );
        registerMethodPanelLayout.setVerticalGroup(
            registerMethodPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(registerMethodPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(registerBtn)
                .addContainerGap(95, Short.MAX_VALUE))
        );

        tab.addTab("REGISTER", registerMethodPanel);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Query"));

        jLabel4.setText("SIP server");

        optionsServerAddressTextField.setText("sip:192.168.1.91:5060");
        optionsServerAddressTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsServerAddressTextFieldActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel4)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(optionsServerAddressTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(optionsServerAddressTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 6, Short.MAX_VALUE))
        );

        sendOptionsBtn.setText("Send");
        sendOptionsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendOptionsBtnActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout optionsMethodPanelLayout = new org.jdesktop.layout.GroupLayout(optionsMethodPanel);
        optionsMethodPanel.setLayout(optionsMethodPanelLayout);
        optionsMethodPanelLayout.setHorizontalGroup(
            optionsMethodPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(optionsMethodPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(optionsMethodPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, optionsMethodPanelLayout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(sendOptionsBtn)))
                .addContainerGap())
        );
        optionsMethodPanelLayout.setVerticalGroup(
            optionsMethodPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(optionsMethodPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(sendOptionsBtn)
                .addContainerGap(163, Short.MAX_VALUE))
        );

        tab.addTab("OPTIONS", optionsMethodPanel);

        org.jdesktop.layout.GroupLayout referMethodPanelLayout = new org.jdesktop.layout.GroupLayout(referMethodPanel);
        referMethodPanel.setLayout(referMethodPanelLayout);
        referMethodPanelLayout.setHorizontalGroup(
            referMethodPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 417, Short.MAX_VALUE)
        );
        referMethodPanelLayout.setVerticalGroup(
            referMethodPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 262, Short.MAX_VALUE)
        );

        tab.addTab("REFER", referMethodPanel);

        org.jdesktop.layout.GroupLayout customMethodPanelLayout = new org.jdesktop.layout.GroupLayout(customMethodPanel);
        customMethodPanel.setLayout(customMethodPanelLayout);
        customMethodPanelLayout.setHorizontalGroup(
            customMethodPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 417, Short.MAX_VALUE)
        );
        customMethodPanelLayout.setVerticalGroup(
            customMethodPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 262, Short.MAX_VALUE)
        );

        tab.addTab("Custom", customMethodPanel);

        messageTextArea.setEditable(false);
        messageTextArea.setColumns(20);
        messageTextArea.setFont(new java.awt.Font("Courier", 0, 14)); // NOI18N
        messageTextArea.setRows(5);
        messageTextArea.setBorder(null);
        jScrollPane1.setViewportView(messageTextArea);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(tab, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 438, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tab)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1)
        );

        fileMenu.setMnemonic('f');
        fileMenu.setText("File");

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setMnemonic('e');
        editMenu.setText("Edit");

        deleteMenuItem.setMnemonic('d');
        deleteMenuItem.setText("Preferences...");
        deleteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preferencesMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(deleteMenuItem);

        menuBar.add(editMenu);

        helpMenu.setMnemonic('h');
        helpMenu.setText("Help");

        contentsMenuItem.setMnemonic('c');
        contentsMenuItem.setText("Contents");
        helpMenu.add(contentsMenuItem);

        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void preferencesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preferencesMenuItemActionPerformed
        ConfigWindow dialog = new ConfigWindow(this, true);
        dialog.setVisible(true);
    }//GEN-LAST:event_preferencesMenuItemActionPerformed

    private void registerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_registerBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_registerBtnActionPerformed

    private void optionsServerAddressTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsServerAddressTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_optionsServerAddressTextFieldActionPerformed

    private void sendOptionsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendOptionsBtnActionPerformed
        final SipRequest request = SipMessageFactory.createOutOfDialogRequest(
                "OPTIONS",
                optionsServerAddressTextField.getText(),
                userAgentProperties.getFrom().toString(),
                null,
                userAgentProperties.getContact());

        // An Accept header field SHOULD be included to indicate the type of
        // message body the UAC wishes to receive in the response.
        // Typically, this is set to a format that is used to describe the
        // media capabilities of a UA, such as SDP (application/sdp).
        
        request.addHeader(HeaderName.ACCEPT, "application/sdp");
        
        Util.logMessage(request, false, messageTextArea);
        
        UserAgentClient.createUacAndSendRequest(this, request, null);
    }//GEN-LAST:event_sendOptionsBtnActionPerformed

    private void expiresTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expiresTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_expiresTextFieldActionPerformed

    private void dialBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dialBtnActionPerformed
        final SipRequest request = SipMessageFactory.createOutOfDialogRequest(
                "INVITE",
                toAddressTextField.getText(),
                userAgentProperties.getFrom().toString(),
                null,
                userAgentProperties.getContact());

        request.addHeader(HeaderName.ACCEPT, "application/sdp");
        
        final CallWindow guiDialog = new CallWindow(userAgentProperties, SipMessageHelper.getTo(request));
        
        WindowStore.getInstance().put(request.getCallId(), guiDialog);
        
        Util.logMessage(request, false, messageTextArea);
        
        final UserAgentClient uac = UserAgentClient.createUacAndSendRequest(guiDialog, request, null);
        guiDialog.setUserAgentClient(uac);
        guiDialog.setVisible(true);
    }//GEN-LAST:event_dialBtnActionPerformed

    @Override
    public void doRequest(final InboundSipRequest isr) {
        
        final SipRequest request = isr.getRequest();
        final CallWindow guiDialog = WindowStore.getInstance().getCallWindow(request.getCallId());
        if (guiDialog != null) {
            guiDialog.doRequest(isr);
            return;
        }
        
        SipResponse response = null;
        Util.logMessage(request, true, messageTextArea);
        
        final Dialog dialog = isr.getServerTransaction().getDialog();
        if (dialog == null) {
        
            if (request.isREGISTER()) {

            } else if (request.isINVITE()) {

                final AlertWindow alertWindow = new AlertWindow(this, false, messageTextArea, isr, userAgentProperties);
                alertWindow.setVisible(true);
                WindowStore.getInstance().put(request.getCallId(), alertWindow);

            } else if (request.isACK()) {
                throw new RuntimeException("Should never receive OOD ACK.");
            } else if (request.isBYE()) {
                throw new RuntimeException("Should never receive OOD BYE.");
            } else if (request.isCANCEL()) {
                
                final AlertWindow alertWindow = WindowStore.getInstance().getAlertWindow(request.getCallId());
                if (alertWindow != null) {
                    alertWindow.setVisible(false);
                    AudioManager.getInstance().stopRinging();
                    WindowStore.getInstance().removeAlertWindow(request.getCallId());
                }
                
                System.out.println("Application received CANCEL.");
            } else if (request.isOPTIONS()) {

                response = isr.createResponse(ResponseType.OK);

            } else if (request.isREFER()) {
            } else {
                System.out.println("Unsupported request method: " + request.getMethod());
            }

            if (response != null) {
                Util.logMessage(response, false, messageTextArea);
                isr.sendResponse(response);
            }
        }
    }

    @Override
    public void inviteClientTxnTimeout() {
        // TODO: Handle this - assume a 408?
    }

    @Override
    public void doResponse(final InboundSipResponse isr) {
        final SipResponse response = isr.getResponse();
        final CallWindow guiDialog = WindowStore.getInstance().getCallWindow(response.getCallId());
        if (guiDialog != null) {
            guiDialog.doResponse(isr);
        }
    }
    
    
    /**
     * @param args the command line arguments
     * @throws Exception  
     */
    public static void main(String args[]) throws Exception {
        AudioManager.getInstance().initialize();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ElainaUserAgent().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JPanel customMethodPanel;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JButton dialBtn;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JTextField expiresTextField;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JPanel inviteMethodPanel;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JDialog jDialog3;
    private javax.swing.JDialog jDialog4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JTextArea messageTextArea;
    private javax.swing.JPanel optionsMethodPanel;
    private javax.swing.JTextField optionsServerAddressTextField;
    private javax.swing.JPanel referMethodPanel;
    private javax.swing.JButton registerBtn;
    private javax.swing.JPanel registerMethodPanel;
    private javax.swing.JTextField registrarTextField;
    private javax.swing.JLabel registrationStatus;
    private javax.swing.JButton sendOptionsBtn;
    private javax.swing.JTabbedPane tab;
    private javax.swing.JTextField toAddressTextField;
    // End of variables declaration//GEN-END:variables
}
