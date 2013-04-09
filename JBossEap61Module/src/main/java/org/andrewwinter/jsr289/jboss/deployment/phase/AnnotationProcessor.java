package org.andrewwinter.jsr289.jboss.deployment.phase;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipSessionsUtil;
import javax.servlet.sip.TimerService;
import javax.servlet.sip.annotation.SipApplication;
import javax.servlet.sip.annotation.SipApplicationKey;
import javax.servlet.sip.annotation.SipListener;
import javax.servlet.sip.annotation.SipServlet;
import org.andrewwinter.jsr289.jboss.deployment.attachment.CustomAttachments;
import org.andrewwinter.jsr289.jboss.metadata.SipApplicationInfo;
import org.andrewwinter.jsr289.jboss.metadata.SipListenerInfo;
import org.andrewwinter.jsr289.jboss.metadata.SipModuleInfo;
import org.andrewwinter.jsr289.model.SipServletManager;
import org.jboss.as.ee.component.Attachments;
import org.jboss.as.ee.component.BindingConfiguration;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ee.component.deployers.EEResourceReferenceProcessor;
import org.jboss.as.ee.component.deployers.EEResourceReferenceProcessorRegistry;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.annotation.AnnotationIndexUtils;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;
import org.jboss.jandex.FieldInfo;
import org.jboss.jandex.Index;
import org.jboss.jandex.MethodInfo;

/**
 *
 * @author andrew
 */
public class AnnotationProcessor extends AbstractDeploymentUnitProcessor {

    private static final DotName RESOURCE_ANNOTATION_NAME = DotName.createSimple(Resource.class.getName());
    private static final DotName SIP_APPLICATION_KEY_ANNOTATION_NAME = DotName.createSimple(SipApplicationKey.class.getName());
    private static final DotName SIP_LISTENER_ANNOTATION_NAME = DotName.createSimple(SipListener.class.getName());
    private static final DotName SIP_SERVLET_ANNOTATION_NAME = DotName.createSimple(SipServlet.class.getName());
    private static final DotName SIP_APPLICATION_ANNOTATION_NAME = DotName.createSimple(SipApplication.class.getName());

    private static final DotName SIP_FACTORY_TYPE_NAME = DotName.createSimple(SipFactory.class.getName());
    private static final DotName SIP_SESSIONS_UTIL_TYPE_NAME = DotName.createSimple(SipSessionsUtil.class.getName());
    private static final DotName TIMER_SERVICE_TYPE_NAME = DotName.createSimple(TimerService.class.getName());
    
    private static final Set<DotName> RESOURCE_INJECTED_TYPES = new HashSet<>();

    static {
        RESOURCE_INJECTED_TYPES.add(SIP_FACTORY_TYPE_NAME);
        RESOURCE_INJECTED_TYPES.add(SIP_SESSIONS_UTIL_TYPE_NAME);
        RESOURCE_INJECTED_TYPES.add(TIMER_SERVICE_TYPE_NAME);
    }

    @Override
    public void deploy(final DeploymentPhaseContext dpc) throws DeploymentUnitProcessingException {

        final DeploymentUnit du = dpc.getDeploymentUnit();

        if (!isSipApplication(du)) {
            return;
        }
        
        final EEResourceReferenceProcessorRegistry registry = du.getAttachment(
                Attachments.RESOURCE_REFERENCE_PROCESSOR_REGISTRY);
        registry.registerResourceReferenceProcessor(new SipFactoryResourceReferenceProcessor(du));
        registry.registerResourceReferenceProcessor(new SipSessionsUtilResourceReferenceProcessor(du));
        registry.registerResourceReferenceProcessor(new TimerServiceResourceReferenceProcessor(du));

        final EEModuleDescription eeModuleDescription = du.getAttachment(Attachments.EE_MODULE_DESCRIPTION);

        final SipModuleInfo sipMetadata = du.getAttachment(CustomAttachments.SIP_MODULE_INFO);

        final Map<ResourceRoot, Index> map = AnnotationIndexUtils.getAnnotationIndexes(du);

        for (final Map.Entry<ResourceRoot, Index> entry : map.entrySet()) {
            processResourceAnnotation(entry.getValue(), registry, eeModuleDescription);
            processSipServletAnnotation(entry.getValue(), sipMetadata);
            processSipApplicationAnnotation(entry.getValue(), sipMetadata);
            processSipApplicationKeyAnnotation(entry.getValue(), sipMetadata);
            processSipListenerAnnotation(entry.getValue(), sipMetadata);
        }
        
        for (final String prefix : new String[] { "java:comp/env/sip/", "java:app/sip/" }) {
            bind(registry, eeModuleDescription, SIP_FACTORY_TYPE_NAME, prefix + sipMetadata.getAppName() + "/SipFactory");
            bind(registry, eeModuleDescription, SIP_SESSIONS_UTIL_TYPE_NAME, prefix + sipMetadata.getAppName() + "/SipSessionsUtil");
            bind(registry, eeModuleDescription, TIMER_SERVICE_TYPE_NAME, prefix + sipMetadata.getAppName() + "/TimerService");
        }
    } 

