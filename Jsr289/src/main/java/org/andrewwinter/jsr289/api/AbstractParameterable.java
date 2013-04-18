package org.andrewwinter.jsr289.api;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.sip.Parameterable;
import org.andrewwinter.sip.parser.HeaderName;
import org.andrewwinter.sip.parser.ParseException;

/**
 *
 * @author andrew
 */
public abstract class AbstractParameterable implements Parameterable {

    protected final org.andrewwinter.sip.parser.Parameterable parameterable;
    
    private final HeaderName hn;
    
    /**
     * @param parameterable null is used when it is known the instance will never
     * have any parameters. For example, the wildcard address, "*".
     * @param hn Name of the header field this Parameterable was parsed from. Null
     * means this Parameterable was created by the application.
     */
    protected AbstractParameterable(final org.andrewwinter.sip.parser.Parameterable parameterable, final HeaderName hn) {
        this.parameterable = parameterable;
        this.hn = hn;
    }

    public HeaderName getHeaderName() {
        return hn;
    }
    
    public org.andrewwinter.sip.parser.Parameterable getRfc3261Parameterable() {
        return parameterable;
    }
    
    @Override
    public String getValue() {
        return parameterable.getValue();
    }

    @Override
    public void setValue(final String value) {
        if (value == null) {
            throw new NullPointerException("Value cannot be null.");
        }
        if (HeaderName.FROM.equals(hn) || HeaderName.TO.equals(hn)) {
            throw new IllegalStateException("Value cannot be modified here.");
        }
        try {
            parameterable.setValue(value);
        } catch (final ParseException e) {
            throw new IllegalArgumentException("Illegal value for this parameterable.");
        }
    }

    @Override
    public String getParameter(final String key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null.");
        }
        return parameterable.getParameter(key);
    }

    @Override
    public void setParameter(String name, final String value) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null.");
        }
        if (name.trim().equalsIgnoreCase("tag") && (HeaderName.FROM.equals(hn) || HeaderName.TO.equals(hn))) {
            throw new IllegalStateException("Cannot remove tag for this address header.");
        }
        parameterable.setParameter(name, value);
    }

    @Override
    public void removeParameter(final String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null.");
        }
        if (name.trim().equalsIgnoreCase("tag") && (HeaderName.FROM.equals(hn) || HeaderName.TO.equals(hn))) {
            throw new IllegalStateException("Cannot remove tag for this address header.");
        }
        parameterable.setParameter(name, null);
    }

    @Override
    public Iterator<String> getParameterNames() {
        return parameterable.getParameterNames().iterator();
    }

    @Override
    public Set<Entry<String, String>> getParameters() {
        return parameterable.getParameters();
    }
    
    @Override
    public abstract Object clone();
}
