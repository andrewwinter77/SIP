package org.andrewwinter.jsr289.jboss;

import java.util.List;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import org.jboss.as.controller.parsing.ParseUtils;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.dmr.ModelNode;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

/**
 *
 * @author andrewwinter77
 */
public class SubsystemXmlParser implements XMLStreamConstants, XMLElementReader<List<ModelNode>>, XMLElementWriter<SubsystemMarshallingContext> {

    public static final SubsystemXmlParser INSTANCE = new SubsystemXmlParser();

    /**
     *
     * @param reader
     * @param list
     * @throws XMLStreamException
     */
    @Override
    public void readElement(
            final XMLExtendedStreamReader reader,
            final List<ModelNode> list) throws XMLStreamException {

        ParseUtils.requireNoAttributes(reader);
        ParseUtils.requireNoContent(reader);
        list.add(SubsystemDescribeHandler.createAddSubSystemOperation());
    }

    /**
     *
     * @param writer
     * @param context
     * @throws XMLStreamException
     */
    @Override
    public void writeContent(
            final XMLExtendedStreamWriter writer,
            final SubsystemMarshallingContext context) throws XMLStreamException {

        context.startSubsystemElement(JBossExtension.NAMESPACE, false);
        writer.writeEndElement();
    }
}
