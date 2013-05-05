package org.andrewwinter.sip.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.andrewwinter.sip.SipResponseHandler;
import org.andrewwinter.sip.dialog.Dialog;
import org.andrewwinter.sip.message.SipMessageFactory;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipUri;
import org.andrewwinter.sip.parser.Uri;
import org.andrewwinter.sip.transaction.client.ClientTransaction;
import org.andrewwinter.sip.transaction.client.invite.InviteClientTransaction;
import org.andrewwinter.sip.transaction.client.noninvite.NonInviteClientTransaction;
import org.andrewwinter.sip.transport.RequestSender;

/**
 *
 * @author andrewwinter77
 */
public class UserAgentClient {

    private final SipRequest request;
    
    private final Dialog dialog;
    
    private final SipResponseHandler listener;
    
    /**
     * List of URIs to target, ordered most preferred to least preferred.
     */
    private List<SipUri> destinationUris;
    
    private int destinationUriIndex;
    
    private ClientTransaction clientTxn;

    private final boolean setRandomViaBranchParam;
    
    /**
     * 
     * @return 
     */
    public boolean isTransactionTerminated() {
        return clientTxn.isTerminated();
    }
    
    private UserAgentClient(final SipResponseHandler listener, final SipRequest request, final Dialog dialog) {
        this.listener = listener;
        this.request = request;
        this.dialog = dialog;
        
        // If the request has not been assigned a branch param then we will assign
        // one before sending. The caller will want to assign its own branch param in
        // the case of, say, CANCEL and ACK when the branch param must be the same
        // as the INVITE being cancelled or acknowledged.
        setRandomViaBranchParam = request.getTopmostVia().getBranch() == null;
        
        destinationUris = determineDestinationUris(request);
        destinationUriIndex = 0;
    }
    
    /**
     *
     * @return
     */
    public ClientTransaction getClientTransaction() {
        return clientTxn;
    }
    
    private static List<SipUri> determineDestinationUris(final SipRequest request) {
        final List<SipUri> uris = new ArrayList<SipUri>();
        
        
        // If the first element in the route set indicated a strict router
        // (resulting in forming the request as described in Section 12.2.1),
        // the procedures MUST be applied to the Request-URI of the request.
        // Otherwise, the procedures are applied to the first Route header field
        // value in the request (if one exists), or to the request's Request-URI
        // if there is no Route header field present.        
        
        // TODO: Select destination using rules in section 8.1.2. Here I'm assuming we'll be using the request-uri.
        // TODO: Also don't assume it's a sip uri. It might be a tel uri!
        
        // TODO: This is temporary...
        if (!request.getRoutes().isEmpty()) {
            final Uri uri = request.getRoutes().get(0).getUri();
            if (uri.isSipUri()) {
                uris.add((SipUri) uri);
            }
        }
        // END TODO
        
        uris.add((SipUri) request.getRequestUri());
        
        return uris;
    }
    
    private static boolean isIPV6Address(final String address) {
        // TODO: Add support for IPV6
        return false;
    }
    
