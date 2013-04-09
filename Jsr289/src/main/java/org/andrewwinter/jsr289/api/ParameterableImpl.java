package org.andrewwinter.jsr289.api;

import org.andrewwinter.sip.parser.GenericParameterable;
import org.andrewwinter.sip.parser.HeaderName;

/**
 *
 * @author andrew
 */
public class ParameterableImpl extends AbstractParameterable {

    private final GenericParameterable parameterable;
    
    ParameterableImpl(final GenericParameterable parameterable, final HeaderName hn) {
        super(parameterable, hn);
        this.parameterable = parameterable;
    }
    
    @Override
    public Object clone() {
        // Use null for the HeaderName because the application is creating a
        // new Parameterable.
        return new ParameterableImpl(parameterable.clone(), null);
    }
}
