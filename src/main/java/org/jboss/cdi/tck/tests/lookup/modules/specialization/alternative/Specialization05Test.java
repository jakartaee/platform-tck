package org.jboss.cdi.tck.tests.lookup.modules.specialization.alternative;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_ALTERNATIVE;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVES;
import static org.jboss.cdi.tck.cdi.Sections.SPECIALIZATION;
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
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class Specialization05Test extends AbstractTest {

    @Inject
    private InjectedBean1 bean1;

    @Inject
    private InjectedBean2 bean2;

    @Inject
    private Event<FactoryEvent> event;

    @Deployment
    public static EnterpriseArchive createTestArchive() {
        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder()
                .withTestClassDefinition(Specialization05Test.class)
                .noDefaultWebModule()
                .withBeanLibrary(Factory.class, Product.class, InjectedBean1.class, FactoryEvent.class)
                .withBeanLibrary(new BeansXml().alternatives(AlternativeSpecializedFactory.class), AlternativeSpecializedFactory.class)
                .build();

        enterpriseArchive.addAsModule(new WebArchiveBuilder().notTestArchive().withDefaultEjbModuleDependency()
                .withClasses(InjectedBean2.class,Specialization05Test.class).build());

        return enterpriseArchive;
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = DECLARING_ALTERNATIVE, id = "aa"), @SpecAssertion(section = SPECIALIZE_MANAGED_BEAN, id = "ac"),
            @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE, id = "i"), @SpecAssertion(section = SPECIALIZATION, id = "ca"),
            @SpecAssertion(section = SPECIALIZATION, id = "cb") })
    public void testEnabledAlternativeSpecializes() {
        assertTrue(bean1.getFactory().isUnsatisfied());
        assertTrue(bean1.getProduct().isUnsatisfied());

        assertTrue(bean2.getFactory().isUnsatisfied());
        assertTrue(bean2.getProduct().isUnsatisfied());
        assertFalse(bean2.getProduct().isAmbiguous());
    }
    
    
    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = OBSERVES, id = "a"), @SpecAssertion(section = SPECIALIZATION, id = "cc") })
    public void testEvent() {
        Factory.reset();
        event.fire(new FactoryEvent());
        assertFalse(Factory.isEventDelivered());
    }
}
