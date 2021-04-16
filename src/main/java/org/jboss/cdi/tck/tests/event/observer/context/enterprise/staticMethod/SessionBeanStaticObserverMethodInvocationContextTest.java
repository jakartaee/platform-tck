/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.event.observer.context.enterprise.staticMethod;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.TestGroups.PERSISTENCE;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_METHOD_INVOCATION_CONTEXT;

import jakarta.enterprise.event.TransactionPhase;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Matus Abaffy
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class SessionBeanStaticObserverMethodInvocationContextTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(SessionBeanStaticObserverMethodInvocationContextTest.class)
                .withDefaultPersistenceXml()
                .build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = { PERSISTENCE, JAVAEE_FULL })
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_METHOD_INVOCATION_CONTEXT, id = "a"),
            @SpecAssertion(section = OBSERVER_METHOD_INVOCATION_CONTEXT, id = "b"),
            @SpecAssertion(section = OBSERVER_METHOD_INVOCATION_CONTEXT, id = "c") })
    public void testTransactionalObserverMethod(Student student) throws Exception {
        ActionSequence.reset();

        student.printSuccess();
        student.printFailure();

        // Checking transaction and client security context is done in FooObserver
        ActionSequence.assertSequenceDataContainsAll(TransactionPhase.IN_PROGRESS.toString(),
                TransactionPhase.BEFORE_COMPLETION.toString(), TransactionPhase.AFTER_COMPLETION.toString(),
                TransactionPhase.AFTER_SUCCESS.toString(), TransactionPhase.AFTER_FAILURE.toString());
    }
}
