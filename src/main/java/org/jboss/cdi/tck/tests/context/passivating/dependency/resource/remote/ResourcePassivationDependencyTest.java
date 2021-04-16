package org.jboss.cdi.tck.tests.context.passivating.dependency.resource.remote;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_CAPABLE_DEPENDENCY_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.cdi.tck.tests.context.passivating.dependency.resource.remote.ejb.FooBean;
import org.jboss.cdi.tck.tests.context.passivating.dependency.resource.remote.ejb.FooRemote;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class ResourcePassivationDependencyTest extends AbstractTest {

    @Deployment(name = "TEST", order = 2)
    public static EnterpriseArchive createTestArchive() {
        return new EnterpriseArchiveBuilder().withTestClass(ResourcePassivationDependencyTest.class)
                .setAsClientMode(false).withClasses(FooRemote.class, Worker.class).build();
    }

    @Deployment(name = "REMOTE_EJB", order = 1, testable = false)
    public static EnterpriseArchive createEjbArchive() {
        return new EnterpriseArchiveBuilder().notTestArchive().noDefaultWebModule().withName("test-ejb.ear")
                .withEjbModuleName("test-ejb.jar").withClasses(FooBean.class, FooRemote.class).build();
    }

    @OperateOnDeployment("TEST")
    @Test(groups = JAVAEE_FULL)
    @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY_EE, id = "dd")
    public void testRemoteSessionBean() throws IOException, ClassNotFoundException {

        Worker worker = getContextualReference(Worker.class);
        assertNotNull(worker);

        String workerId = worker.getId();
        String fooRemoteId = worker.getFoo().getId();

        byte[] serializedWorker = passivate(worker);
        Worker workerCopy = (Worker) activate(serializedWorker);

        assertNotNull(workerCopy);
        assertNotNull(workerCopy.getFoo());
        assertEquals(workerCopy.getId(), workerId);
        assertEquals(workerCopy.getFoo().getId(), fooRemoteId);

    }

}
