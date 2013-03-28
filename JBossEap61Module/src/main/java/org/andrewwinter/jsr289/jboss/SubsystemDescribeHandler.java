package org.andrewwinter.jsr289.jboss;

import java.util.Locale;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.common.CommonDescriptions;
import org.jboss.dmr.ModelNode;

/**
 * Translates the current state of the model into operations similar to the ones
 * created by the Subsystem XML parser. This is used only when running in a
 * managed domain and is used when the host controller queries the domain
 * controller for the configuration of the profile used to start up each server.
 *
 * @author andrew
 */
public class SubsystemDescribeHandler implements OperationStepHandler, DescriptionProvider {

    static final SubsystemDescribeHandler INSTANCE = new SubsystemDescribeHandler();

    private SubsystemDescribeHandler() {
    }
    
    public static ModelNode createAddSubSystemOperation() {
        ModelNode subsystem = new ModelNode();
        subsystem.get(ModelDescriptionConstants.OP).set(ModelDescriptionConstants.ADD);
        subsystem.get(ModelDescriptionConstants.OP_ADDR).add(ModelDescriptionConstants.SUBSYSTEM, JBossExtension.SUBSYSTEM_NAME);
        return subsystem;
    }

    @Override
    public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
        context.getResult().add(createAddSubSystemOperation());
        context.completeStep();
    }

    @Override
    public ModelNode getModelDescription(Locale locale) {
        return CommonDescriptions.getSubsystemDescribeOperation(locale);
    }
}
