/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.andrewwinter.sip.element.registrar;

import java.io.Serializable;
import org.andrewwinter.sip.element.Binding;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import org.andrewwinter.sip.element.LocationService;
import org.andrewwinter.sip.message.InboundSipRequest;
import org.andrewwinter.sip.message.ResponseType;
import org.andrewwinter.sip.parser.Address;
import org.andrewwinter.sip.parser.HeaderName;
import org.andrewwinter.sip.parser.SipMessageHelper;
import org.andrewwinter.sip.parser.SipRequest;
import org.andrewwinter.sip.parser.SipResponse;
import org.andrewwinter.sip.parser.SipUri;
import org.andrewwinter.sip.parser.Uri;

/**
 *
 * @author andrewwinter77
 */
public class Registrar {
    
    // TODO: Put this value into ServerProperties. Make it part of server config.
    private static final int LOCALLY_CONFIGURED_DEFAULT_EXPIRES = 3600;
    
    /**
     * The address-of-record MUST be a SIP URI or SIPS URI so other schemes are
     * not considered in this method.
     * @param uri
     * @return
     */
    public static SipUri canonicalizeUri(final SipUri uri) {
        
        // The URI MUST then be converted to a canonical form. To do that, all
        // URI parameters MUST be removed (including the user-param), and any
        // escaped characters MUST be converted to their unescaped form.
        
        final SipUri newUri = (SipUri) Uri.parse(uri.toString());
        for (final String name : newUri.getParameterNames()) {
            newUri.setParameter(name, null);
        }
        return newUri;
    }
    
    // If neither mechanism for expressing a suggested expiration time is present in a REGISTER, the client is
// indicating its desire for the server to choose.
    
    /**
     *
     * @param isr
     * @return
     */
    public SipResponse doRegister(final InboundSipRequest isr) {
        
        SipResponse response = null;
        
        final SipRequest register = isr.getRequest();
        
        final Uri toUri = SipMessageHelper.getTo(register).getUri();
        
        // This address-of-record MUST be a SIP URI or SIPS URI.
        if (toUri instanceof SipUri) {
            
            final String canonicalizedUri = canonicalizeUri((SipUri) SipMessageHelper.getTo(register).getUri()).toString();

            final Set<Binding> bindingsToAdd = new HashSet<Binding>();
            final Set<Binding> bindingsToRemove = new HashSet<Binding>();

            final List<String> contactsAsStrings = register.getHeadersAsStrings(HeaderName.CONTACT);

            // If neither mechanism for expressing a suggested expiration time is
            // present in a REGISTER, the client is indicating its desire for the
            // server to choose.
            Integer expiresFieldValue = SipMessageHelper.getExpires(register);
            if (expiresFieldValue == null) {
                expiresFieldValue = LOCALLY_CONFIGURED_DEFAULT_EXPIRES;
            }

            // The registrar checks whether the request contains the Contact header
            // field. If not, it skips to the last step.

            if (contactsAsStrings != null && !contactsAsStrings.isEmpty()) {

                // If the Contact header field is present, the registrar checks if
                // there is one Contact field value that contains the special value
                // "*" and an Expires field.

                boolean starFound = false;
                for (final String contactAsString : contactsAsStrings) {
                    if ("*".equals(contactAsString)) {
                        starFound = true;
                        break;
                    }
                }


                if (starFound) {
                    if (contactsAsStrings.size()>1 || expiresFieldValue > 0) {

                        // If the request has additional Contact fields or an expiration
                        // time other than zero, the request is invalid, and the server MUST
                        // return a 400 (Invalid Request) and skip the remaining steps.

                        response = isr.createResponse(ResponseType.BAD_REQUEST);
                    } else {


                        final Set<Binding> bindings = LocationService.getInstance().getBindings(canonicalizedUri);
                        for (final Binding binding : bindings) {


                            if (binding.getCallId().equals(register.getCallId())) {


                                if (register.getCSeq().getSequence() > binding.getCSeqValue()) {

                                    // If it does agree, it MUST remove the binding only
                                    // if the CSeq in the request is higher than the
                                    // value stored for that binding.

                                    bindingsToRemove.add(binding);

                                } else {

                                    // Otherwise, the update MUST be aborted and the
                                    // request fails.

                                    response = isr.createResponse(ResponseType.SERVER_INTERNAL_ERROR);
                                    break;
                                }

                            } else {

                                // If not, the registrar checks whether the Call-ID agrees
                                // with the value stored for each binding. If not, it MUST
                                // remove the binding.

                                bindingsToRemove.add(binding);
                            }
                        }
                    }
                } else {

                    response = processContactHeaders(isr, expiresFieldValue, canonicalizedUri, bindingsToAdd, bindingsToRemove);
                }
            }

            if (response == null) {
                applyBindingChanges(canonicalizedUri, bindingsToAdd, bindingsToRemove);
                response = createOK(isr, canonicalizedUri);
            }
        
        } else {
            
            // This address-of-record MUST be a SIP URI or SIPS URI.
            
            // From RFC 4475:
            // A registrar would reject this request with a 400 Bad Request
            // response, since the To: header field is required to contain a SIP
            // or SIPS URI as an AOR.
            
            response = isr.createResponse(ResponseType.BAD_REQUEST.getStatusCode(), "To URI not SIP or SIPS");
        }
        

        return response;
    }
    
    
    /**
     * 
     * @param isr
     * @return 
     */
    private SipResponse createOK(final InboundSipRequest isr, final String canonicalizedUri) {
        final SipResponse ok = isr.createResponse(ResponseType.OK);
        
        // The response SHOULD include a Date header field.
        
        final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        final String date = sdf.format(new Date());
        SipMessageHelper.setDate(date, ok);

        // The response MUST contain Contact header field values enumerating all
        // current bindings. Each Contact value MUST feature an "expires"
        // parameter indicating its expiration interval chosen by the registrar.

        final long now = new Date().getTime();
        
        final Set<Binding> bindings = LocationService.getInstance().getBindings(canonicalizedUri);
        if (bindings != null && !bindings.isEmpty()) {
            for (final Binding binding : bindings) {
                
                int expires = (int) ((binding.getExpiryTime().getTime() - now + 1) / 1000);
                if (expires < 0) {
                    expires = 0;
                }
                
                final Address address = new Address(binding.getUri());
                address.setParameter("expires", String.valueOf(expires));
                ok.addHeader(HeaderName.CONTACT, address.toString());
            }
        }
        
        return ok;
    }
    
