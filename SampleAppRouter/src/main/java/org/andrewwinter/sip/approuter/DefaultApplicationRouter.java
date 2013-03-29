package org.andrewwinter.sip.approuter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    
    private static final Pattern LINE_PATTERN = Pattern.compile(" *?\\((.*?)\\) *?,?");
    
    private static final Pattern RULE_PATTERN = Pattern.compile("\"(.*?)\"");

    private Map<String, List<SipApplicationRouterInfo>> rules;
    
    
    private boolean initialized;
    
    private final DefaultHttpClient httpclient;
    /**
     * The properties file MUST be made available to the DAR and the
     * location/content of this file MUST be accessible from a hierarchical URI
     * which itself is to be supplied as a system property.
     */
    private static final String CONFIGURATION_FILE_SYSTEM_PROPERTY = "javax.servlet.sip.ar.dar.configuration";

    /**
     * 
     */
    public DefaultApplicationRouter() {
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
    }

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
        refreshProperties();
        initialized = true;
    }

    private static String getNextStringFromRule(final Matcher matcher) {
        if (matcher.find()) {
            String str = matcher.group();
            str = str.substring(1, str.length()-1);
            return str;
        } else {
            return "";
        } 
    }

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
        
        
        final String stateInfo = getNextStringFromRule(ruleMatcher);
        
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
                
                rules = new HashMap<>();
                
                final InputStream is = entity.getContent();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String line = null;

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

    @Override
    public void destroy() {
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

//        final SipApplicationRouterInfo info = new SipApplicationRouterInfo(
//                null,
//                region,
//                null,
//                strings,
//                SipRouteModifier.ROUTE,
//                stateInfo);
//
//        return info;
        return null;
    }

    // INVITE: ("OriginatingCallWaiting", "DAR:From", "ORIGINATING", "", "NO_ROUTE", "0"), ("CallForwarding", "DAR:To", "TERMINATING", "", "NO_ROUTE", "1")
    public static void main(final String[] args) {
        
        final String line = "(\"OriginatingCallWaiting\", \"DAR:From\", \"ORIGINATING\", \"\", \"NO_ROUTE\", \"0\"), (\"CallForwarding\", \"DAR:To\", \"TERMINATING\", \"\", \"NO_ROUTE\", \"1\")";
        Pattern p = Pattern.compile("\\(.*\\)");
        Matcher m = p.matcher(line);
        System.out.println(m.matches());
    }
}
