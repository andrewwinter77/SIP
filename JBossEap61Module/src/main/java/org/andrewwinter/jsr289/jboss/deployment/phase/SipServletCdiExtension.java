package org.andrewwinter.jsr289.jboss.deployment.phase;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.servlet.sip.SipServlet;

/**
 *
 * @author andrew
 */
public class SipServletCdiExtension implements Extension {

    public void afterBeanDiscovery(@Observes AfterBeanDiscovery abd) {
    }

    public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd, BeanManager bm) {
    }

    public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> event, BeanManager beanManager) {
        final AnnotatedType type = event.getAnnotatedType();
        if (SipServlet.class.isAssignableFrom(type.getJavaClass())) {

            // The extension uses an InjectionTarget to delegate instantiation,
            // dependency injection and lifecycle callbacks to the CDI
            // container.
            final InjectionTarget<SipServlet> it = beanManager.createInjectionTarget(type);

            // Each instance needs its own CDI CreationalContext
            final CreationalContext ctx = beanManager.createCreationalContext(null);

            // Instantiate the framework component and inject its dependencies.

            // Call the SipServlet's constructor.
            final SipServlet instance = it.produce(ctx);

            // Call initializer methods and perform field injection.
            it.inject(instance, ctx);

            // Call the @PostConstruct method.
            it.postConstruct(instance);
        }
    }
}
