package org.andrewwinter.jsr289;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.andrewwinter.jsr289.api.SipServletRequestImpl;
import org.andrewwinter.jsr289.api.SipSessionImpl;
import org.andrewwinter.jsr289.store.ApplicationPathStore;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.dialog.DialogId;

/**
 *
 * @author andrew
 */
public class ApplicationPath {
    
    private final List<SipServletRequestImpl> requests;
    
    private final String id;
    
    public ApplicationPath() {
        requests = new ArrayList<>();
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }
    
    public void add(final SipServletRequestImpl request) {
        requests.add(request);
        final Dialog dialog = request.getDialog();
        if (dialog != null) {
            final DialogId dialogId = dialog.getId();
            if (ApplicationPathStore.getInstance().get(dialogId) == null) {
                ApplicationPathStore.getInstance().put(dialogId, this);
            }
        }
    }
    
    public SipServletRequestImpl getLastRequest() {
        return requests.get(requests.size()-1);
    }
    
    public List<SipServletRequestImpl> getRequests() {
        return Collections.unmodifiableList(requests);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[ApplicationPath id=").append(id);
        for (final SipServletRequestImpl request : requests) {
            sb.append(", ");
            sb.append("app=").append(((SipSessionImpl) request.getSession()).getApplicationName());
        }
        sb.append("]");
        return sb.toString();
    }
}
