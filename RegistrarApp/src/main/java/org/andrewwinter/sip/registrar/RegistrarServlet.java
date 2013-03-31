package org.andrewwinter.sip.registrar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;
import java.util.TimeZone;
import javax.servlet.ServletException;
import javax.servlet.sip.Address;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.URI;
import javax.servlet.sip.SipURI;

@javax.servlet.sip.annotation.SipServlet
public class RegistrarServlet extends SipServlet {

    private static final int LOCALLY_CONFIGURED_DEFAULT_EXPIRES = 3600;

    @Override
    protected void doRegister(SipServletRequest request) throws ServletException, IOException {

        SipServletResponse response = null;

        final URI toUri = request.getTo().getURI();

        // This address-of-record MUST be a SIP URI or SIPS URI.
        if (toUri instanceof SipURI) {

            final String canonicalizedUri = canonicalizeUri((SipURI) toUri.clone()).toString();

            final Set<Binding> bindingsToAdd = new HashSet<>();
            final Set<Binding> bindingsToRemove = new HashSet<>();

            // If neither mechanism for expressing a suggested expiration time is
            // present in a REGISTER, the client is indicating its desire for the
            // server to choose.
            int expiresFieldValue = request.getExpires();
            if (expiresFieldValue == -1) {
                expiresFieldValue = LOCALLY_CONFIGURED_DEFAULT_EXPIRES;
            }

            final ListIterator<String> contactsAsStrings = request.getHeaders("Contact");

            // The registrar checks whether the request contains the Contact header
            // field. If not, it skips to the last step.

            if (contactsAsStrings.hasNext()) {

                // If the Contact header field is present, the registrar checks if
                // there is one Contact field value that contains the special value
                // "*" and an Expires field.

                int contactHeaderCount = 0;
                boolean starFound = false;
                while (contactsAsStrings.hasNext()) {
                    final String contactAsString = contactsAsStrings.next();
                    if ("*".equals(contactAsString)) {
                        starFound = true;
                    }
                    ++contactHeaderCount;
                }

                if (starFound) {
                    if (contactHeaderCount > 1 || expiresFieldValue > 0) {

                        // If the request has additional Contact fields or an expiration
                        // time other than zero, the request is invalid, and the server MUST
                        // return a 400 (Invalid Request) and skip the remaining steps.

                        response = request.createResponse(SipServletResponse.SC_BAD_REQUEST);
                    } else {


                        final Set<Binding> bindings = LocationService.getInstance().getBindings(canonicalizedUri);
                        for (final Binding binding : bindings) {


                            if (binding.getCallId().equals(request.getCallId())) {


                                if (Util.getCSeqValue(request) > binding.getCSeqValue()) {

                                    // If it does agree, it MUST remove the binding only
                                    // if the CSeq in the request is higher than the
                                    // value stored for that binding.

                                    bindingsToRemove.add(binding);

                                } else {

                                    // Otherwise, the update MUST be aborted and the
                                    // request fails.

                                    response = request.createResponse(SipServletResponse.SC_SERVER_INTERNAL_ERROR);
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

                    response = processContactHeaders(request, expiresFieldValue, canonicalizedUri, bindingsToAdd, bindingsToRemove);
                }
            }

            if (response == null) {
                applyBindingChanges(canonicalizedUri, bindingsToAdd, bindingsToRemove);
                response = createOK(request, canonicalizedUri.toString());
            }

        } else {

            // This address-of-record MUST be a SIP URI or SIPS URI.

            // From RFC 4475:
            // A registrar would reject this request with a 400 Bad Request
            // response, since the To: header field is required to contain a SIP
            // or SIPS URI as an AOR.

            response = request.createResponse(SipServletResponse.SC_BAD_REQUEST, "To URI not SIP or SIPS");
        }

        response.send();
    }

    /**
     * The address-of-record MUST be a SIP URI or SIPS URI so other schemes are
     * not considered in this method. Modifies the copy passed in, but also
     * returns it for convenience.
     *
     * @param uri
     * @return
     */
    public static SipURI canonicalizeUri(final SipURI uri) {

        // The URI MUST then be converted to a canonical form. To do that, all
        // URI parameters MUST be removed (including the user-param), and any
        // escaped characters MUST be converted to their unescaped form.

        Iterator<String> iter = uri.getParameterNames();
        while (iter.hasNext()) {
            uri.removeParameter(iter.next());
        }

        // TODO: Unescape any escaped characters.

        return uri;
    }

    /**
     *
     * @param isr
     * @return
     */
    private SipServletResponse createOK(final SipServletRequest request, final String canonicalizedUri) {
        final SipServletResponse ok = request.createResponse(SipServletResponse.SC_OK);

        // The response SHOULD include a Date header field.

        final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        final String date = sdf.format(new Date());
        ok.setHeader("Date", date);

        // The response MUST contain Contact header field values enumerating all
        // current bindings. Each Contact value MUST feature an "expires"
        // parameter indicating its expiration interval chosen by the registrar.

        final long now = new Date().getTime();

        final Set<Binding> bindings = LocationService.getInstance().getBindings(canonicalizedUri);
        if (bindings != null && !bindings.isEmpty()) {

            final SipFactory sf = (SipFactory) getServletContext().getAttribute("javax.servlet.sip.SipFactory");

            for (final Binding binding : bindings) {

                int expires = (int) ((binding.getExpiryTime().getTime() - now + 1) / 1000);
                if (expires < 0) {
                    expires = 0;
                }

                try {
                    final Address address = sf.createAddress(binding.getUri());
                    address.setParameter("expires", String.valueOf(expires));
                    ok.addAddressHeader("Contact", address, false);
                } catch (ServletParseException e) {
                    // Should never happen.
                }
            }
        }

        return ok;
    }

    private SipFactory getSipFactory() {
        return (SipFactory) getServletContext().getAttribute("javax.servlet.sip.SipFactory");
    }

    /**
     *
     * @param isr
     * @param expiresFieldValue
     * @param canonicalizedUri
     * @return
     */
    private SipServletResponse processContactHeaders(
            final SipServletRequest request,
            final Integer expiresFieldValue,
            final String canonicalizedUri,
            final Set<Binding> bindingsToAdd,
            final Set<Binding> bindingsToRemove) {

        SipServletResponse response = null;

        // The registrar now processes each contact address in the
        // Contact header field in turn. For each address, it determines
        // the expiration interval as follows:
        //   - If the field value has an expires parameter, that value
        //     MUST be taken as the requested expiration.

        final ListIterator<Address> contacts;
        try {
            contacts = request.getAddressHeaders("Contact");
        } catch (final ServletParseException e) {
            return request.createResponse(SipServletResponse.SC_BAD_REQUEST, "Illegal Contact header value");
        }

        final int cseqValue;
        try {
            cseqValue = Util.getCSeqValue(request);
        } catch (ServletParseException e) {
            return request.createResponse(SipServletResponse.SC_BAD_REQUEST, "Illegal CSeq header value");
        }

        while (contacts.hasNext()) {

            final Address contact = contacts.next();
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

            Binding binding = LocationService.getInstance().getBinding(
                    canonicalizedUri,
                    contact.getURI(),
                    getSipFactory());
            if (binding == null) {

                // If the binding does not exist, it is tentatively
                // added.

                binding = new Binding(
                        request.getCallId(),
                        cseqValue,
                        contact.getURI().toString(),
                        createExpiryTime(expires));

                bindingsToAdd.add(binding);
            } else {

                // If the binding does exist, the registrar checks the
                // Call-ID value.

                if (binding.getCallId().equals(request.getCallId())) {

                    // If they are the same, the registrar compares the
                    // CSeq value.

                    if (cseqValue > binding.getCSeqValue()) {

                        // If the value is higher than that of the
                        // existing binding, it MUST update or remove
                        // the binding as above.

                        bindingsToRemove.add(binding);
                        if (expires > 0) {
                            binding = new Binding(
                                    request.getCallId(),
                                    cseqValue,
                                    contact.getURI().toString(),
                                    createExpiryTime(expires));

                            bindingsToAdd.add(binding);
                        }

                    } else {

                        // If not, the update MUST be aborted and the
                        // request fails.

                        return request.createResponse(SipServletResponse.SC_SERVER_INTERNAL_ERROR);
                    }


                } else {

                    // If the Call-ID value in the existing binding
                    // differs from the Call-ID value in the request,
                    // the binding MUST be removed if the expiration
                    // time is zero and updated otherwise.

                    bindingsToRemove.add(binding);
                    if (expires > 0) {
                        binding = new Binding(
                                request.getCallId(),
                                cseqValue,
                                contact.getURI().toString(),
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
                canonicalizedUri, bindingsToAdd, bindingsToRemove, getSipFactory());
    }
}
