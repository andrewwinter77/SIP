package org.andrewwinter.jsr289.api;

import org.andrewwinter.sip.parser.GenericParameterable;
import org.andrewwinter.sip.parser.HeaderName;

/**
 *
 * @author andrew
 */
public class ParameterableImpl extends AbstractParameterable {

    ParameterableImpl(final GenericParameterable parameterable, final HeaderName hn) {
        super(parameterable, hn);
    }
    
    @Override
    public String toString() {
        return parameterable.toString();
    }
    
    @Override
    public Object clone() {
        // Use null for the HeaderName because the application is creating a
        // new Parameterable.
        return new ParameterableImpl(((GenericParameterable) parameterable).clone(), null);
    }
}
