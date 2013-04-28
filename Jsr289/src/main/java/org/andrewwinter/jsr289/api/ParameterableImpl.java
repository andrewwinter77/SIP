package org.andrewwinter.jsr289.api;

import org.andrewwinter.sip.parser.HeaderName;
import org.andrewwinter.sip.parser.Parameterable;

/**
 *
 * @author andrew
 */
public class ParameterableImpl extends AbstractParameterable {

    ParameterableImpl(final Parameterable parameterable, final HeaderName hn) {
        super(parameterable, hn);
    }
    
    @Override
    public String toString() {
        return parameterable.toString();
    }
    
    @Override
    public Object clone() {
        // Use null for the HeaderName because the application (rather than the container)
        // is creating a new Parameterable.
        
        return new ParameterableImpl((Parameterable) ((Parameterable) parameterable).clone(), null);
    }
}