    /**
     * 
     * @param isr
     * @param expiresFieldValue
     * @param canonicalizedUri
     * @return 
     */
    private SipResponse processContactHeaders(
            final InboundSipRequest isr,
            final Integer expiresFieldValue,
            final String canonicalizedUri,
            final Set<Binding> bindingsToAdd,
            final Set<Binding> bindingsToRemove) {

        final SipRequest register = isr.getRequest();
        
        SipResponse response = null;
        
        // The registrar now processes each contact address in the
        // Contact header field in turn. For each address, it determines
        // the expiration interval as follows:
        //   - If the field value has an expires parameter, that value
        //     MUST be taken as the requested expiration.

        final List<Address> contacts = SipMessageHelper.getContact(register);
        for (final Address contact : contacts) {

            Integer expires;

            final String expiresParam = contact.getParameter("expires");
            if (expiresParam == null) {

                // If there is no such parameter, but the request has an
                // Expires header field, that value MUST be taken as the
                // requested expiration.

                expires = expiresFieldValue;

            } else {

                try {
                    expires = Integer.parseInt(expiresParam);
                } catch (NumberFormatException e) {

                    // Malformed values SHOULD be treated as equivalent
                    // to 3600.

                    expires = 3600;
                }
            }

            Binding binding = LocationService.getInstance().getBinding(canonicalizedUri, contact.getUri());
            if (binding == null) {

                // If the binding does not exist, it is tentatively
                // added.
                
                binding = new Binding(
                        register.getCallId(),
                        register.getCSeq().getSequence(),
                        contact.getUri(),
                        createExpiryTime(expires));
                
                bindingsToAdd.add(binding);

            } else {

                // If the binding does exist, the registrar checks the
                // Call-ID value.

                if (binding.getCallId().equals(register.getCallId())) {

                    // If they are the same, the registrar compares the
                    // CSeq value.

                    if (register.getCSeq().getSequence() > binding.getCSeqValue()) {

                        // If the value is higher than that of the
                        // existing binding, it MUST update or remove
                        // the binding as above.

                        bindingsToRemove.add(binding);
                        if (expires > 0) {
                           binding = new Binding(
                                   register.getCallId(),
                                   register.getCSeq().getSequence(),
                                   contact.getUri(),
                                   createExpiryTime(expires));
                           
                           bindingsToAdd.add(binding);
                        }

                    } else {

                        // If not, the update MUST be aborted and the
                        // request fails.

                        return isr.createResponse(ResponseType.SERVER_INTERNAL_ERROR);
                    }


                } else {

                    // If the Call-ID value in the existing binding
                    // differs from the Call-ID value in the request,
                    // the binding MUST be removed if the expiration
                    // time is zero and updated otherwise.

                    bindingsToRemove.add(binding);
                    if (expires > 0) {
                           binding = new Binding(
                                   register.getCallId(),
                                   register.getCSeq().getSequence(),
                                   contact.getUri(),
                                   createExpiryTime(expires));
                           
                           bindingsToAdd.add(binding);
                    }
                }
            }
        }

        return response;
    }
    
    
    private Date createExpiryTime(final int expires) {
        final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.add(Calendar.SECOND, expires);
        return cal.getTime();
    }
    
    
    private void applyBindingChanges(
            final String canonicalizedUri,
            final Set<Binding> bindingsToAdd,
            final Set<Binding> bindingsToRemove) {
        
        // The binding updates MUST be committed (that is, made visible to the
        // proxy or redirect server) if and only if all binding updates and
        // additions succeed. If any one of them fails (for example, because the
        // back-end database commit failed), the request MUST fail with a 500
        // (Server Error) response and all tentative binding updates MUST be
        // removed.
        
        LocationService.getInstance().applyBindingsChanges(
                canonicalizedUri, bindingsToAdd, bindingsToRemove);
    }
}
