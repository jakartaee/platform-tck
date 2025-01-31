package tck.arquillian.porting.lib.spi;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;

import java.lang.annotation.Annotation;

/**
 * A base class for {@link TestArchiveProcessor} implementations that also provides the required
 * {@link ResourceProvider} implementation to be able to inject the {@link TestArchiveProcessor} instance.
 */
public abstract class AbstractTestArchiveProcessor implements TestArchiveProcessor, ResourceProvider {
    @Inject
    @ApplicationScoped
    private InstanceProducer<TestArchiveProcessor> archiveProcessorProducer;

    /**
     * Called on completion of the Arquillian configuration. Subclasses that override this method must
     * call super.initalize(descriptor) to ensure that the {@link TestArchiveProcessor} producer instance is set.
     * @param descriptor the Arquillian configuration descriptor
     */
    public void initalize(@Observes ArquillianDescriptor descriptor) {
        archiveProcessorProducer.set(this);
    }

    @Override
    public boolean canProvide(Class<?> type) {
        return type.isAssignableFrom(TestArchiveProcessor.class);
    }

    @Override
    public Object lookup(ArquillianResource resource, Annotation... qualifiers) {
        return archiveProcessorProducer.get();
    }
}
