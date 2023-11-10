package org.jboss.cdi.tck.tests.alternative.selection.resource;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SELECTED_ALTERNATIVES_APPLICATION;
import static org.jboss.cdi.tck.tests.alternative.selection.SelectedAlternativeTestUtil.createBuilderBase;

import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.tests.alternative.selection.Alpha;
import org.jboss.cdi.tck.tests.alternative.selection.Bravo;
import org.jboss.cdi.tck.tests.alternative.selection.Charlie;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests that alternative resource can declare {@link jakarta.annotation.Priority} directly on the producer in order
 * to globally enable such a bean.
 */
@SpecVersion(spec = "cdi", version = "4.1")
public class ResourceAlternativeExplicitPriorityTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return createBuilderBase()
                .withTestClass(ResourceAlternativeExplicitPriorityTest.class)
                .withLibrary(ProductionReady.class)
                .withClasses(Alpha.class)
                .withBeanLibrary(Bravo.class, DeltaResourceProducer.class)
                .withBeanLibrary(Charlie.class)
                .withWebXml(
                        Descriptors.create(WebAppDescriptor.class).createEnvEntry().envEntryName("test1")
                                .envEntryType("java.lang.String").envEntryValue("hello").up()).build();
    }

    @Inject
    Alpha alpha;

    @Inject
    Bravo bravo;

    @Inject
    Charlie charlie;

    @Test(groups = { INTEGRATION })
    @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_APPLICATION, id = "cc")
    public void testAlternativeResourceSelected() {
        alpha.assertAvailable(String.class, ProductionReady.ProductionReadyLiteral.INSTANCE);
        bravo.assertAvailable(String.class, ProductionReady.ProductionReadyLiteral.INSTANCE);
        charlie.assertAvailable(String.class, ProductionReady.ProductionReadyLiteral.INSTANCE);
    }
}
