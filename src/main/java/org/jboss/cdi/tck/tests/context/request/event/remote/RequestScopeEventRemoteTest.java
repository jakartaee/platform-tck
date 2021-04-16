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
package org.jboss.cdi.tck.tests.context.request.event.remote;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.REQUEST_CONTEXT_EE;
import static org.testng.Assert.assertTrue;

import jakarta.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Clarification for request context lifecycle during remote method invocation of EJB is required - see CDI-180.
 * 
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class RequestScopeEventRemoteTest extends AbstractTest {

    @Deployment(name = "TEST", order = 1)
    public static EnterpriseArchive createTestArchive() {
        return new EnterpriseArchiveBuilder().withTestClass(RequestScopeEventRemoteTest.class).setAsClientMode(false)
                .withClasses(FooRemote.class).build();
    }

    @Deployment(name = "REMOTE_EJB", order = 2, testable = false)
    public static EnterpriseArchive createEjbArchive() {
        return new EnterpriseArchiveBuilder().notTestArchive().noDefaultWebModule().withName("test-ejb.ear")
                .withEjbModuleName("test-ejb.jar")
                .withClasses(FooBean.class, FooRemote.class, RequestScopedObserver.class, ApplicationScopedObserver.class)
                .build();
    }

    @EJB(lookup = "java:global/test-ejb/test-ejb/FooBean!org.jboss.cdi.tck.tests.context.request.event.remote.FooRemote")
    FooRemote foo;

    @OperateOnDeployment("TEST")
    @Test(groups =  JAVAEE_FULL)
    @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "jc")
    public void testRequestScopeActiveDuringRemoteCallToEjb() throws Exception {

        assertTrue(foo.first());
        assertTrue(foo.second());
    }

}