    private void bind(
            final EEResourceReferenceProcessorRegistry registry,
            final EEModuleDescription description,
            final DotName typeName,
            final String bindingName) throws DeploymentUnitProcessingException {
        final EEResourceReferenceProcessor processor = registry.getResourceReferenceProcessor(typeName.toString());
        final BindingConfiguration bindingConfiguration = new BindingConfiguration(
                bindingName,
                processor.getResourceReferenceBindingSource());
        description.getBindingConfigurations().add(bindingConfiguration);
    }
    
    /**
     * {@code
     *
     * @Resource} is used to inject: <ul> <li>{
     * @SipFactory}</li> <li>{
     * @SipSessionsUtil}</li> <li>{
     * @TimerService}</li> </ul>
     * @param index
     */
    private void processResourceAnnotation(
            final Index index,
            final EEResourceReferenceProcessorRegistry registry,
            final EEModuleDescription eeModuleDescription) throws DeploymentUnitProcessingException {

        final List<AnnotationInstance> annotations = index.getAnnotations(RESOURCE_ANNOTATION_NAME);
        for (final AnnotationInstance annotation : annotations) {

            if (annotation.target() instanceof FieldInfo) {
                final FieldInfo fi = (FieldInfo) annotation.target();

                final DotName type = fi.type().name();
                if (RESOURCE_INJECTED_TYPES.contains(type)) {

                    final String name = "java:comp/env/" + fi.declaringClass().name().toString() + "/" + fi.name();
                    bind(registry, eeModuleDescription, type, name);

                    // I don't think it's necessary to put the binding here but I tried it once.
                    // Delete these lines when we know for sure this isn't required.
                    // final EEModuleClassDescription classDescription = eeModuleDescription.addOrGetLocalClassDescription(fi.declaringClass().name().toString());
                    // classDescription.getBindingConfigurations().add(bindingConfiguration);
                }
            }
        }
    }

    private static String annotationValueAsString(
            final AnnotationInstance annotation,
            final String name) {

        final AnnotationValue value = annotation.value(name);
        String result = null;
        if (value != null) {
            result = value.asString();
        }
        return result;
    }

    /**
     *
     * @param index
     */
    private void processSipServletAnnotation(final Index index, final SipModuleInfo sipMetadata) throws DeploymentUnitProcessingException {
        final List<AnnotationInstance> annotations = index.getAnnotations(SIP_SERVLET_ANNOTATION_NAME);
        for (final AnnotationInstance annotation : annotations) {

            if (annotation.target() instanceof ClassInfo) {
                final ClassInfo info = (ClassInfo) annotation.target();

                final SipServletManager metadata = new SipServletManager(
                        info.name().toString(),
                        annotationValueAsString(annotation, "name"),
                        annotationValueAsString(annotation, "loadOnStartup"),
                        annotationValueAsString(annotation, "applicationName"),
                        annotationValueAsString(annotation, "description"));

                sipMetadata.add(metadata);
            } else {
                throw new DeploymentUnitProcessingException("@SipServlet appeared on something other than a class.");
            }
        }
    }

