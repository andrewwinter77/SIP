package org.andrewwinter.jsr289;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.andrewwinter.jsr289.api.SipSessionImpl;
import org.andrewwinter.sip.SipResponseHandler;
import org.andrewwinter.sip.message.InboundSipResponse;

/**
 *
 * @author andrew
 */
public class ApplicationPath implements SipResponseHandler {
    
    private final List<ApplicationPathEntry> entries;
    
    public ApplicationPath() {
        entries = new ArrayList<>();
    }
    
    public void add(final ApplicationPathEntry entry) {
        entries.add(entry);
    }

    @Override
    public void doResponse(final InboundSipResponse response) {
        final ListIterator<ApplicationPathEntry> iter = entries.listIterator(entries.size());
        while (iter.hasPrevious()) {
            final ApplicationPathEntry entry = iter.previous();
            ((SipSessionImpl) entry.getRequest().getSession()).doResponse(response);
        }
    }
}
