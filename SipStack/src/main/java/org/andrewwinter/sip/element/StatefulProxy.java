package org.andrewwinter.sip.element;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.andrewwinter.sip.SipRequestHandler;
import org.andrewwinter.sip.SipResponseHandler;
import org.andrewwinter.sip.message.InboundSipRequest;
import org.andrewwinter.sip.message.InboundSipResponse;
import org.andrewwinter.sip.message.ResponseType;
import org.andrewwinter.sip.parser.Address;
import org.andrewwinter.sip.parser.SipMessage;
import org.andrewwinter.sip.parser.SipMessageHelper;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.parser.Uri;
import org.andrewwinter.sip.parser.Via;
import org.andrewwinter.sip.properties.ServerProperties;
import org.andrewwinter.sip.util.Util;

/**
 *
 * @author andrew
 */
public class StatefulProxy implements SipRequestHandler, SipResponseHandler {
    
    /**
     * The set of uris will either be predetermined by the contents of the
     * request or will be obtained from an abstract location service. Each
     * target in the set is represented as a URI.
     */
    private final List<Uri> targets;
    
    private final boolean parallelFork;

    private final Set<SipResponse> finalResponsesReceived;
    
    private boolean finalResponseForwarded;
    
    private Set<UserAgentClient> clients;
    
    /**
     * Do not modify this original request. Be sure to make a copy.
     */
    private final InboundSipRequest requestToProxy;
    
    private final boolean recordRoute;
    
    /**
     *
     * @param uris
     * @param parallelFork
     * @param requestToProxy
     */
    public StatefulProxy(
            final List<Uri> targets,
            final boolean parallelFork,
            final InboundSipRequest requestToProxy,
            final boolean recordRoute) {
        
        final Uri requestUri = requestToProxy.getRequest().getRequestUri();
        final String maddr = requestUri.getParameter("maddr");
        if (maddr == null) {
            this.targets = targets;
        } else {
            // If the Request-URI of the request contains an maddr parameter,
            // the Request-URI MUST be placed into the target set as the only
            // target URI, and the proxy MUST proceed to Section 16.6.
            
            this.targets = new ArrayList<Uri>();
            this.targets.add(requestUri);
        }
        
        this.parallelFork = parallelFork;
        this.requestToProxy = requestToProxy;
        clients = new HashSet<UserAgentClient>();
        finalResponsesReceived = new HashSet<SipResponse>();
        finalResponseForwarded = false;
        this.recordRoute = recordRoute;
    }
    
    private void parallelFork() {
        for (final Uri target : targets) {
            
            
            // 1. Copy request
            // The proxy starts with a copy of the received request. The copy
            // MUST initially contain all of the header fields from the received
            // request. Fields not detailed in the processing described below
            // MUST NOT be removed. The copy SHOULD maintain the ordering of the
            // header fields as in the received request. The proxy MUST NOT
            // reorder field values with a common field name
            // (See Section 7.3.1). The proxy MUST NOT add to, modify, or remove
            // the message body.
            
            final SipRequest proxiedRequest = (SipRequest) SipMessage.parse(requestToProxy.getRequest().toString());
            
            // 2. Request-URI
            // The Request-URI in the copy's start line MUST be replaced with
            // the URI for this target.

            // If the URI contains any parameters not allowed in a Request-URI,
            // they MUST be removed.

            // TODO: Remove illegal params
            
            proxiedRequest.setRequestUri(target);
            
            // 3. Max-Forwards
            final Integer maxForwards = proxiedRequest.getMaxForwards();
            if (maxForwards == null) {

                // If the copy does not contain a Max-Forwards header field, the
                // proxy MUST add one with a field value, which SHOULD be 70.
                
                SipMessageHelper.setMaxForwards(70, proxiedRequest);
            } else {
                // If the copy contains a Max-Forwards header field, the proxy
                // MUST decrement its value by one (1).
                
                SipMessageHelper.setMaxForwards(maxForwards-1, proxiedRequest);
            }

            // 4. Record-Route
            
            // If this proxy wishes to remain on the path of future requests in
            // a dialog created by this request (assuming the request creates a
            // dialog), it MUST insert a Record-Route header field value into
            // the copy before any existing Record-Route header field values,
            // even if a Route header field is already present.
            
            if (recordRoute) {
                final Address address = Address.parse("<sip:" + Util.getIpAddress() + ";lr>");
                SipMessageHelper.pushRecordRoute(address, proxiedRequest);
            }
            
            // TODO: Do this
            
            
            // 5. Add Additional Header Fields
            
            // TODO: Do this if it's required by JSR289
            
            
            // 6. Postprocess routing information
            
            
            
            
            // 7. Determine Next-Hop Address, Port, and Transport
            // THIS IS DONE BY UserAgentClient
            
            // 8. Add a Via header field value
            // The proxy MUST insert a Via header field value into the copy
            // before the existing Via header field values.
            
            final Via via = new Via(Util.getIpAddress());
            // Don't set a branch. Allow UserAgentClient to create one for us.
            via.setPort(ServerProperties.getInstance().getUnsecurePort());
            SipMessageHelper.pushVia(via, proxiedRequest);

            clients.add(UserAgentClient.createUacAndSendRequest(this, proxiedRequest, null));
        }
    }
    
