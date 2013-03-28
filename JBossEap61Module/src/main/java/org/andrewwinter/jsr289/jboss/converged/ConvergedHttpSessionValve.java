package org.andrewwinter.jsr289.jboss.converged;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

/**
 *
 * @author andrew
 */
public class ConvergedHttpSessionValve extends ValveBase {

    @Override
    public void invoke(Request request, final Response response) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            request = new RequestDelegator(request);
        }
        getNext().invoke(request, response);
    }
}
