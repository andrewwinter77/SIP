package org.andrewwinter.jsr289.jboss.deployment.phase;

import java.lang.annotation.Annotation;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.enterprise.inject.spi.ProcessSessionBean;
import javax.enterprise.util.AnnotationLiteral;

/**
 *
 * @author andrew
 */
public class SipServletCdiExtension implements Extension {

    public static final Annotation APPLICATION_SCOPED_LITERAL = new AnnotationLiteral<ApplicationScoped>() {
    };

    public void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
    }

    public void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd, BeanManager bm) {
    }

    public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> event, BeanManager beanManager) {
    }

    public <T> void observeInjectionTarget(@Observes ProcessInjectionTarget<T> event) {
    }

    public <T> void observeSessionBeans(@Observes ProcessSessionBean<T> event) {
    }
}