    private void sequentialFork() {
        // TODO: Sequential forking
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     *
     */
    public void proxy() {
        if (parallelFork) {
            parallelFork();
        } else {
            sequentialFork();
        }
    }

    @Override
    public void doRequest(final InboundSipRequest isr) {
        final List<Uri> uris = new ArrayList<Uri>();
        uris.add(isr.getRequest().getRequestUri());
        isr.proxy(uris, true, false);
    }

    @Override
    public void doResponse(final InboundSipResponse isr) {
     
        // TODO: Likely multiple threads will come in here at the same time. Make it thread safe.
        
        final SipResponse response = isr.getResponse();
        
        // The proxy removes the topmost Via header field value from the
        // response.
        
        response.popVia();
        
        if (response.getVias().isEmpty()) {
            
            // If no Via header field values remain in the response, the
            // response was meant for this element and MUST NOT be forwarded.
            // The remainder of the processing described in this section is not
            // performed on this message, the UAC processing rules described in
            // Section 8.1.3 are followed instead (transport layer processing
            // has already occurred).
            
            // This will happen, for instance, when the element generates CANCEL
            // requests as described in Section 10.
            
            // TODO: Handle case where no Via headers remain
            
        } else {
            
            if (response.getStatusCode() >= 200) {
                
                // Final responses received are stored in the response context until
                // a final response is generated on the server transaction
                // associated with this context.

                finalResponsesReceived.add(response);
            }
            
            if (finalResponseForwarded) {
                
                // After a final response has been sent on the server
                // transaction, the following responses MUST be forwarded
                // immediately:
                //  - Any 2xx response to an INVITE request
                // A stateful proxy MUST NOT immediately forward any other
                // responses.
                
                final int status = response.getStatusCode();
                if (status >= 200 && status < 300) {
                    requestToProxy.sendResponse(response);
                }
            } else {

                // Until a final response has been sent on the server
                // transaction, the following responses MUST be forwarded
                // immediately:
                //  - Any provisional response other than 100 (Trying)
                //  - Any 2xx response

                final int status = response.getStatusCode();
                if (status > 100 && status < 300) {
                    requestToProxy.sendResponse(response);
                    if (status >= 200) {
                        finalResponseForwarded = true;
                    }
                }
            }
            
            if (!finalResponseForwarded) {
                
                // A stateful proxy MUST send a final response to a response
                // context's server transaction if no final responses have been
                // immediately forwarded by the above rules and all client
                // transactions in this response context have been terminated.
                
                boolean allTerminated = true;
                for (final UserAgentClient uac : clients) {
                    if (!uac.isTransactionTerminated()) {
                        allTerminated = false;
                    }
                }
                
                if (allTerminated) {
                    
                    SipResponse responseToForward;
                    if (finalResponsesReceived.isEmpty()) {
                    
                        // If there are no final responses in the context, the
                        // proxy MUST send a 408 (Request Timeout) response to
                        // the server transaction.
                        
                        responseToForward = requestToProxy.createResponse(ResponseType.REQUEST_TIMEOUT);
                        
                    } else {

                        // Otherwise, the proxy MUST forward a response from the
                        // responses stored in the response context.

                        responseToForward = chooseBestResponse(finalResponsesReceived);
                        
                        // TODO: 7. Aggregate Authorization Header Field Values
                        // TODO: 8. Record-Route
                        
                        
                    }
                    
                    // The proxy MUST pass the response to the server
                    // transaction associated with the response context. This
                    // will result in the response being sent to the location
                    // now indicated in the topmost Via header field value.
                    
                    requestToProxy.sendResponse(responseToForward);
                    finalResponseForwarded = true;
                }
            }
            
            
            if (finalResponseForwarded) {
                
                // If the forwarded response was a final response, the proxy
                // MUST generate a CANCEL request for all pending client
                // transactions associated with this response context.
                
                cancel();
            }
        }
    }
    
    /**
     * 
     * @param finalResponses Non-empty set of final responses.
     * @return 
     */
    private static SipResponse chooseBestResponse(final Set<SipResponse> finalResponses) {
        
        // The stateful proxy MUST choose the "best" final response among those
        // received and stored in the response context.
        
        // TODO: Choose best response as per p80
        return finalResponses.iterator().next();
    }
    
    /**
     *
     */
    public void cancel() {
        for (final UserAgentClient uac : clients) {
            if (!uac.isTransactionTerminated()) {
                uac.cancel(null); // Use null to get the SipStack to create its own CANCEL.
            }
        }
    }
}
