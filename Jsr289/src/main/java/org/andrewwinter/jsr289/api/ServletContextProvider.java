package org.andrewwinter.jsr289.api;

import javax.servlet.ServletContext;

/**
 *
 * @author andrew
 */
public class ServletContextProvider {

    private ServletContext context;
    
    ServletContext getServletContext() {
        return context;
    }
    
    public void setServletContext(final ServletContext context) {
        this.context = context;
    }
}
