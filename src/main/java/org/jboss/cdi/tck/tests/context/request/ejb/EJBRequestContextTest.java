/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.cdi.tck.tests.context.request.ejb;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.REQUEST_CONTEXT_EE;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.cdi.tck.util.Timer;
import org.jboss.cdi.tck.util.Timer.StopCondition;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * EJB and related tests with the built-in request context.
 *
 * Clarification for request context lifecycle during remote method invocation of EJB is required - see CDI-180.
 *
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class EJBRequestContextTest extends AbstractTest {

    @Deployment(name = "TEST", order = 1)
    public static EnterpriseArchive createTestArchive() {
        return new EnterpriseArchiveBuilder().withTestClass(EJBRequestContextTest.class).setAsClientMode(false)
                .withClasses(FMS.class, FMSModelIII.class, BarBean.class, SimpleRequestBean.class, FooRemote.class).build();
    }

    @Deployment(name = "REMOTE_EJB", order = 2, testable = false)
    public static EnterpriseArchive createEjbArchive() {
        return new EnterpriseArchiveBuilder().notTestArchive().noDefaultWebModule().withName("test-ejb.ear")
                .withEjbModuleName("test-ejb.jar").withClasses(FooBean.class, FooRemote.class, FooRequestBean.class).build();
    }

    @EJB(lookup = "java:global/test-ejb/test-ejb/FooBean!org.jboss.cdi.tck.tests.context.request.ejb.FooRemote")
    FooRemote foo;

    @Inject
    BarBean bar;

    /**
     * The request scope is active during any remote method invocation of any EJB bean, during any call to an EJB timeout method
     * and during message delivery to any EJB message driven bean.
     */
    @OperateOnDeployment("TEST")
    @Test(groups = JAVAEE_FULL)
    @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "gc")
    public void testRequestScopeActiveDuringCallToEjbTimeoutMethod() throws Exception {
        FMSModelIII.reset();
        FMS flightManagementSystem = getContextualReference(FMS.class);
        flightManagementSystem.climb();

        new Timer().setDelay(20, TimeUnit.SECONDS).addStopCondition(new StopCondition() {
            public boolean isSatisfied() {
                return FMSModelIII.isClimbed();
            }
        }).start();

        assertTrue(flightManagementSystem.isRequestScopeActive());
    }

    /**
     * The request context is destroyed after the remote method invocation, timeout or message delivery completes.
     */
    @OperateOnDeployment("TEST")
    @Test(groups = JAVAEE_FULL)
    @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "hc")
    public void testRequestScopeDestroyedAfterCallToEjbTimeoutMethod() throws Exception {
        FMSModelIII.reset();
        SimpleRequestBean.reset();
        FMS flightManagementSystem = getContextualReference(FMS.class);

        flightManagementSystem.climb();

        Timer timer = new Timer().setDelay(20, TimeUnit.SECONDS).addStopCondition(new StopCondition() {
            public boolean isSatisfied() {
                return FMSModelIII.isClimbed();
            }
        }).start();

        flightManagementSystem.descend();

        timer.addStopCondition(new StopCondition() {
            public boolean isSatisfied() {
                return FMSModelIII.isDescended();
            }
        }, true).start();

        assertFalse(flightManagementSystem.isSameBean());
        assertTrue(SimpleRequestBean.isBeanDestroyed());
    }

    @OperateOnDeployment("TEST")
    @Test(groups = JAVAEE_FULL)
    @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "ga")
    @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "ha")
    public void testRequestScopeActiveDuringRemoteCallToEjb() throws Exception {
        assertNotNull(foo.ping());
        assertTrue(foo.wasRequestBeanInPreviousCallDestroyed());
    }

    @OperateOnDeployment("TEST")
    @Test(groups = JAVAEE_FULL)
    @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "gb")
    @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "hb")
    public void testRequestScopeActiveDuringAsyncCallToEjb() throws Exception {
        SimpleRequestBean simpleRequestBean = getContextualReference(SimpleRequestBean.class);
        SimpleRequestBean.reset();
        Future<String> result = bar.compute();
        String id = result.get();
        assertNotNull(id);
        assertNotEquals(id, simpleRequestBean.getId());
        assertTrue(SimpleRequestBean.isBeanDestroyed());
    }

}
