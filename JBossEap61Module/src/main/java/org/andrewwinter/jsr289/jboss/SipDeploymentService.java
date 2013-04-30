package org.andrewwinter.jsr289.jboss;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import org.andrewwinter.jsr289.util.ManagedClassInstantiator;
import org.andrewwinter.jsr289.jboss.deployment.attachment.CustomAttachments;
import org.andrewwinter.jsr289.model.SipModuleInfo;
import org.apache.catalina.core.StandardContext;
import org.jboss.as.naming.context.NamespaceContextSelector;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.weld.services.BeanManagerService;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleClassLoader;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistry;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;

public class SipDeploymentService implements Service<SipDeploymentService>, ManagedClassInstantiator {

    public static final ServiceName NAME = ServiceName.JBOSS.append("sipdeployment");
    private final DeploymentUnit du;
    private final NamespaceContextSelector namespaceContextSelector;

    public SipDeploymentService(final DeploymentUnit du, final NamespaceContextSelector namespaceContextSelector) {
        this.du = du;
        this.namespaceContextSelector = namespaceContextSelector;
    }

    private ModuleClassLoader getClassLoader() {
        // Andrew discovered the Module attachment by reading the source code
        // of the JBoss AS 7 class 'InstallReflectionIndexProcessor'.

        final Module module = (Module) du.getAttachment(Attachments.MODULE);
        return module.getClassLoader();
    }
    
    private BeanManager getBeanManager() {
        final ServiceRegistry registry = du.getServiceRegistry();
        final ServiceController<BeanManagerService> controller = (ServiceController<BeanManagerService>) registry.getService(
                BeanManagerService.serviceName(du));
        return (BeanManager) controller.getValue();
    }

    private SipServletService getSipServletService() {
        final ServiceRegistry registry = du.getServiceRegistry();
        final ServiceController<SipServletService> controller = (ServiceController<SipServletService>) registry.getService(
                SipServletService.SERVICE_NAME);
        return controller.getValue();
    }

    @Override
    public void start(final StartContext context) throws StartException {
        
        final SipModuleInfo moduleInfo = du.getAttachment(CustomAttachments.SIP_MODULE_INFO);

        final StandardContext standardContext = du.getAttachment(CustomAttachments.STANDARD_CONTEXT);
        
        moduleInfo.setClassLoader(getClassLoader());
        
        Thread.currentThread().setContextClassLoader(getClassLoader());

        // This will throw an exception if there is anything wrong with the metadata. For example,
        // if there is no app name, no servlets, etc. It also instantiates the servlets ready
        // for use.
        try {
            getSipServletService().deployApplication(moduleInfo, this, standardContext.getServletContext());
        } catch (Exception e) {
            throw new StartException(e);
        }
    }

    @Override
    public void stop(final StopContext context) {
    }

    @Override
    public SipDeploymentService getValue() throws IllegalStateException, IllegalArgumentException {
        return this;
    }

    @Override
    public void bindContexts() {
        NamespaceContextSelector.pushCurrentSelector(namespaceContextSelector);
    }

    @Override
    public void unbindContexts() {
        NamespaceContextSelector.popCurrentSelector();
    }
    /**
     * Use BeanManager to instantiate the objects.
     * 
     * @param clazz
     * @return 
     */
    @Override
    public Object instantiate(final Class clazz) {

        bindContexts();
        
        try {
            final BeanManager bm = getBeanManager();

            final AnnotatedType<Object> type = bm.createAnnotatedType(clazz);

            // The extension uses an InjectionTarget to delegate instantiation,
            // dependency injection and lifecycle callbacks to the CDI
            // container.
            final InjectionTarget<Object> it = bm.createInjectionTarget(type);

            // Each instance needs its own CDI CreationalContext
            final CreationalContext ctx = bm.createCreationalContext(null);

            // Instantiate the framework component and inject its dependencies.

            // Call the SipServlet's constructor.
            final Object instance = it.produce(ctx);

            // Call initializer methods and perform field injection.
            it.inject(instance, ctx);

            // Call the @PostConstruct method.
            it.postConstruct(instance);

            return instance;
        } catch (RuntimeException e) {
            throw e;
        } finally {
            unbindContexts();
        }
    }
}
