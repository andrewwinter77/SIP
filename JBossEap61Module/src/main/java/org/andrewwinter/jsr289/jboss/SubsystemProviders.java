package org.andrewwinter.jsr289.jboss;

import java.util.Locale;
import java.util.ResourceBundle;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.dmr.ModelNode;

class SubsystemProviders {

    static final String RESOURCE_NAME = Constants.MODULE_NAME + ".LocalDescriptions";

    static ModelNode getSubsystemDescription(final Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);
        final ModelNode subsystem = new ModelNode();
        subsystem.get(ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("jsr289"));
        subsystem.get(ModelDescriptionConstants.HEAD_COMMENT_ALLOWED).set(true);
        subsystem.get(ModelDescriptionConstants.TAIL_COMMENT_ALLOWED).set(true);
        subsystem.get(ModelDescriptionConstants.NAMESPACE).set(JBossExtension.NAMESPACE);

        subsystem.get(ModelDescriptionConstants.ATTRIBUTES).setEmptyObject();
        subsystem.get(ModelDescriptionConstants.CHILDREN).setEmptyObject();
        subsystem.get(ModelDescriptionConstants.OPERATIONS);

        return subsystem;
    }

    static ModelNode getSubsystemAddDescription(final Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);
        final ModelNode op = new ModelNode();
        op.get(ModelDescriptionConstants.OPERATION_NAME).set(ModelDescriptionConstants.ADD);
        op.get(ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("jsr289.add"));
        op.get(ModelDescriptionConstants.REPLY_PROPERTIES).setEmptyObject();
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES).setEmptyObject();
        return op;
    }

    static ModelNode getSubsystemRemoveDescription(final Locale locale) {
        final ResourceBundle bundle = getResourceBundle(locale);
        final ModelNode op = new ModelNode();
        op.get(ModelDescriptionConstants.OPERATION_NAME).set(ModelDescriptionConstants.REMOVE);
        op.get(ModelDescriptionConstants.DESCRIPTION).set(bundle.getString("jsr289.remove"));
        op.get(ModelDescriptionConstants.REPLY_PROPERTIES).setEmptyObject();
        op.get(ModelDescriptionConstants.REQUEST_PROPERTIES).setEmptyObject();
        return op;
    }

    private static ResourceBundle getResourceBundle(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return ResourceBundle.getBundle(RESOURCE_NAME, locale);
    }
}