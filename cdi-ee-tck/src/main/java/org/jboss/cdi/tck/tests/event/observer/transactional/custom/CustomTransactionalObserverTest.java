/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.event.observer.transactional.custom;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.TestGroups.PERSISTENCE;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_NOTIFICATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Inject;

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
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class CustomTransactionalObserverTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(CustomTransactionalObserverTest.class)
                .withExtension(ObserverExtension.class).withDefaultPersistenceXml().build();
    }

    @Inject
    private GiraffeService giraffeService;

    @Inject
    private ObserverExtension extension;

    @Test(groups = { INTEGRATION, PERSISTENCE })
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_NOTIFICATION, id = "fa"),
            @SpecAssertion(section = OBSERVER_NOTIFICATION, id = "fb") })
    public void testCustomTransactionalObserver() throws Exception {

        ActionSequence.reset();

        // GiraffeObserver 2x, GiraffeCustomObserver 1x
        assertEquals(getCurrentManager().resolveObserverMethods(new Giraffe()).size(), 3);

        // Transactional invocation
        giraffeService.feed();

        // Test ObserverMethod.notify() was called
        assertNotNull(extension.getAnyGiraffeObserver().getReceivedPayload());
        assertEquals(Giraffe.class, extension.getAnyGiraffeObserver().getReceivedPayload().getEvent().getClass());
        assertNotNull(extension.getAnyGiraffeObserver().getReceivedPayload().getMetadata());
        // Test ObserverMethod.getTransactionPhase() was called
        assertTrue(extension.getAnyGiraffeObserver().isTransactionPhaseCalled());

        // Test custom observer received notification during the after completion phase (after succesfull commit)
        // BEFORE_COMPLETION must be fired at the beginning of the commit (after checkpoint)
        // AFTER_SUCCESS and AFTER_COMPLETION must be fired after BEFORE_COMPLETION
        // AFTER_FAILURE is not fired
        ActionSequence.getSequence().beginsWith("checkpoint", TransactionPhase.BEFORE_COMPLETION.toString());
        ActionSequence.getSequence().assertDataContainsAll(TransactionPhase.AFTER_SUCCESS.toString(),
                TransactionPhase.AFTER_COMPLETION.toString());
    }

}
