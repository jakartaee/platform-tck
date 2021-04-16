package org.jboss.cdi.tck.tests.lookup.modules.specialization.alternative;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_ALTERNATIVE;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVES;
import static org.jboss.cdi.tck.cdi.Sections.SPECIALIZE_MANAGED_BEAN;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class Specialization06Test extends AbstractTest {

    @Inject
    private InjectedBean1 bean1;

    @Inject
    private InjectedBean2 bean2;

    @Inject
    private Event<FactoryEvent> event;

    @Deployment
    public static EnterpriseArchive createTestArchive() {
        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder().withTestClassDefinition(Specialization06Test.class).noDefaultWebModule()
                .withBeanLibrary(Factory.class, Product.class, InjectedBean2.class, FactoryEvent.class).withBeanLibrary(AlternativeSpecializedFactory.class)
                .build();

        enterpriseArchive.addAsModule(new WebArchiveBuilder().notTestArchive().withDefaultEjbModuleDependency()
                .withClasses(InjectedBean1.class, Specialization06Test.class).build());

        return enterpriseArchive;
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = DECLARING_ALTERNATIVE, id = "aa"), @SpecAssertion(section = SPECIALIZE_MANAGED_BEAN, id = "ac") })
    public void testEnabledAlternativeSpecializes() {
        assertTrue(bean1.getFactory().get() instanceof Factory);
        assertFalse(bean1.getProduct().isUnsatisfied());

        assertTrue(bean2.getFactory().get() instanceof Factory);
        assertFalse(bean2.getProduct().isUnsatisfied());

    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = OBSERVES, id = "a") })
    public void testEvent() {
        Factory.reset();
        event.fire(new FactoryEvent());
        assertTrue(Factory.isEventDelivered());
    }
}