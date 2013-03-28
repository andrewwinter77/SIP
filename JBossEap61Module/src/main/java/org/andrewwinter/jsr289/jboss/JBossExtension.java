package org.andrewwinter.jsr289.jboss;

import java.util.Locale;
import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.ReloadRequiredRemoveStepHandler;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.dmr.ModelNode;

/**
 *
 * @author andrewwinter77
 */
public class JBossExtension implements Extension {

    /**
     * The name of the subsystem within the model.
     */
    public static final String SUBSYSTEM_NAME = "jsr289";
    
    /**
     *
     */
    public static final String NAMESPACE = "urn:" + JBossExtension.class.getPackage().getName() + ":1.0";
    
    /**
     * 
     */
    private static final DescriptionProvider SUBSYSTEM_DESCRIPTION = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(Locale locale) {
            return SubsystemProviders.getSubsystemDescription(locale);
        }
    };
    
    /**
     * 
     */
    private static final DescriptionProvider SUBSYSTEM_ADD_DESCRIPTION = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(Locale locale) {
            return SubsystemProviders.getSubsystemAddDescription(locale);
        }
    };
    
    /**
     * 
     */
    private static final DescriptionProvider SUBSYSTEM_REMOVE_DESCRIPTION = new DescriptionProvider() {
        @Override
        public ModelNode getModelDescription(Locale locale) {
            return SubsystemProviders.getSubsystemRemoveDescription(locale);
        }
    };

    /**
     *
     * @param context
     */
    @Override
    public void initialize(final ExtensionContext context) {

        //register subsystem with its model version
        final SubsystemRegistration subsystem = context.registerSubsystem(SUBSYSTEM_NAME, 1, 0);
        ManagementResourceRegistration registration = subsystem.registerSubsystemModel(SUBSYSTEM_DESCRIPTION);
        registration.registerOperationHandler(ModelDescriptionConstants.ADD, SubsystemAddHandler.INSTANCE, SUBSYSTEM_ADD_DESCRIPTION, false);
        registration.registerOperationHandler(ModelDescriptionConstants.DESCRIBE, SubsystemDescribeHandler.INSTANCE, SubsystemDescribeHandler.INSTANCE, false, OperationEntry.EntryType.PRIVATE);
        registration.registerOperationHandler(ModelDescriptionConstants.REMOVE, ReloadRequiredRemoveStepHandler.INSTANCE, SUBSYSTEM_REMOVE_DESCRIPTION, false);

        //we can register additional submodels here
        subsystem.registerXMLElementWriter(SubsystemXmlParser.INSTANCE);
    }

    /**
     *
     * @param context
     */
    @Override
    public void initializeParsers(final ExtensionParsingContext context) {
        context.setSubsystemXmlMapping(SUBSYSTEM_NAME, NAMESPACE, SubsystemXmlParser.INSTANCE);
    }
}
