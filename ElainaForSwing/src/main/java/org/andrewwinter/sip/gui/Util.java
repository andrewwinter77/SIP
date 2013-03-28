/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.sip.gui;

import javax.swing.JTextArea;
import org.andrewwinter.sip.parser.SipMessage;

/**
 *
 * @author andrew
 */
class Util {

    public static void logMessage(final SipMessage msg, final boolean inbound, final JTextArea area) {
        String direction = "OUT";
        if (inbound) {
            direction = "IN=";
        }
        area.append("\n==" + direction + "===========================\n" + msg + "\n\n");
    }
    
}
