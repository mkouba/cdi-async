package foo;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessProducerMethod;

/**
 *
 * @author Martin Kouba
 */
public class AsyncExtension implements Extension {

    private Set<Metadata> asynProducerMethods = new HashSet<>();

    void onAsyncProducerMethod(@Observes ProcessProducerMethod<? extends CompletionStage<?>, ?> event) {
        // Discover all producer methods returning CompletionStage<?>
        asynProducerMethods.add(new Metadata(event.getAnnotatedProducerMethod().getBaseType(), event.getBean().getQualifiers()));
    }

    @SuppressWarnings("unchecked")
    void addBeans(@Observes AfterBeanDiscovery event, BeanManager beanManager) {
        // For each "CompletionStage<Foo>" producer method also register a bean with type "Foo" which delegates to the producer method
        for (Metadata method : asynProducerMethods) {
            event.addBean().types(Collections.singleton(method.baseType)).scope(Dependent.class).qualifiers(method.qualifiers).createWith((ctx) -> {
                Bean<CompletionStage<?>> bean = (Bean<CompletionStage<?>>) beanManager
                        .resolve(beanManager.getBeans(method.producerType, method.qualifiers.toArray(new Annotation[] {})));
                try {
                    // Unlike the original producer method this blocks
                    return ((CompletionStage<?>) beanManager.getReference(bean, method.producerType, ctx)).toCompletableFuture().get();
                } catch (InterruptedException | ExecutionException e) {
                    // TODO
                    e.printStackTrace();
                    return null;
                }
            });
        }
    }

    static class Metadata {

        private Type producerType;

        private Type baseType;

        private Set<Annotation> qualifiers;

        public Metadata(Type producerType, Set<Annotation> qualifiers) {
            this.producerType = producerType;
            ParameterizedType parameterizedType = (ParameterizedType) producerType;
            this.baseType = parameterizedType.getActualTypeArguments()[0];
            this.qualifiers = qualifiers;
        }

    }

}
