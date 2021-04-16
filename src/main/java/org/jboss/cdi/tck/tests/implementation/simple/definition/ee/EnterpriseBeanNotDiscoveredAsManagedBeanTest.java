package org.jboss.cdi.tck.tests.implementation.simple.definition.ee;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.WHAT_CLASSES_ARE_BEANS_EE;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.implementation.simple.definition.ClovenHoved;
import org.jboss.cdi.tck.tests.implementation.simple.definition.Sheep;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseBeanNotDiscoveredAsManagedBeanTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(EnterpriseBeanNotDiscoveredAsManagedBeanTest.class)
                .withClasses(EnterpriseBeanObserver.class, Sheep.class, MockEnterpriseBean.class, ClovenHoved.class)
                .withExtension(EnterpriseBeanObserver.class).build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = WHAT_CLASSES_ARE_BEANS_EE, id = "a")
    public void testClassesImplementingEnterpriseBeanInterfaceNotDiscoveredAsSimpleBean() {
        assert !EnterpriseBeanObserver.observedEnterpriseBean;
        assert EnterpriseBeanObserver.observedAnotherBean;
    }

}