    private static boolean isIPV4Address(final String address) {
        
        // Note this has to be done this way to support XMLVM's current
        // capabilities.
        
        final String[] parts = address.split("\\.");
        if (parts.length != 4) {
            return false;
        }
        for (final String part : parts) {
            try {
                final int value = Integer.valueOf(part);
                if (value > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        
        return true;
    }

    private static boolean isNumericIp(final String address) {
        return isIPV4Address(address) || isIPV6Address(address);
    }
    
    
    private String determineTransport(final SipUri uri) {
        
        String transport;
        
        // If the URI specifies a transport protocol in the transport parameter,
        // that transport protocol SHOULD be used.
        transport = uri.getParameter("transport");
        if (transport == null) {
            
            if (isNumericIp(uri.getHost()) || uri.getPort() > 0) {
                
                // Otherwise, if no transport protocol is specified, but the
                // TARGET is a numeric IP address, the client SHOULD use UDP for
                // a SIP URI, and TCP for a SIPS URI.

                // Similarly, if no transport protocol is specified, and the
                // TARGET is not numeric, but an explicit port is provided, the
                // client SHOULD use UDP for a SIP URI, and TCP for a SIPS URI.
                
                final String scheme = uri.getScheme();
                if ("sip".equals(scheme)) {
                    transport = "UDP";
                } else if ("sips".equals(scheme)) {
                    transport = "TCP";
                } else {
                    System.out.println("Unknown transport");
                    // TODO: Handle this error
                }
            }
        } else {
            transport = transport.toUpperCase(Locale.US);
        }
        
        if (transport == null) {
            
            // Otherwise, if no transport protocol or port is specified, and the
            // target is not a numeric IP address, the client SHOULD perform a
            // NAPTR query for the domain in the URI.
        
            // TODO: NAPTR query
            
            throw new UnsupportedOperationException("Non-numeric addresses not yet supported (" + uri + ")");
        }
        
        return transport;
    }

    
    private Destination applyDnsProcedures(final SipUri uri) {
        
        final String transport = determineTransport(uri);
        int port = uri.getPort();
        String host = uri.getHost();
        
        if (isNumericIp(host)) {
            
            // If TARGET is a numeric IP address, the client uses that address.
            
            if (port >= 0) {
              // If the URI also contains a port, it uses that port.
            } else {
                
                // If no port is specified, it uses the default port for the
                // particular transport protocol.
                
                if ("UDP".equals(transport) || "TCP".equals(transport)) {
                    port = 5060;
                } else {
                    // TODO: Handle TLS over TCP for sips URIs.
                }
            }

            
        } else {
            // TODO: Support non-numeric addresses
        }
        
        
        return new Destination(host, port, transport);
    }
    
    private void sendRequest() {
        
        // When the TU wishes to initiate a new transaction, it creates a client
        // transaction and passes it the SIP request to send and an IP address,
        // port, and transport to which to send it. The client transaction
        // begins execution of its state machine.
        
        // Once the request has been constructed, the address of the server is
        // computed and the request is sent, using the same procedures for
        // requests outside of a dialog (Section 8.1.2).

        // The UAC SHOULD follow the procedures defined in [4] for stateful
        // elements, trying each address until a server is contacted. Each try
        // constitutes a new transaction, and therefore each carries a different
        // topmost Via header field value with a new branch parameter.
        
        // TODO: Iterate over destinationUris but remember to stop iterating if cancel() has been called.
        
        final SipUri uri = destinationUris.get(destinationUriIndex++);
        final Destination dest = applyDnsProcedures(uri);

        if (setRandomViaBranchParam) {
            request.getTopmostVia().setBranch(SipMessageFactory.createBranchTag());
        }
        
        if (request.isACK()) {
            
            // However, the request is passed to the transport layer directly
            // for transmission, rather than a client transaction.
            
            final RequestSender sender = new RequestSender(dest);
            sender.send(request);
            
        } else if (request.isINVITE()) {
            
            clientTxn = InviteClientTransaction.create(
                    listener,
                    request,
                    dest);
            
        } else {
        
            clientTxn = NonInviteClientTransaction.create(
                    listener,
                    request,
                    dialog,
                    dest);
        }
    }
    
    /**
     * Null argument can be used. Null means SipStack will create a suitable CANCEL itself.
     * @param cancel 
     */
    public void cancel(final SipRequest cancel) {
        if (clientTxn instanceof InviteClientTransaction) {
            clientTxn.cancel(cancel);
        }
    }
    
    /**
     *
     * @param listener
     * @param request
     * @param dialog
     * @return  
     */
    public static UserAgentClient createUacAndSendRequest(final SipResponseHandler listener, final SipRequest request, final Dialog dialog) {
        final UserAgentClient uac = new UserAgentClient(listener, request, dialog);
        uac.sendRequest();
        return uac;
    }
}
