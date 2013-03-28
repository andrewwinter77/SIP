package org.andrewwinter.servlet;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 *
 * @author andrew
 */
public class ServletConfigImpl implements ServletConfig {

    private final String servletName;
    
    private final ServletContext context;
    
    private final Map<String, String> initParams;
    
    /**
     * 
     * @param servletName The name of this servlet instance.
     */
    public ServletConfigImpl(final String servletName, final ServletContext context) {
        this.servletName = servletName;
        this.context = context;
        initParams = new HashMap<>();
    }
    
    @Override
    public String getServletName() {
        return servletName;
    }

    @Override
    public ServletContext getServletContext() {
        return context;
    }

    @Override
    public String getInitParameter(final String name) {
        return initParams.get(name);
    }

    @Override
    public Enumeration getInitParameterNames() {
        return Collections.enumeration(initParams.keySet());
    }
}