    /**
     *
     * @param index
     */
    private void processSipApplicationAnnotation(final Index index, final SipModuleInfo sipMetadata) throws DeploymentUnitProcessingException {
        final List<AnnotationInstance> annotations = index.getAnnotations(SIP_APPLICATION_ANNOTATION_NAME);
        for (final AnnotationInstance annotation : annotations) {

            if (annotation.target() instanceof ClassInfo) {
                final ClassInfo info = (ClassInfo) annotation.target();
                final String packageName = info.getClass().getPackage().getName().toString();

                final SipApplicationInfo metadata = new SipApplicationInfo();
                metadata.setAppName(annotationValueAsString(annotation, "name"));
                metadata.setPackageName(packageName);
                metadata.setDescription(annotationValueAsString(annotation, "description"));
                metadata.setDisplayName(annotationValueAsString(annotation, "displayName"));
                metadata.setDistributable(annotationValueAsString(annotation, "distributable"));
                metadata.setLargeIcon(annotationValueAsString(annotation, "largeIcon"));
                metadata.setMainServlet(annotationValueAsString(annotation, "mainServlet"));
                metadata.setProxyTimeout(annotationValueAsString(annotation, "proxyTimeout"));
                metadata.setSessionTimeout(annotationValueAsString(annotation, "sessionTimeout"));
                metadata.setSmallIcon(annotationValueAsString(annotation, "smallIcon"));
                
                sipMetadata.add(metadata);
            } else {
                throw new DeploymentUnitProcessingException("@SipApplication appeared on something other than a class.");
            }
        }
    }

    /**
     *
     * @param index
     */
    private void processSipApplicationKeyAnnotation(final Index index, final SipModuleInfo sipMetadata) throws DeploymentUnitProcessingException {
        final List<AnnotationInstance> annotations = index.getAnnotations(SIP_APPLICATION_KEY_ANNOTATION_NAME);
        for (final AnnotationInstance annotation : annotations) {

            if (annotation.target() instanceof MethodInfo) {
                final MethodInfo info = (MethodInfo) annotation.target();
                
                System.out.println("___________________________TODO____________ process @SipApplicationKey");
                
//                final String packageName = info.getClass().getPackage().getName().toString();
//
//                final SipApplicationInfo metadata = new SipApplicationInfo();
//                metadata.setAppName(annotationValueAsString(annotation, "name"));
//                metadata.setPackageName(packageName);
//                metadata.setDescription(annotationValueAsString(annotation, "description"));
//                metadata.setDisplayName(annotationValueAsString(annotation, "displayName"));
//                metadata.setDistributable(annotationValueAsString(annotation, "distributable"));
//                metadata.setLargeIcon(annotationValueAsString(annotation, "largeIcon"));
//                metadata.setMainServlet(annotationValueAsString(annotation, "mainServlet"));
//                metadata.setProxyTimeout(annotationValueAsString(annotation, "proxyTimeout"));
//                metadata.setSessionTimeout(annotationValueAsString(annotation, "sessionTimeout"));
//                metadata.setSmallIcon(annotationValueAsString(annotation, "smallIcon"));
//                
//                sipMetadata.add(metadata);
                
//                System.out.println(metadata);
                
            } else {
                throw new DeploymentUnitProcessingException("@SipApplicationKey appeared on something other than a method.");
            }
        }
    }

    /**
     *
     * @param index
     */
    private void processSipListenerAnnotation(final Index index, final SipModuleInfo sipMetadata) throws DeploymentUnitProcessingException {
        final List<AnnotationInstance> annotations = index.getAnnotations(SIP_LISTENER_ANNOTATION_NAME);
        for (final AnnotationInstance annotation : annotations) {

            if (annotation.target() instanceof ClassInfo) {
                final ClassInfo info = (ClassInfo) annotation.target();
                
                System.out.println("___________________________TODO____________ process @SipListener");
                
                for (final DotName iface : info.interfaces()) {
                    
                    final Class ifaceAsClass;
                    try {
                        ifaceAsClass = Class.forName(iface.toString());
                    } catch (ClassNotFoundException e) {
                        throw new DeploymentUnitProcessingException(e);
                    }
                    
                    if (org.andrewwinter.jsr289.util.Util.LISTENER_CLASSES.contains(ifaceAsClass)) {
                        
                        final SipListenerInfo listenerInfo = new SipListenerInfo(
                                ifaceAsClass,
                                info.name().toString());
                        
                        sipMetadata.add(listenerInfo);
                    }
                }

                // TODO: Add support for applicationName and description
                
            } else {
                throw new DeploymentUnitProcessingException("@SipListener appeared on something other than a class.");
            }
        }
    }

    
    //    private void processAnnotation(final Index index, final String annotationClassName) {
//
//        final DotName dotName = DotName.createSimple(annotationClassName);
//        final List<AnnotationInstance> annotations = index.getAnnotations(dotName);
//
//        for (final AnnotationInstance annotation : annotations) {
//
////ClassInfo
////FieldInfo
////MethodInfo
////MethodParameterInfo            
//
////            annotation.target().
//
//            System.out.println("============ annotation: " + annotationClassName + " " + annotation.target());
//        }
//    }
}
