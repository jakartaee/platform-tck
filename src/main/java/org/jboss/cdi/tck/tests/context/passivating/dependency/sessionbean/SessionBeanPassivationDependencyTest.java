package org.jboss.cdi.tck.tests.context.passivating.dependency.sessionbean;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_CAPABLE_DEPENDENCY_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.IOException;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class SessionBeanPassivationDependencyTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(SessionBeanPassivationDependencyTest.class).build();
    }

    @Inject
    Worker worker;

    @Inject
    Chef chef;

    @Test(groups=INTEGRATION)
    @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY_EE, id = "ab")
    public void testSingleton() throws IOException, ClassNotFoundException {
        assertNotNull(worker);
        assertNotNull(worker.getHammer());

        String workerId = worker.getId();
        String hammerId = worker.getHammer().getId();

        byte[] serializedWorker = passivate(worker);
        Worker workerCopy = (Worker) activate(serializedWorker);

        assertNotNull(workerCopy);
        assertNotNull(workerCopy.getHammer());
        assertEquals(workerCopy.getId(), workerId);
        assertEquals(workerCopy.getHammer().getId(), hammerId);
    }

    @Test(groups=INTEGRATION)
    @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY_EE, id = "aa")
    public void testStateless() throws IOException, ClassNotFoundException {
        assertNotNull(chef);
        assertNotNull(chef.getSpoon());

        String chefId = chef.getId();

        byte[] serializedChef = passivate(chef);
        Chef chefCopy = (Chef) activate(serializedChef);

        assertNotNull(chefCopy);
        assertNotNull(chefCopy.getSpoon());
        assertEquals(chefCopy.getId(), chefId);
        assertEquals(chefCopy.getSpoon().getId(), Spoon.class.getName());
    }

}
