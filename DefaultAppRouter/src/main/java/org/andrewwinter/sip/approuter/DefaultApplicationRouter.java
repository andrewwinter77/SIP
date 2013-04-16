package org.andrewwinter.sip.approuter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.sip.Address;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.ar.SipApplicationRouter;
import javax.servlet.sip.ar.SipApplicationRouterInfo;
import javax.servlet.sip.ar.SipApplicationRoutingDirective;
import javax.servlet.sip.ar.SipApplicationRoutingRegion;
import javax.servlet.sip.ar.SipRouteModifier;
import javax.servlet.sip.ar.SipTargetedRequestInfo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultApplicationRouter implements SipApplicationRouter {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(DefaultApplicationRouter.class);
    
    /**
     * The properties file MUST be made available to the DAR and the
     * location/content of this file MUST be accessible from a hierarchical URI
     * which itself is to be supplied as a system property.
     */
    private static final String CONFIGURATION_FILE_SYSTEM_PROPERTY = "javax.servlet.sip.ar.dar.configuration";

    private static final Pattern LINE_PATTERN = Pattern.compile(" *?\\((.*?)\\) *?,?");
    
    private static final Pattern RULE_PATTERN = Pattern.compile("\"(.*?)\"");

    /**
     * Returned by getNextApplication when no routing rule matches.
     */
    private static final SipApplicationRouterInfo DEFAULT_ROUTER_INFO = new SipApplicationRouterInfo(
            null,
            null,
            null,
            null,
            SipRouteModifier.NO_ROUTE,
            null);
    
    private Map<String, List<SipApplicationRouterInfo>> rulesMap;
    
    private final Object processingRulesLock = new Object();
    
    private boolean initialized;
    
    private DefaultHttpClient httpclient;

    /**
     * 
     */
    @Override
    public void init() {
        init(null);
    }

    /**
     * 
     * @param properties 
     */
    @Override
    public void init(final Properties properties) {
        if (initialized) {
            throw new IllegalStateException("App router must not be initialized more than once.");
        }

        initialized = false;
        httpclient = new DefaultHttpClient();

        httpclient.setRedirectStrategy(new DefaultRedirectStrategy() {
            @Override
            public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) {
                boolean isRedirect = false;
                try {
                    isRedirect = super.isRedirected(request, response, context);
                } catch (ProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (!isRedirect) {
                    int responseCode = response.getStatusLine().getStatusCode();
                    if (responseCode == 301 || responseCode == 302) {
                        return true;
                    }
                }
                return isRedirect;
            }
        });
        
        refreshProperties();
        initialized = true;
    }

    /**
     * 
     * @param matcher
     * @return 
     */
    private static String getNextStringFromRule(final Matcher matcher) {
        if (matcher.find()) {
            String str = matcher.group();
            str = str.substring(1, str.length()-1);
            return str;
        } else {
            return "";
        } 
    }

    /**
     * 
     * @param method
     * @param rule 
     */
    private void processRule(final String method, final String rule) {
        final Matcher ruleMatcher = RULE_PATTERN.matcher(rule);
        
        final String appName = getNextStringFromRule(ruleMatcher);
        final String subscriberUri = getNextStringFromRule(ruleMatcher);
        
        final String routingRegionAsString = getNextStringFromRule(ruleMatcher);
        final SipApplicationRoutingRegion routingRegion;
        if ("ORIGINATING".equals(routingRegionAsString)) {
            routingRegion = SipApplicationRoutingRegion.ORIGINATING_REGION;
        } else if ("TERMINATING".equals(routingRegionAsString)) {
            routingRegion = SipApplicationRoutingRegion.TERMINATING_REGION;
        } else {
            routingRegion = SipApplicationRoutingRegion.NEUTRAL_REGION;
        }
        
        final String route = getNextStringFromRule(ruleMatcher);
        
        final String routeModifierAsString = getNextStringFromRule(ruleMatcher);
        final SipRouteModifier routeModifier = SipRouteModifier.valueOf(routeModifierAsString);
        
        final String stateInfoAsString = getNextStringFromRule(ruleMatcher);
        final Integer stateInfo = Integer.valueOf(stateInfoAsString);
        
        final SipApplicationRouterInfo info = new SipApplicationRouterInfo(
                appName,
                routingRegion,
                subscriberUri,
                new String[] { route },
                routeModifier,
                stateInfo);
        
        List<SipApplicationRouterInfo> rules = rulesMap.get(method);
        if (rules == null) {
            rules = new ArrayList<>();
            rulesMap.put(method, rules);
        }
        rules.add(info);
    }
    
    /**
     * E.g.,
     * INVITE: ("OriginatingCallWaiting", "DAR:From", "ORIGINATING", "", "NO_ROUTE", "0"), ("CallForwarding", "DAR:To", "TERMINATING", "", "NO_ROUTE", "1")
     */
    private void processLineFromPropertiesFile(String line) {
        final String[] parts = line.split(":", 2);
        final String method = parts[0];
        line = parts[1];
                
        final Matcher lineMatcher = LINE_PATTERN.matcher(line);
        while (lineMatcher.find()) {
            final String rule = lineMatcher.group();
            processRule(method, rule);
        }
    }
    
    /**
     * 
     */
    private void refreshProperties() {
        
        // If we don't synchronize this then httpclient.execute() sometimes barfs
        synchronized (processingRulesLock) {
        
            final String url = System.getProperty(CONFIGURATION_FILE_SYSTEM_PROPERTY);
            if (url == null) {
                LOG.error("Default Application Router (DAR) config file system property not set.");
                return;
            }

            final HttpGet httpget = new HttpGet(url);
            try {
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();

                if (entity == null) {

                } else {
                    final InputStream is = entity.getContent();
                    final BufferedReader in = new BufferedReader(new InputStreamReader(is));
                    String line;

                        rulesMap = new HashMap<>();
                        while((line = in.readLine()) != null) {
                            line = line.trim();
                            if (!line.isEmpty()) {
                                processLineFromPropertiesFile(line);
                            }
                        }
                }
            } catch (IOException e) {
                LOG.error("Error fetching DAR config file from " + url, e);
            }
        }
    }

    @Override
    public void destroy() {
        if (httpclient != null && httpclient.getConnectionManager() != null) {
            httpclient.getConnectionManager().shutdown();
        }
    }

    @Override
    public void applicationDeployed(final List<String> newlyDeployedApplicationNames) {
        refreshProperties();
    }

    @Override
    public void applicationUndeployed(final List<String> undeployedApplicationNames) {
        refreshProperties();
    }

    @Override
    public SipApplicationRouterInfo getNextApplication(
            final SipServletRequest initialRequest,
            final SipApplicationRoutingRegion region,
            final SipApplicationRoutingDirective directive,
            final SipTargetedRequestInfo targetedRequestInfo,
            final Serializable stateInfo) {

        if (initialRequest == null) {
            throw new NullPointerException("Initial request must not be null.");
        }

        if (!initialRequest.isInitial()) {
            throw new IllegalStateException("Request must be initial but is not.");
        }

        if (!initialized) {
            throw new IllegalStateException("App router must be initialized before invoking.");
        }

        synchronized (processingRulesLock) {
            
            final String method = initialRequest.getMethod();
            final List<SipApplicationRouterInfo> rules = rulesMap.get(method);
            
            if (rules == null || rules.isEmpty()) {
                return DEFAULT_ROUTER_INFO;
            } else {
            
                if (stateInfo == null && directive == SipApplicationRoutingDirective.NEW && targetedRequestInfo == null) {

                    final SipApplicationRouterInfo orig = rules.get(0);

                    return new SipApplicationRouterInfo(
                            orig.getNextApplicationName(),
                            orig.getRoutingRegion(),
                            getSubscriberUri(orig.getSubscriberURI(), initialRequest),
                            orig.getRoutes(),
                            orig.getRouteModifier(),
                            new Integer(0));
                    
                } else {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            }
        }
    }
    
    /**
     * The identity of the subscriber that the DAR returns. It can return any
     * header in the SIP request using the DAR directive DAR:SIP_HEADER e.g
     * "DAR:From" would return the SIP URI in From header. Or alternatively it
     * can return any string.
     * @param str
     * @param request
     * @return 
     */
    private static String getSubscriberUri(String str, final SipServletRequest request) {
        if (str.startsWith("DAR:")) {
            // Initially try to return just the URI from the address header
            // field, not the entire header field itself. If that files (e.g.,
            // because it's not a parsable header) then return the whole header
            // field.
            try {
                final Address addr = request.getAddressHeader(str.substring(4));
                if (addr != null) {
                    return addr.getURI().toString();
                }
            } catch (ServletParseException e) {
            }
            return request.getHeader(str.substring(4));
        } else {
            return str;
        }
    }
}
